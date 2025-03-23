package dev.bnorm.deck.shared.mac

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.core.SceneScope
import dev.bnorm.storyboard.core.Frame

@Composable
fun MacTerminal(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    content: @Composable () -> Unit,
) {
    MacWindow(
        color = Color(0xFFF0F0F1), modifier = modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
    ) {
        ProvideTextStyle(LocalTextStyle.current.copy(fontFamily = JetBrainsMono, color = Color.White)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .verticalScroll(scrollState, enabled = false)
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun <T> SceneScope<T>.MacTerminalPopup(
    visible: (Frame<T>) -> Boolean,
    content: @Composable () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
        frame.AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
        ) {
            MacTerminal(modifier = Modifier.fillMaxWidth().offset(y = 16.dp)) {
                content()
            }
        }
    }
}
