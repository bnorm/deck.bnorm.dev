package dev.bnorm.kc24.examples

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable
import dev.bnorm.storyboard.core.SceneScope

@Stable
interface ExampleScope {
    val sceneScope: SceneScope<*>
    val transition: Transition<out ExampleState>
}
