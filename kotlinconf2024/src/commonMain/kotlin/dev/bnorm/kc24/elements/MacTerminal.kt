package dev.bnorm.kc24.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// TODO convert mac terminal to IntelliJ run tool window?
@Composable
fun MacTerminal(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    MacWindow(color = Color(0xFFF0F0F1), modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            content()
        }
    }
}
