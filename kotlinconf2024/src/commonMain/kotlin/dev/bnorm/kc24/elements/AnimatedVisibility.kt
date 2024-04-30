package dev.bnorm.kc24.elements

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalAnimationApi
@Composable
fun Transition<out Boolean>.AnimatedVisibility(
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn(defaultSpec()) + expandIn(defaultSpec()),
    exit: ExitTransition = shrinkOut(defaultSpec()) + fadeOut(defaultSpec()),
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) = AnimatedVisibility({ it }, modifier, enter, exit, content)
