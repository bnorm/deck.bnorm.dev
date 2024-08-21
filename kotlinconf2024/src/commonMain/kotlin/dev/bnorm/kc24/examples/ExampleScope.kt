package dev.bnorm.kc24.examples

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable
import dev.bnorm.storyboard.core.SlideScope

@Stable
interface ExampleScope {
    val slideScope: SlideScope<*>
    val transition: Transition<out ExampleState>
}
