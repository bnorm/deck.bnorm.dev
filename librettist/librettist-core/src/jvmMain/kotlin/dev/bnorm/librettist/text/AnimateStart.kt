package dev.bnorm.librettist.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun String.flowStart(charDelay: Duration = 50.milliseconds): Flow<String> = flow {
    val value = StringBuilder()
    emit(value.toString())

    for (codePoint in this@flowStart.codePoints()) {
        value.appendCodePoint(codePoint)
        if (!value.last().isWhitespace()) {
            delay(charDelay)
            emit(value.toString())
        }
    }

    emit(value.toString())
}

@Composable
fun String.stateStart(charDelay: Duration = 50.milliseconds) =
    flowStart(charDelay).collectAsState(this)
