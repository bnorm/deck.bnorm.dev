package dev.bnorm.kc24.elements

import androidx.compose.animation.core.Transition

fun <T> Transition<out T>.at(state: T): Boolean {
    return !isRunning && currentState == state
}

inline fun <reified T> Transition<*>.atType(): Boolean {
    return !isRunning && currentState is T
}

fun <T> Transition<out T>.between(a: T, b: T): Boolean {
    return targetState == a && currentState == b || targetState == b && currentState == a
}

operator fun <T : Comparable<T>> Transition<out T>.compareTo(state: T): Int {
    val targetInt = targetState.compareTo(state).coerceIn(-1, 1)
    val currentInt = currentState.compareTo(state).coerceIn(-1, 1)

    if (targetInt == 0) return currentInt
    if (currentInt == 0) return targetInt
    if (targetInt != currentInt) return 0 // TODO states are on different sides of state, so consider it equal?
    return targetInt
}
