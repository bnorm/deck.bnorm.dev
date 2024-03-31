package dev.bnorm.kc24.elements

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.jvm.JvmName

enum class OutputState {
    Hidden,
    Visible,
    Minimized,
}

@JvmName("OutputTextBoolean")
@Composable
fun OutputText(text: String, visible: Transition<Boolean>, modifier: Modifier = Modifier) {
    val state = visible.createChildTransition { if (it) OutputState.Visible else OutputState.Hidden }
    OutputText(
        text = text,
        state = state,
        modifier = modifier
    )
}

@Composable
fun OutputText(
    text: String,
    state: Transition<OutputState>,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    val outputOffset by state.animateDp(transitionSpec = { defaultSpec() }) {
        when (it) {
            OutputState.Hidden -> 600.dp
            OutputState.Visible -> 40.dp
            OutputState.Minimized -> 320.dp
        }
    }

    MacTerminal(modifier = modifier.requiredHeight(600.dp).fillMaxWidth().offset(y = outputOffset)) {
        ProvideTextStyle(MaterialTheme.typography.body2) {
            Text(
                text = text,
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(32.dp)
                    .padding(bottom = 40.dp) // Offset the visible padding
                    .wrapContentSize(Alignment.TopStart, unbounded = true),
            )
        }
    }
}