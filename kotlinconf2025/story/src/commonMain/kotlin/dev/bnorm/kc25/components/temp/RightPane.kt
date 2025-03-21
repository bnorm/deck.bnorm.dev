package dev.bnorm.kc25.components.temp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RightPane(
    visible: Transition<out Boolean>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    visible.AnimatedVisibility(
        visible = { it },
        modifier = modifier.fillMaxHeight().requiredWidth(760.dp).offset(x = 10.dp),
        enter = slideInHorizontally { it },
        exit = slideOutHorizontally { it },
    ) {
        content()
    }
}
