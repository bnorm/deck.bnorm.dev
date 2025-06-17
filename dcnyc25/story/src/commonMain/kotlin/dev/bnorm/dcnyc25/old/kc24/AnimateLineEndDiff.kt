package dev.bnorm.dcnyc25.old.kc24

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString

fun AnnotatedString.flowLineEndDiff(other: AnnotatedString): Sequence<AnnotatedString> {
    val leftLines = this.annotatedLines()
    val rightLines = other.annotatedLines()
    require(rightLines.size == leftLines.size)
    val lines = leftLines.size

    return sequence {
        yield(this@flowLineEndDiff)
        for (lineIndex in 0..<lines) {
            val left = leftLines[lineIndex]
            val right = rightLines[lineIndex]

            fun buildString(i: Int, line: AnnotatedString): AnnotatedString {
                return buildAnnotatedString {
                    for (l in 0..<lineIndex) {
                        append(rightLines[l])
                        if (l + 1 < lines) appendLine()
                    }
                    append(line.subSequence(0, i))
                    for (l in lineIndex + 1..<lines) {
                        appendLine()
                        append(leftLines[l])
                    }
                }
            }

            // TODO codepoints?
            var index = 0
            while (index < left.length && index < right.length && left[index] == right[index]) index++

            if (index < left.length) {
                for (i in (index..<left.length).reversed()) {
                    yield(buildString(i, left))
                }
            }
            if (index < right.length) {
                for (i in index + 1..right.length) {
                    yield(buildString(i, right))
                }
            }
        }
        yield(other)
    }
}

fun AnimationSequence<AnnotatedString>.thenLineEndDiff(next: AnnotatedString): AnimationSequence<AnnotatedString> {
    val nextSequence = end.flowLineEndDiff(next)
    val sequence = sequence { yieldAll(sequence); yieldAll(nextSequence) }
    return copy(end = next, sequence = sequence)
}
