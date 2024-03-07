package dev.bnorm.kc24.template

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.kc24.PreviewSlide
import dev.bnorm.kc24.image.Kodee
import dev.bnorm.kc24.image.kodee.Petite
import dev.bnorm.kc24.image.kodee.Sitting
import dev.bnorm.librettist.section.LocalSlideSection
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.rememberAdvancementBoolean

@Composable
fun SlideScope.SectionHeader(
    animateToBody: Boolean = true,
    title: @Composable () -> Unit = LocalSlideSection.current.header,
) {
    val state by if (animateToBody) rememberAdvancementBoolean() else mutableStateOf(false)
    val spacing by animateDpAsState(
        targetValue = when (state) {
            false -> 225.dp
            true -> 0.dp
        }
    )
    val fontSize by animateFloatAsState(
        targetValue = when (state) {
            false -> MaterialTheme.typography.h2.fontSize.value
            true -> MaterialTheme.typography.h3.fontSize.value
        }
    )

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
        Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(2.dp).background(Color(0xFF7F52FF)))
        Column(modifier = Modifier.offset(742.dp, (-146).dp)) {
            AnimatedVisibility(visible = !state, enter = fadeIn(), exit = fadeOut()) {
                Image(
                    imageVector = Kodee.Sitting,
                    contentDescription = "",
                    modifier = Modifier.requiredSize(258.dp),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(state, enter = slideInVertically { it }, exit = slideOutVertically { it }) {
            Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(2.dp).background(Color(0xFF7F52FF)))
        }
    }
    AnimatedVisibility(state, enter = slideInVertically { 3 * it / 2 }, exit = slideOutVertically { 3 * it / 2 }) {
        Box(modifier = Modifier.absoluteOffset(920.dp, 494.dp).requiredSize(73.dp, 63.dp)) {
            // TODO make image customizable so Kodee can react to what is on the slide?
            Image(imageVector = Kodee.Petite, contentDescription = "", modifier = Modifier.fillMaxSize())
        }
    }
}
