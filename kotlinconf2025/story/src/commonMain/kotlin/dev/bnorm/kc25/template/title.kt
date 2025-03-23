package dev.bnorm.kc25.template

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.DefaultCornerKodee
import dev.bnorm.deck.shared.SharedKodee
import dev.bnorm.storyboard.core.*
import dev.bnorm.storyboard.easel.SceneSection
import dev.bnorm.storyboard.easel.enter
import dev.bnorm.storyboard.easel.exit
import dev.bnorm.storyboard.easel.section
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

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
        enterTransition = enter(
            start = if (animateFromBody) DefaultEnterTransition else SceneEnter(alignment = Alignment.CenterEnd),
            end = if (animateToBody) DefaultEnterTransition else SceneEnter(alignment = Alignment.CenterEnd),
        ),
        exitTransition = exit(
            start = if (animateFromBody) DefaultExitTransition else SceneExit(alignment = Alignment.CenterEnd),
            end = if (animateToBody) DefaultExitTransition else SceneExit(alignment = Alignment.CenterEnd),
        ),
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
fun SceneScope<Int>.SectionTitle(
    showAsBody: Transition<Boolean>,
    title: @Composable () -> Unit = SceneSection.title,
) {
    val moveDuration = 500
    val lineDuration = 500

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val textStyle = showAsBody.animateTextStyle(
            whenFalse = MaterialTheme.typography.h2,
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
                when (it) {
                    false -> maxHeight / 2 - 36.dp
                    true -> 16.dp
                }
            },
        )
        val width by showAsBody.animateDp(
            label = "header line width",
            transitionSpec = {
                when (targetState) {
                    true -> tween(lineDuration, moveDuration, EaseInOut)
                    false -> tween(lineDuration, delayMillis = 0, EaseInOut)
                }
            },
            targetValueByState = {
                when (it) {
                    false -> 0.dp
                    true -> maxWidth
                }
            },
        )

        val modifier = when {
            showAsBody.currentState -> Modifier.sharedElement(
                rememberSharedContentState(key = SharedHeaderKey),
                animatedVisibilityScope = this@SectionTitle,
            )
            else -> Modifier
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize(),
        ) {
            Spacer(Modifier.heightIn(min = height))
            ProvideTextStyle(textStyle) {
                title()
            }
            Spacer(Modifier.height(4.dp))
            Spacer(
                Modifier.requiredSize(width, 2.dp)
                    .padding(horizontal = 64.dp)
                    .background(MaterialTheme.colors.secondary)
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
