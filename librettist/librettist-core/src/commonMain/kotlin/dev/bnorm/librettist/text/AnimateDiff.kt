package dev.bnorm.librettist.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

// TODO: diff based on words?
// TODO: diff based on the end of the line?
// TODO: write our own diff algorithm? (work is all public...) base it on code points instead! YES!
// TODO: how to handle code points? https://github.com/cketti/kotlin-codepoints
expect fun String.flowDiff(other: String, charDelay: Duration = 50.milliseconds): Flow<String>

fun flowDiff(values: List<String>, charDelay: Duration = 50.milliseconds): Flow<String> {
    require(values.isNotEmpty())
    return values.zipWithNext { a, b -> a.flowDiff(b, charDelay) }.concat().dedup()
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
