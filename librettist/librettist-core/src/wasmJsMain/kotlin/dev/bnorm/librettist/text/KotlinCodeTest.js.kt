package dev.bnorm.librettist.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.bnorm.librettist.LocalShowTheme

// TODO we need some kind of Kotlin/Multiplatform version of hightlight.js if we want to move this to common...
@Composable
actual fun KotlinCodeString(
    text: String,
    identifierType: (String) -> SpanStyle?
): AnnotatedString = buildAnnotatedString {
    withStyle(LocalShowTheme.current.code.simple) {
        append(text)
    }
}
