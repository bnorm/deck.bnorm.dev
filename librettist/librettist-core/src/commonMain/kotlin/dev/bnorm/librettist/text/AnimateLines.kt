package dev.bnorm.librettist.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun String.flowLines(other: String, lineDelay: Duration = 50.milliseconds): Flow<String> {
    val thisLines = this.lines()
    val otherLines = other.lines()
    require(otherLines.size >= thisLines.size)

    return flow {
        emit(this@flowLines)
        for (line in otherLines.indices) {
            delay(lineDelay)
            emit(buildString {
                for (i in 0..line) {
                    appendLine(otherLines[i])
                }
                for (i in line + 1..<thisLines.size) {
                    appendLine(thisLines[i])
                }
            })
        }
    }
}

fun flowLines(values: List<String>, lineDelay: Duration = 50.milliseconds): Flow<String> {
    require(values.isNotEmpty())
    return values.zipWithNext { a, b -> a.flowLines(b, lineDelay) }.concat()
}

@Composable
fun String.animateLines(other: String, lineDelay: Duration = 50.milliseconds): State<String> {
    val flowLines = remember(this, other) { flowLines(other, lineDelay) }
    return flowLines.collectAsState(this)
}

@Composable
fun animateLines(values: List<String>, lineDelay: Duration = 50.milliseconds): State<String> {
    val flowLines = remember(values) { flowLines(values, lineDelay) }
    return flowLines.collectAsState(values[0])
}
