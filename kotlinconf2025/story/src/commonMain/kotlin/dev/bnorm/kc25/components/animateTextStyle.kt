package dev.bnorm.kc25.components

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun Transition<TextStyle>.animateTextStyle(
    transitionSpec: @Composable Transition.Segment<TextStyle>.() -> FiniteAnimationSpec<Float>,
): TextStyle {
    val fontSize by animateFloat(transitionSpec) {
        it.fontSize.value
    }
    val lineHeight by animateFloat(transitionSpec) {
        it.lineHeight.value
    }
    val letterSpacing by animateFloat(transitionSpec) {
        it.letterSpacing.value
    }
    return TextStyle(
        fontSize = fontSize.sp,
        lineHeight = lineHeight.sp,
        letterSpacing = letterSpacing.sp,
    )
}
