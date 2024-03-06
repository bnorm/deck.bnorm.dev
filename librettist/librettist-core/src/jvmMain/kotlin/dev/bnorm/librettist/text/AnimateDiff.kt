package dev.bnorm.librettist.text

import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.apache.commons.text.diff.ReplacementsFinder
import org.apache.commons.text.diff.StringsComparator
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private data class Diff(
    val skipped: Int,
    val from: List<Char>,
    val to: List<Char>,
)

fun String.flowDiff(other: String, charDelay: Duration = 50.milliseconds): Flow<String> {
    // TODO: diff based on words?
    // TODO: diff based on the end of the line?
    // TODO: write our own diff algorithm? (work is all public...) base it on code points instead! YES!
    // TODO: how to handle code points? https://github.com/cketti/kotlin-codepoints

    val diffs = buildList {
        StringsComparator(this@flowDiff, other).script.visit(ReplacementsFinder { skipped, from, to ->
            add(Diff(skipped, from.toList(), to.toList()))
        })
    }

    return flow {
        var value = this@flowDiff
        emit(value)

        var offset = 0
        for ((skipped, from, to) in diffs) {
            offset += skipped
            val start = value.substring(0, offset)
            val end = value.substring(offset + from.size)

            for (i in from.indices.reversed()) {
                if (from[i].isWhitespace()) continue

                delay(charDelay)
                value = buildString {
                    append(start)
                    for (j in 0..<i) {
                        append(from[j])
                    }
                    append(end)
                }
                emit(value)
            }

            for (i in to.indices) {
                if (to[i].isWhitespace()) continue

                delay(charDelay)
                value = buildString {
                    append(start)
                    for (j in 0..i) {
                        append(to[j])
                    }
                    append(end)
                }
                offset++
                emit(value)
            }
        }
    }
}

fun flowDiff(values: List<String>, charDelay: Duration = 50.milliseconds): Flow<String> {
    require(values.isNotEmpty())
    return values.zipWithNext { a, b -> a.flowDiff(b, charDelay) }.concat()
}

@Composable
fun String.animateDiff(other: String, charDelay: Duration = 50.milliseconds): State<String> {
    val flowDiff = remember(this, other) { flowDiff(other, charDelay) }
    return flowDiff.collectAsState(this)
}

@Composable
fun animateDiff(values: List<String>, charDelay: Duration = 50.milliseconds): State<String> {
    val flowDiff = remember(values) { flowDiff(values, charDelay) }
    return flowDiff.collectAsState(values[0])
}
