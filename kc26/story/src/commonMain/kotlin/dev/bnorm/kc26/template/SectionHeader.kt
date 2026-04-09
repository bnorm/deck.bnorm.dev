package dev.bnorm.kc26.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun SectionSceneScaffold(
    modifier: Modifier = Modifier,
    header: @Composable BoxScope.() -> Unit = {
        Box(
            modifier = Modifier
                .sharedElement(rememberSharedContentState(key = SceneTitle))
        ) {
            SectionHeader {
                GradientText(SceneTitle.current?.title ?: "")
            }
        }
    },
    body: @Composable BoxScope.(PaddingValues) -> Unit,
) {
    SceneScaffold(
        header = header,
        modifier = modifier,
        body = body,
    )
}

@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    lineFraction: Float = 1f,
    content: @Composable RowScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(Modifier.height(16.dp))
        ProvideTextStyle(MaterialTheme.typography.header) {
            Row(Modifier.padding(horizontal = 32.dp)) { content() }
        }
        Spacer(Modifier.height(4.dp))
        Spacer(
            Modifier
                .fillMaxWidth(lineFraction).requiredHeight(2.dp)
                .gradientBackground()
        )
    }
}
