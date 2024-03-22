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
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideSection

fun ShowBuilder.SectionHeader(
    animateToBody: Boolean = true,
    title: (@Composable () -> Unit)? = null,
) {
    if (animateToBody) {
        slide(advancements = 2) {
            SectionHeaderImpl(
                showAsBody = transition.createChildTransition { it == 1 },
                title = title ?: SlideSection.header,
            )
        }
    } else {
        slide {
            SectionHeaderImpl(
                showAsBody = transition.createChildTransition { false },
                title = title ?: SlideSection.header,
            )
        }
    }
}

@Composable
private fun SectionHeaderImpl(
    showAsBody: Transition<Boolean>,
    title: @Composable () -> Unit,
) {
    val spacing by showAsBody.animateDp {
        when (it) {
            false -> 450.dp
            true -> 32.dp
        }
    }
    val textStyle = showAsBody.animateTextStyle(MaterialTheme.typography.h2, MaterialTheme.typography.h3)

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(top = spacing, start = SLIDE_PADDING, bottom = 32.dp)
                .requiredHeight(130.dp)
                .animateContentSize(),
            contentAlignment = Alignment.BottomStart,
        ) {
            ProvideTextStyle(textStyle) {
                title()
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
        Row(modifier = Modifier.offset(y = (-268).dp), horizontalArrangement = Arrangement.End) {
            Spacer(Modifier.weight(1f))
            showAsBody.AnimatedVisibility(
                visible = { !it },
                enter = fadeIn() + slideInHorizontally { it },
                exit = slideOutHorizontally { it } + fadeOut(),
            ) {
                KodeeSitting(Modifier.requiredSize(516.dp))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        showAsBody.AnimatedVisibility(enter = slideInVertically { it }, exit = slideOutVertically { it }) {
            Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
        }
    }
    showAsBody.AnimatedVisibility(
        enter = fadeIn() + slideInHorizontally { it },
        exit = slideOutHorizontally { it } + fadeOut(),
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.BottomEnd) {
            DefaultCornerKodee()
        }
    }
}

@Composable
private fun Transition<Boolean>.animateTextStyle(
    whenFalse: TextStyle,
    whenTrue: TextStyle,
): TextStyle {
    val fontSize by animateFloat {
        when (it) {
            false -> whenFalse.fontSize.value
            true -> whenTrue.fontSize.value
        }
    }
    val lineHeight by animateFloat {
        when (it) {
            false -> whenFalse.lineHeight.value
            true -> whenTrue.lineHeight.value
        }
    }
    val letterSpacing by animateFloat {
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
