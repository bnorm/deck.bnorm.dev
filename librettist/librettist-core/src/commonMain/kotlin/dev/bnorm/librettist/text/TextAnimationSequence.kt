package dev.bnorm.librettist.text

import androidx.compose.runtime.*
import dev.bnorm.librettist.animation.AnimationState
import dev.bnorm.librettist.animation.LaunchedAnimation
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.show.SlideScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class TextAnimationSequence(
    val start: String,
    val end: String,
    val flow: Flow<String>
)

fun startTextAnimation(start: String): TextAnimationSequence {
    return TextAnimationSequence(start, start, flowOf(start))
}

@Composable
fun AnimateText(
    sequence: TextAnimationSequence,
    state: MutableState<AnimationState>,
    delay: Duration = 50.milliseconds,
    content: @Composable (String) -> Unit
) {
    var text by remember(sequence) {
        mutableStateOf(if (state.value == AnimationState.PENDING) sequence.start else sequence.end)
    }

    LaunchedAnimation(state) {
        when (it) {
            AnimationState.PENDING -> text = sequence.start
            AnimationState.RUNNING -> sequence.flow.dedup().collect { delay(delay); text = it }
            AnimationState.COMPLETE -> text = sequence.end
        }
    }

    content(text)
}


@Composable
fun SlideScope.AnimateText(
    sequence: TextAnimationSequence,
    state: MutableState<AnimationState> = rememberAdvancementAnimation(),
    delay: Duration = 50.milliseconds,
    content: @Composable (String) -> Unit
) {
    var text by remember(sequence) {
        mutableStateOf(if (state.value == AnimationState.PENDING) sequence.start else sequence.end)
    }

    LaunchedAnimation(state) {
        when (it) {
            AnimationState.PENDING -> text = sequence.start
            AnimationState.RUNNING -> sequence.flow.dedup().collect { delay(delay); text = it }
            AnimationState.COMPLETE -> text = sequence.end
        }
    }

    content(text)
}
