package dev.bnorm.deck.shared.mac

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.JetBrainsMono

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
