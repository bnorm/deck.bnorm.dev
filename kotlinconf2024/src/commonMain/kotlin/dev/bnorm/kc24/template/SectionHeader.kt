package dev.bnorm.kc24.template

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.librettist.section.LocalSlideSection
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.rememberAdvancementBoolean

@Composable
fun SlideScope.SectionHeader(
    animateToBody: Boolean = true,
    title: @Composable () -> Unit = LocalSlideSection.current.header,
) {
    val showAsBody by if (animateToBody) rememberAdvancementBoolean() else mutableStateOf(false)
    val transition = updateTransition(showAsBody, label = "slide type")

    val spacing by transition.animateDp {
        when (it) {
            false -> 225.dp
            true -> 0.dp
        }
    }
    val fontSize by transition.animateFloat {
        when (it) {
            false -> MaterialTheme.typography.h2.fontSize.value
            true -> MaterialTheme.typography.h3.fontSize.value
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(top = spacing, start = 32.dp, bottom = 8.dp)
                .requiredHeight(72.dp)
                .animateContentSize(),
            contentAlignment = Alignment.BottomStart,
        ) {
            val textStyle = MaterialTheme.typography.h3.copy(
                fontSize = fontSize.sp,
                textMotion = TextMotion.Animated,
            )
            ProvideTextStyle(textStyle) {
                title()
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(2.dp).background(MaterialTheme.colors.primary))
        Column(modifier = Modifier.offset(742.dp, (-146).dp)) {
            AnimatedVisibility(
                visible = !showAsBody,
                enter = fadeIn() + slideInHorizontally { it },
                exit = slideOutHorizontally { it } + fadeOut(),
            ) {
                KodeeSitting(Modifier.requiredSize(258.dp))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(showAsBody, enter = slideInVertically { it }, exit = slideOutVertically { it }) {
            Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(2.dp).background(MaterialTheme.colors.primary))
        }
    }
    AnimatedVisibility(
        showAsBody,
        enter = fadeIn() + slideInHorizontally { it },
        exit = slideOutHorizontally { it } + fadeOut(),
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.BottomEnd) {
            DefaultCornerKodee()
        }
    }
}
