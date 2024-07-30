package dev.bnorm.kc24.elements

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.kc24.Theme

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
        val style = MaterialTheme.typography.body2.copy(
            fontFamily = Theme.JetBrainsMono,
            fontSize = 32.sp,
            lineHeight = 38.sp,
        )

        ProvideTextStyle(style) {
            Text(
                text = text,
                modifier = Modifier
                    .verticalScroll(scrollState, enabled = false)
                    .padding(32.dp)
                    .padding(bottom = 40.dp), // Offset the visible padding
            )
        }
    }
}
