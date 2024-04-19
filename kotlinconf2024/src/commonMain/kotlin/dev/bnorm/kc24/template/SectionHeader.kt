@file:OptIn(ExperimentalAnimationApi::class, ExperimentalTransitionApi::class)

package dev.bnorm.kc24.template

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
import dev.bnorm.kc24.elements.AnimatedVisibility
import dev.bnorm.kc24.elements.defaultSpec
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideSection
import dev.bnorm.librettist.show.SlideState

fun ShowBuilder.SectionHeader(
    animateFromBody: Boolean = false,
    animateToBody: Boolean = false,
    title: (@Composable () -> Unit)? = null,
) {
    slide {
        SectionHeader(
            showAsBody = transition.createChildTransition {
                when (it) {
                    SlideState.Entering -> animateFromBody
                    SlideState.Exiting -> animateToBody
                    is SlideState.Index -> false
                }
            },
            title = title ?: SlideSection.header,
        )
    }
}

@Composable
fun SectionHeader(
    showAsBody: Transition<Boolean>,
    title: @Composable () -> Unit = SlideSection.header,
) {
    val spacing by showAsBody.animateDp(transitionSpec = { defaultSpec() }) {
        when (it) {
            false -> 440.dp
            true -> 0.dp
        }
    }
    val textStyle = showAsBody.animateTextStyle(
        whenFalse = MaterialTheme.typography.h2,
        whenTrue = MaterialTheme.typography.h3,
        transitionSpec = { defaultSpec() },
    )

    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.requiredHeight(spacing))
        Box(Modifier.fillMaxWidth().padding(horizontal = SLIDE_PADDING, vertical = SLIDE_CONTENT_SPACING)) {
            ProvideTextStyle(textStyle) {
                title()
            }
        }
        Spacer(Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
        Box(Modifier.fillMaxWidth().offset(y = (-294).dp), contentAlignment = Alignment.TopEnd) {
            showAsBody.AnimatedVisibility(
                visible = { !it },
                enter = fadeIn(defaultSpec()) + slideInHorizontally(defaultSpec()) { it },
                exit = slideOutHorizontally(defaultSpec()) { it } + fadeOut(defaultSpec()),
                modifier = Modifier.wrapContentHeight(align = Alignment.Top, unbounded = true)
            ) {
                KodeeSitting(Modifier.requiredSize(516.dp))
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        showAsBody.AnimatedVisibility(
            enter = fadeIn(defaultSpec()) + slideInHorizontally(defaultSpec()) { it },
            exit = slideOutHorizontally(defaultSpec()) { it } + fadeOut(defaultSpec()),
            modifier = Modifier.align(Alignment.BottomEnd),
        ) {
            Box(Modifier.padding(8.dp)) {
                DefaultCornerKodee()
            }
        }
    }
}

@Composable
private fun Transition<Boolean>.animateTextStyle(
    whenFalse: TextStyle,
    whenTrue: TextStyle,
    transitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = { spring() },
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
