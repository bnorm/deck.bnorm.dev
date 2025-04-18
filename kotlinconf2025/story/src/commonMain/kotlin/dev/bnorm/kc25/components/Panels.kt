package dev.bnorm.kc25.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun RightPanel(
    show: Transition<Boolean>,
    modifier: Modifier = Modifier,
    enterSpec: FiniteAnimationSpec<IntOffset> = tween(500, easing = EaseIn),
    exitSpec: FiniteAnimationSpec<IntOffset> = tween(500, easing = EaseOut),
    content: @Composable () -> Unit,
) {
    // TODO try again to get haze to work?
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
        show.AnimatedVisibility(
            visible = { it },
            enter = slideInHorizontally(enterSpec) { it },
            exit = slideOutHorizontally(exitSpec) { it },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .offset(x = 16.dp)
                    .border(2.dp, MaterialTheme.colors.secondary, RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.surface.copy(alpha = 0.9f))
                    .padding(end = 16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun BottomPanel(
    show: Transition<Boolean>,
    modifier: Modifier = Modifier,
    enterSpec: FiniteAnimationSpec<IntOffset> = tween(500, easing = EaseIn),
    exitSpec: FiniteAnimationSpec<IntOffset> = tween(500, easing = EaseOut),
    content: @Composable () -> Unit,
) {
    // TODO try again to get haze to work?
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
        show.AnimatedVisibility(
            visible = { it },
            enter = slideInVertically(enterSpec) { it },
            exit = slideOutVertically(exitSpec) { it },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 16.dp)
                    .border(2.dp, MaterialTheme.colors.secondary, RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.surface.copy(alpha = 0.9f))
                    .padding(bottom = 16.dp)
            ) {
                content()
            }
        }
    }
}
