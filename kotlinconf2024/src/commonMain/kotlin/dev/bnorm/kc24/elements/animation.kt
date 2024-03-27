package dev.bnorm.kc24.elements

import androidx.compose.animation.core.*
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
