package dev.bnorm.kc25.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.animateTextStyle
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.SceneSection

fun StoryboardBuilder.SectionTitle(
    animateFromHeader: Boolean = false,
    animateToHeader: Boolean = false,
    title: (@Composable () -> Unit)? = null,
) {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SectionTitle(
            showAsHeader = transition.createChildTransition {
                when (it) {
                    Frame.Start -> animateFromHeader
                    Frame.End -> animateToHeader
                    is Frame.State -> false
                }
            },
            title = title ?: SceneSection.title,
        )
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun SectionTitle(
    showAsHeader: Transition<Boolean>,
    title: @Composable () -> Unit = SceneSection.title,
) {
    val titleTextStyle = MaterialTheme.typography.h1
    val headerTextStyle = MaterialTheme.typography.h3

    val moveDuration = 300
    val lineDuration = 300

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val textStyle by showAsHeader.animateTextStyle(
                transitionSpec = {
                    when (targetState) {
                        true -> tween(moveDuration, delayMillis = 0, EaseInOut)
                        false -> tween(moveDuration, lineDuration, EaseInOut)
                    }
                },
            targetValueByState = { if (it) headerTextStyle else titleTextStyle }
        )
        val height by showAsHeader.animateDp(
            label = "header top padding",
            transitionSpec = {
                when (targetState) {
                    true -> tween(moveDuration, delayMillis = 0, EaseInOut)
                    false -> tween(moveDuration, lineDuration, EaseInOut)
                }
            },
            targetValueByState = {
                val textHeight = with(LocalDensity.current) { MaterialTheme.typography.h1.lineHeight.toDp() }
                when (it) {
                    false -> (maxHeight - textHeight) / 2 - 16.dp
                    true -> 0.dp
                }
            },
        )

        val lineFraction by showAsHeader.animateFloat(
            label = "header line width",
            transitionSpec = {
                when (targetState) {
                    true -> tween(lineDuration, moveDuration, EaseInOut)
                    false -> tween(lineDuration, delayMillis = 0, EaseInOut)
                }
            },
            targetValueByState = { if (it) 1f else 0f },
        )

        ProvideTextStyle(textStyle) {
            Header(
                modifier = Modifier
                    .sharedElement(rememberSharedContentState(key = SceneSection.current))
                    .fillMaxSize()
                    .padding(top = height),
                lineFraction = lineFraction,
                title = title,
            )
        }
    }
}
