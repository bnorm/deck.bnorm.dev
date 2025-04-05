package dev.bnorm.kc25.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneSection

@Composable
fun Header(
    modifier: Modifier = Modifier,
    lineFraction: Float = 1f,
    title: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(Modifier.height(16.dp))
        title()
        Spacer(Modifier.height(4.dp))
        Spacer(
            Modifier
                .fillMaxWidth(lineFraction).requiredHeight(2.dp)
                .padding(horizontal = 64.dp)
                .background(MaterialTheme.colors.primary)
        )
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun Header(modifier: Modifier = Modifier) {
    val section = SceneSection.current
    ProvideTextStyle(MaterialTheme.typography.h3) {
        Header(
            modifier = modifier.sharedElement(
                rememberSharedContentState(key = section)
            ),
        ) {
            section.title()
        }
    }
}
