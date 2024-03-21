package dev.bnorm.kc24.template

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
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
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideSection

fun ShowBuilder.SectionHeader(
    animateToBody: Boolean = true,
    title: (@Composable () -> Unit)? = null,
) {
    if (animateToBody) {
        slide(advancements = 2) {
            val title = title ?: SlideSection.header
            SectionHeaderImpl(showAsBody = advancement == 1, title)
        }
    } else {
        slide {
            val title = title ?: SlideSection.header
            SectionHeaderImpl(showAsBody = false, title)
        }
    }
}

@Composable
private fun SectionHeaderImpl(
    showAsBody: Boolean,
    title: @Composable () -> Unit,
) {
    val transition = updateTransition(showAsBody, label = "slide type")

    val spacing by transition.animateDp {
        when (it) {
            false -> 450.dp
            true -> 32.dp
        }
    }
    val textStyle = transition.animateTextStyle(MaterialTheme.typography.h2, MaterialTheme.typography.h3)

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
            AnimatedVisibility(
                visible = !showAsBody,
                enter = fadeIn() + slideInHorizontally { it },
                exit = slideOutHorizontally { it } + fadeOut(),
            ) {
                KodeeSitting(Modifier.requiredSize(516.dp))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(showAsBody, enter = slideInVertically { it }, exit = slideOutVertically { it }) {
            Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
        }
    }
    AnimatedVisibility(
        showAsBody,
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
