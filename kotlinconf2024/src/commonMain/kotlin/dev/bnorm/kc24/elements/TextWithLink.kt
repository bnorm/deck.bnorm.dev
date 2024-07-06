package dev.bnorm.kc24.elements

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextDecoration

fun AnnotatedString.Builder.appendLink(text: String, url: String = text) {
    val start = length
    append(text)
    val end = length

    addLink(
        url = LinkAnnotation.Url(
            url = url,
            styles = TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline))
        ),
        start = start,
        end = end,
    )
}
