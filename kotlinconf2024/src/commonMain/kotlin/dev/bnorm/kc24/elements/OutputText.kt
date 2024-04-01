package dev.bnorm.kc24.elements

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.template.ExampleState
import dev.bnorm.librettist.animation.animateList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

enum class OutputState {
    Hidden,
    Visible,
    Minimized,
}

@Composable
fun OutputText(
    text: String,
    state: Transition<out OutputState>,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    val outputOffset by state.animateDp(transitionSpec = { defaultSpec() }) {
        when (it) {
            OutputState.Hidden -> 700.dp
            OutputState.Visible -> 40.dp
            OutputState.Minimized -> 420.dp
        }
    }

    MacTerminal(modifier = modifier.requiredHeight(700.dp).fillMaxWidth().offset(y = outputOffset)) {
        ProvideTextStyle(MaterialTheme.typography.body2) {
            Text(
                text = text,
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(32.dp)
                    .padding(bottom = 40.dp) // Offset the visible padding
                    // TODO should everything NOT wrap? is this a con for some examples?
                    .wrapContentSize(Alignment.TopStart, unbounded = true),
            )
        }
    }
}

@Composable
fun Transition<out ExampleState>.outputTextDiff(
    values: ImmutableList<String>,
    transitionSpec: @Composable Transition.Segment<Int>.() -> FiniteAnimationSpec<Int> = {
        typingSpec(count = values.size - 1, charDelay = 100.milliseconds)
    },
    targetIndexByState: @Composable (state: Int) -> Int = {
        if (it > 0) values.lastIndex else 0
    },
): State<String> {
    val state = createChildTransition { it.outputIndex }
    return state.animateList(
        values = values,
        transitionSpec = transitionSpec,
        targetIndexByState = targetIndexByState
    )
}

@Composable
fun Transition<out ExampleState>.outputTextDiff(
    sequence: ImmutableList<ImmutableList<String>>,
): State<String> {
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
    return outputTextDiff(
        values = values,
        transitionSpec = {
            typingSpec(
                count = abs(mapping.getValue(targetState) - mapping.getValue(initialState)),
                charDelay = 100.milliseconds
            )
        },
        targetIndexByState = { mapping.getValue(it) },
    )
}
