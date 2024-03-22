package dev.bnorm.kc24.elements

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalAnimationApi
@Composable
fun Transition<Boolean>.AnimatedVisibility(
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = shrinkOut() + fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) = AnimatedVisibility({ it }, modifier, enter, exit, content)
