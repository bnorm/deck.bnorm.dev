package dev.bnorm.librettist.text

import dev.bnorm.librettist.animation.AnimationSequence

fun String.flowLines(other: String): Sequence<String> {
    val thisLines = this.lines()
    val otherLines = other.lines()
    require(otherLines.size >= thisLines.size)

    return sequence {
        yield(this@flowLines)
        for (line in otherLines.indices) {
            yield(buildString {
                for (i in 0..line) {
                    appendLine(otherLines[i])
                }
                for (i in line + 1..<thisLines.size) {
                    appendLine(thisLines[i])
                }
            }.trim())
        }
    }
}

fun AnimationSequence<String>.thenLines(next: String): AnimationSequence<String> {
    val nextSequence = end.flowLines(next)
    val sequence = sequence { yieldAll(sequence); yieldAll(nextSequence) }
    return copy(end = next, sequence = sequence)
}
