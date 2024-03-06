package dev.bnorm.librettist

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.SpanStyle

@Composable
fun SlideTheme(theme: SlideTheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSlideTheme provides theme) {
        MaterialTheme(colors = theme.colors, typography = theme.typography) {
            content()
        }
    }
}

val LocalSlideTheme = staticCompositionLocalOf<SlideTheme> {
    error("Theme is not provided")
}

@Immutable
data class SlideTheme(
    val colors: Colors,
    val typography: Typography,
    val code: CodeStyle
) {
    @Immutable
    data class CodeStyle(
        val simple: SpanStyle,
        val number: SpanStyle,
        val keyword: SpanStyle,
        val punctuation: SpanStyle,
        val annotation: SpanStyle,
        val comment: SpanStyle,
        val string: SpanStyle,
    )
}
