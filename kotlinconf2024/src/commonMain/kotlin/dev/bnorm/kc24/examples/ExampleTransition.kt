package dev.bnorm.kc24.examples

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.kc24.elements.typingSpec
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideState
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.abs

fun ShowBuilder.ExampleTransition(
    transitionSpec: @Composable Transition.Segment<Int>.() -> FiniteAnimationSpec<Int> = {
        typingSpec(count = abs(targetState - initialState))
    },
    strings: @Composable () -> ImmutableList<AnnotatedString>,
) {
    slide(states = 0) {
        val values = strings()
        val state = transition.createChildTransition { if (it == SlideState.Exiting) values.lastIndex else 0 }
        val text by state.animateList(values = values, transitionSpec = transitionSpec) { it }

        TitleAndBody {
            Example(text)
        }
    }
}
