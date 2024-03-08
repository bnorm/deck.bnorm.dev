package dev.bnorm.librettist.text

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

// TODO: diff based on words?
// TODO: write our own diff algorithm? (work is all public...) base it on code points instead! YES!
// TODO: how to handle code points? https://github.com/cketti/kotlin-codepoints
expect fun String.flowDiff(other: String): Flow<String>

fun TextAnimationSequence.thenDiff(next: String): TextAnimationSequence {
    val nextFlow = end.flowDiff(next)
    val flow = flow { emitAll(flow); emitAll(nextFlow) }
    return copy(end = next, flow = flow)
}
