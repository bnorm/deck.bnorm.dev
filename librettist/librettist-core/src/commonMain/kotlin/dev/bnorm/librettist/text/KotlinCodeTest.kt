package dev.bnorm.librettist.text

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle


@Composable
fun KotlinCodeText(
    text: String,
    modifier: Modifier = Modifier,
    identifierType: (String) -> SpanStyle? = { null },
) {
    Column(modifier) {
        for (line in text.lines()) {
            Text(text = KotlinCodeString(line, identifierType))
        }
    }
}

// TODO we need some kind of Kotlin/Multiplatform version of hightlight.js if we want to move this to common...
@Composable
expect fun KotlinCodeString(
    text: String,
    identifierType: (String) -> SpanStyle? = { null },
): AnnotatedString
