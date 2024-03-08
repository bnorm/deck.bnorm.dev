package dev.bnorm.librettist.text

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

fun String.flowLineEndDiff(other: String): Flow<String> {
    val leftLines = this.lines()
    val rightLines = other.lines()
    require(rightLines.size == leftLines.size)
    val lines = leftLines.size

    return flow {
        emit(this@flowLineEndDiff)
        for (line in 0..<lines) {
            val left = leftLines[line]
            val right = rightLines[line]

            fun buildLeftString(i: Int): String {
                return buildString {
                    for (l in 0..<line) appendLine(leftLines[l])
                    appendLine(left.substring(0, i))
                    for (l in line + 1..<lines) appendLine(rightLines[l])
                }
            }

            fun buildRightString(i: Int): String {
                return buildString {
                    for (l in 0..<line) appendLine(leftLines[l])
                    appendLine(right.substring(0, i))
                    for (l in line + 1..<lines) appendLine(rightLines[l])
                }
            }

            // TODO codepoints
            var index = 0
            while (index < left.length && index < right.length && left[index] == right[index]) index++

            if (index < left.length) {
                for (i in (index..<left.length).reversed()) {
                    emit(buildLeftString(i))
                }
            }
            if (index < right.length) {
                for (i in index + 1..right.length) {
                    emit(buildRightString(i))
                }
            }
        }
    }
}

fun TextAnimationSequence.thenLineEndDiff(next: String): TextAnimationSequence {
    val nextFlow = end.flowLineEndDiff(next)
    val flow = flow { emitAll(flow); emitAll(nextFlow) }
    return copy(end = next, flow = flow)
}
