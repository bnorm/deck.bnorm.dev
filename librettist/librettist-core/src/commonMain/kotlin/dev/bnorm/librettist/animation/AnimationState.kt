package dev.bnorm.librettist.animation

import androidx.compose.runtime.*
import dev.bnorm.librettist.ListenAdvancement
import dev.bnorm.librettist.lastAdvancement
import kotlinx.coroutines.CoroutineScope

enum class AnimationState {
    PENDING,
    RUNNING,
    COMPLETE,
}

@Composable
fun LaunchedAnimation(
    state: MutableState<AnimationState>,
    block: suspend CoroutineScope.(AnimationState) -> Unit,
) {
    LaunchedEffect(state.value) {
        block(state.value)
        if (state.value == AnimationState.RUNNING) {
            state.value = AnimationState.COMPLETE
        }
    }
}

@Composable
fun rememberAdvancementAnimation(): MutableState<AnimationState> {
    val lastAdvancement = lastAdvancement()
    val state = remember {
        mutableStateOf(if (lastAdvancement.forward) AnimationState.PENDING else AnimationState.COMPLETE)
    }

    ListenAdvancement {
        if (it.forward) {
            when (state.value) {
                AnimationState.PENDING -> state.value = AnimationState.RUNNING
                AnimationState.RUNNING -> state.value = AnimationState.COMPLETE
                AnimationState.COMPLETE -> return@ListenAdvancement false
            }
        } else {
            when (state.value) {
                AnimationState.PENDING -> return@ListenAdvancement false
                AnimationState.RUNNING -> state.value = AnimationState.PENDING
                AnimationState.COMPLETE -> state.value = AnimationState.PENDING
            }
        }

        return@ListenAdvancement true
    }

    return state
}

@Composable
fun rememberAdvancementAnimations(count: Int): List<MutableState<AnimationState>> {
    require(count > 1)
    val lastAdvancement = lastAdvancement()

    var index = remember(count) { if (lastAdvancement.forward) 0 else count - 1 }
    val states = remember(count) {
        val start = if (lastAdvancement.forward) AnimationState.PENDING else AnimationState.COMPLETE
        List(count) { mutableStateOf(start) }
    }

    ListenAdvancement {
        fun handle(state: MutableState<AnimationState>): Boolean {
            if (it.forward) {
                when (state.value) {
                    AnimationState.PENDING -> state.value = AnimationState.RUNNING
                    AnimationState.RUNNING -> state.value = AnimationState.COMPLETE

                    AnimationState.COMPLETE -> {
                        index = minOf(index + 1, count - 1)
                        return false
                    }
                }
            } else {
                when (state.value) {
                    AnimationState.PENDING -> {
                        index = maxOf(index - 1, 0)
                        return false
                    }

                    AnimationState.RUNNING -> state.value = AnimationState.PENDING
                    AnimationState.COMPLETE -> state.value = AnimationState.PENDING
                }
            }
            return true
        }

        if (handle(states[index])) return@ListenAdvancement true
        return@ListenAdvancement handle(states[index]) // Retry in case we've moved to the next/previous animation
    }

    return states
}
