package dev.bnorm.evolved.template

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.DefaultCornerKodee
import dev.bnorm.deck.shared.KodeeSitting
import dev.bnorm.deck.shared.SharedKodee
import dev.bnorm.storyboard.core.Frame
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.SceneSection
import dev.bnorm.storyboard.easel.section

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
    scene(stateCount = 1) {
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
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val height by showAsBody.animateDp(transitionSpec = { defaultSpec() }) {
            when (it) {
                false -> maxHeight / 3
                true -> 0.dp
            }
        }
        Column {
            Box(Modifier.heightIn(min = height))
            TitleWithKodee(showAsHeader = showAsBody) {
                title()
            }
        }
    }

    SharedKodee {
        showAsBody.AnimatedVisibility(
            visible = { it },
            enter = fadeIn(defaultSpec()) + slideInHorizontally(defaultSpec()) { it },
            exit = fadeOut(defaultSpec()) + slideOutHorizontally(defaultSpec()) { it },
        ) {
            DefaultCornerKodee(Modifier.size(50.dp))
        }
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun TitleWithKodee(
    showAsHeader: Transition<Boolean>,
    title: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier.fillMaxWidth()
    ) {
        val textStyle = showAsHeader.animateTextStyle(
            whenFalse = MaterialTheme.typography.h2,
            whenTrue = MaterialTheme.typography.h3,
            transitionSpec = { defaultSpec() },
        )

        Header(textStyle, title)

        Box(
            modifier = Modifier
                .requiredHeight(0.dp)
                .wrapContentHeight(align = Alignment.Bottom, unbounded = true)
                .align(Alignment.BottomEnd),
            contentAlignment = Alignment.BottomEnd
        ) {
            showAsHeader.AnimatedVisibility(
                visible = { !it },
                enter = fadeIn(defaultSpec()) + slideInHorizontally(defaultSpec()) { it },
                exit = fadeOut(defaultSpec()) + slideOutHorizontally(defaultSpec()) { it },
                modifier = Modifier.offset(y = 111.dp)
            ) {
                KodeeSitting(Modifier.requiredSize(258.dp))
            }
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
