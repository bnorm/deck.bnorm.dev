package dev.bnorm.evolved.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneSection

private object SharedHeaderKey

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun Header(
    textStyle: TextStyle = MaterialTheme.typography.h3,
    title: @Composable () -> Unit = SceneSection.title,
) {
    Column(
        modifier = Modifier.sharedElement(rememberSharedContentState(SharedHeaderKey))
    ) {
        Box(Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 16.dp)) {
            ProvideTextStyle(textStyle) {
                title()
            }
        }
        Spacer(Modifier.fillMaxWidth().requiredHeight(2.dp).background(MaterialTheme.colors.primary))
    }
}
