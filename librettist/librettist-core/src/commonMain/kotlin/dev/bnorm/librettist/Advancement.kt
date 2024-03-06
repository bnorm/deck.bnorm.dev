package dev.bnorm.librettist

import androidx.compose.runtime.*

data class Advancement(
    val forward: Boolean
)

typealias AdvancementHandler = (Advancement) -> Boolean

data class AdvancementState(
    var lastAdvancement: Advancement = Advancement(forward = true),
    val handlers: MutableList<AdvancementHandler> = mutableListOf()
)

val LocalAdvancementState = compositionLocalOf<AdvancementState> {
    error("LocalAdvancementState is not provided")
}

@Composable
fun lastAdvancement(): Advancement {
    return LocalAdvancementState.current.lastAdvancement
}

@Composable
fun ListenAdvancement(handler: AdvancementHandler) {
    // TODO there needs to be a way for advancement to know when to start at the end rather than the beginning
    //  i.e., when navigating back to a slide with sub-advancement
    //  return the last advancement?

    val handlerState = rememberUpdatedState(handler)
    val state = LocalAdvancementState.current
    DisposableEffect(handlerState) {
        val localHandler: AdvancementHandler = {
            handlerState.value(it)
        }
        state.handlers.add(localHandler)
        onDispose {
            state.handlers.remove(localHandler)
        }
    }
}

@Composable
fun rememberAdvancementBoolean(): State<Boolean> {
    val start = !lastAdvancement().forward
    val state = remember { mutableStateOf(start) }
    ListenAdvancement {
        val flip = it.forward xor state.value
        if (flip) state.value = !state.value
        return@ListenAdvancement flip
    }
    return state
}

@Composable
fun rememberAdvancementIndex(count: Int, start: Int? = null): MutableState<Int> {
    val lastAdvancement = lastAdvancement()
    val index = remember { mutableStateOf(start ?: if (lastAdvancement.forward) 0 else count - 1) }

    ListenAdvancement {
        val nextSlide = when (it.forward) {
            true -> index.value + 1
            false -> index.value - 1
        }

        val inRange = nextSlide in 0..<count
        if (inRange) index.value = nextSlide
        return@ListenAdvancement inRange
    }

    return index
}
