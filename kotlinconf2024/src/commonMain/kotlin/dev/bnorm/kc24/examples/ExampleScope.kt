package dev.bnorm.kc24.examples

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable

@Stable
interface ExampleScope {
    val transition: Transition<out ExampleState>
}
