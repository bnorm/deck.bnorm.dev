package dev.bnorm.kc25.components

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun <S> Transition<S>.animateTextStyle(
    transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Float> = { spring() },
    label: String = "TextStyleAnimation",
    targetValueByState: @Composable (state: S) -> TextStyle,
): State<TextStyle> {
    val fontSize = animateFloat(transitionSpec, "$label:fontSize") {
        targetValueByState(it).fontSize.value
    }
    val lineHeight = animateFloat(transitionSpec, "$label:lineHeight") {
        targetValueByState(it).lineHeight.value
    }
    val letterSpacing = animateFloat(transitionSpec, "$label:letterSpacing") {
        targetValueByState(it).letterSpacing.value
    }
    return derivedStateOf {
        TextStyle(
            fontSize = fontSize.value.sp,
            lineHeight = lineHeight.value.sp,
            letterSpacing = letterSpacing.value.sp,
        )
    }
}
