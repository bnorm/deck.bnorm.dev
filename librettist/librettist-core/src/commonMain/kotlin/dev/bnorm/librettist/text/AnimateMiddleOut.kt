package dev.bnorm.librettist.text

import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun String.animateMiddleOut(charDelay: Duration = 50.milliseconds): State<String> {
    if (isEmpty()) {
        return mutableStateOf("")
    } else {
        val start = substring(0, length / 2)
        val end = substring((length + 1) / 2, length)

        val state = remember {
            mutableStateOf(
                when {
                    length % 2 == 0 -> ""
                    else -> this[length / 2].toString()
                }
            )
        }

        LaunchedEffect(Unit) {
            val mutex = Mutex()
            launch {
                start.reversed().asIterable().asFlow()
                    .onEach { if (!it.isWhitespace()) delay(charDelay) }
                    .collect { mutex.withLock { state.value = it + state.value } }
            }
            launch {
                end.asIterable().asFlow()
                    .onEach { if (!it.isWhitespace()) delay(charDelay) }
                    .collect { mutex.withLock { state.value += it } }
            }
        }

        return state
    }
}
