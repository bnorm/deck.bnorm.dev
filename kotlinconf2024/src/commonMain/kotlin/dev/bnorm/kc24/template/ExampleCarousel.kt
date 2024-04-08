package dev.bnorm.kc24.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.kc24.elements.defaultSpec
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideState
import kotlin.time.Duration.Companion.milliseconds

fun ShowBuilder.ExampleCarousel(
    forward: Boolean = true,
    values: @Composable () -> Pair<AnnotatedString, AnnotatedString>,
) {
    ExampleCarousel(
        forward = forward,
        start = {
            Example(values().first)
        },
        end = {
            Example(values().second)
        }
    )
}

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
