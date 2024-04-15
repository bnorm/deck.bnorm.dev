package dev.bnorm.kc24.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import dev.bnorm.kc24.elements.defaultSpec
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideState
import kotlin.time.Duration.Companion.milliseconds

fun ShowBuilder.ExampleCarousel(
    forward: Boolean = true,
    start: @Composable () -> Unit,
    end: @Composable () -> Unit,
) {
    slide(states = 0) {
        TitleAndBody {
            transition.AnimatedVisibility(
                visible = { it == SlideState.Entering },
                enter = slideInHorizontally(defaultSpec(750.milliseconds)) { if (forward) -it else it },
                exit = slideOutHorizontally(defaultSpec(750.milliseconds)) { if (forward) -it else it },
            ) {
                start()
            }

            transition.AnimatedVisibility(
                visible = { it == SlideState.Exiting },
                enter = slideInHorizontally(defaultSpec(750.milliseconds)) { if (forward) it else -it },
                exit = slideOutHorizontally(defaultSpec(750.milliseconds)) { if (forward) it else -it },
            ) {
                end()
            }
        }
    }
}
