package dev.bnorm.kc25.template

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.DefaultCornerKodee
import dev.bnorm.deck.shared.SharedKodee
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.SceneSection
import dev.bnorm.storyboard.easel.template.section

fun StoryboardBuilder.SectionAndTitle(
    header: String,
    block: StoryboardBuilder.() -> Unit,
) {
    section(header) {
        SectionTitle(animateToBody = true)
        block()
        SectionTitle(animateFromBody = true)
    }
}

fun StoryboardBuilder.SectionTitle(
    animateFromBody: Boolean = false,
    animateToBody: Boolean = false,
    title: (@Composable () -> Unit)? = null,
) {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SectionTitle(
            showAsBody = frame.createChildTransition {
                when (it) {
                    Frame.Start -> animateFromBody
                    Frame.End -> animateToBody
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
    showAsBody: Transition<Boolean>,
    title: @Composable () -> Unit = SceneSection.title,
) {
    val moveDuration = 300
    val lineDuration = 300

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val textStyle = showAsBody.animateTextStyle(
            whenFalse = MaterialTheme.typography.h1,
            whenTrue = MaterialTheme.typography.h3,
            transitionSpec = {
                when (targetState) {
                    true -> tween(moveDuration, delayMillis = 0, EaseInOut)
                    false -> tween(moveDuration, lineDuration, EaseInOut)
                }
            },
        )
        val height by showAsBody.animateDp(
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

        val lineFraction by showAsBody.animateFloat(
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

    SharedKodee {
        showAsBody.AnimatedVisibility(
            visible = { it },
            enter = fadeIn(tween(lineDuration, moveDuration, EaseInOut)) +
                    slideInHorizontally(tween(lineDuration, moveDuration, EaseInOut)) { it },
            exit = fadeOut(tween(lineDuration, delayMillis = 0, EaseInOut)) +
                    slideOutHorizontally(tween(lineDuration, delayMillis = 0, EaseInOut)) { it },
        ) {
            DefaultCornerKodee(Modifier.size(50.dp))
        }
    }
}

@Composable
private fun Transition<Boolean>.animateTextStyle(
    whenFalse: TextStyle,
    whenTrue: TextStyle,
    transitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float>,
): TextStyle {
    val fontSize by animateFloat(transitionSpec) {
        when (it) {
            false -> whenFalse.fontSize.value
            true -> whenTrue.fontSize.value
        }
    }
    val lineHeight by animateFloat(transitionSpec) {
        when (it) {
            false -> whenFalse.lineHeight.value
            true -> whenTrue.lineHeight.value
        }
    }
    val letterSpacing by animateFloat(transitionSpec) {
        when (it) {
            false -> whenFalse.letterSpacing.value
            true -> whenTrue.letterSpacing.value
        }
    }
    return whenTrue.copy(
        fontSize = fontSize.sp,
        lineHeight = lineHeight.sp,
        letterSpacing = letterSpacing.sp,
        textMotion = TextMotion.Animated,
    )
}
