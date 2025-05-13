package dev.bnorm.deck.kc25.companion.utils

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun Link(text: String, url: String) {
    Text(buildAnnotatedString {
        withLink(LinkAnnotation.Url(url, styles)) {
            append(text)
        }
    })
}

fun AnnotatedLink(text: String, link: LinkAnnotation.Url): AnnotatedString {
    return buildAnnotatedString {
        withLink(link) { append(text) }
    }
}

val styles = TextLinkStyles(
    style = SpanStyle(color = Color.Blue),
    hoveredStyle = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
    pressedStyle = SpanStyle(color = Color(0xFF8400FF), textDecoration = TextDecoration.Underline),
)
