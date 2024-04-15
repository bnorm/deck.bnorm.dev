package dev.bnorm.kc24.elements

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun <T> defaultSpec(
    duration: Duration = 300.milliseconds,
    delay: Duration = Duration.ZERO,
    easing: Easing = FastOutSlowInEasing,
): FiniteAnimationSpec<T> {
    return tween(
        durationMillis = duration.inWholeMilliseconds.toInt(),
        delayMillis = delay.inWholeMilliseconds.toInt(),
        easing = easing,
    )
}

fun <T> typingSpec(
    duration: Duration,
    delay: Duration = Duration.ZERO,
    easing: Easing = LinearEasing,
): FiniteAnimationSpec<T> {
    return tween(
        durationMillis = duration.inWholeMilliseconds.toInt(),
        delayMillis = delay.inWholeMilliseconds.toInt(),
        easing = easing,
    )
}

fun <T> typingSpec(
    count: Int,
    charDelay: Duration = 20.milliseconds,
    delay: Duration = Duration.ZERO,
    easing: Easing = LinearEasing,
): FiniteAnimationSpec<T> {
    return tween(
        durationMillis = count * charDelay.inWholeMilliseconds.toInt(),
        delayMillis = delay.inWholeMilliseconds.toInt(),
        easing = easing,
    )
}

@Composable
fun <T> Transition<Int>.animateThrough(
    sequence: ImmutableList<ImmutableList<T>>,
    transitionSpec: @Composable (Int) -> FiniteAnimationSpec<Int> = { typingSpec(count = it) }
): State<T> {
    val values = remember(sequence) { sequence.flatten().toImmutableList() }
    val mapping = remember(sequence) {
        val mapping = mutableMapOf<Int, Int>()
        mapping[0] = 0

        var size = 0
        for ((index, value) in sequence.withIndex()) {
            size += value.size
            mapping[index + 1] = size - 1
        }

        mapping
    }

    val index = animateInt(
        transitionSpec = { transitionSpec(abs(mapping.getValue(targetState) - mapping.getValue(initialState))) },
        label = "ListAnimation",
        targetValueByState = { mapping.getValue(it) },
    )

    // TODO is this the best way to map a state object?
    return derivedStateOf { values[index.value] }
}
