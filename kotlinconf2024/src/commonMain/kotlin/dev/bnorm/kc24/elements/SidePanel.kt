package dev.bnorm.kc24.elements

import androidx.compose.animation.core.Transition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.bnorm.kc24.template.SLIDE_PADDING

@Composable
fun SidePanel(
    visible: Transition<Boolean>,
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {

    Box(modifier = modifier.fillMaxHeight()) {
        visible.AnimatedVisibility(
            enter = slideInHorizontally(defaultSpec()) { it },
            exit = slideOutHorizontally(defaultSpec()) { it },
        ) {
            MacWindow(
                color = Color(0xFF313438),
                title = title,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface)
                        .padding(SLIDE_PADDING)
                        .wrapContentWidth(Alignment.Start, unbounded = true)
                ) {
                    content()
                }
            }
        }
    }
}

