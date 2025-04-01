package dev.bnorm.kc25.template.code

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import dev.bnorm.storyboard.text.highlight.Highlighting
import dev.bnorm.storyboard.text.highlight.LocalHighlighter

@Composable
fun AnnotatedString.toCode(
    identifierType: (Highlighting, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    val highlighter = LocalHighlighter.current
    return rememberSaveable(highlighter, this) {
        val highlighting = highlighter.highlighting
        val styled = highlighter.highlight(
            text = text,
            identifierStyle = { identifierType(highlighting, it) ?: it.toStyle(highlighting) },
        )
        buildAnnotatedString {
            append(this@toCode)
            for (range in styled.spanStyles) {
                addStyle(range.item, range.start, range.end)
            }
        }
    }
}

@Composable
fun String.toCode(
    identifierType: (Highlighting, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    val highlighter = LocalHighlighter.current
    return rememberSaveable(highlighter, this) {
        val highlighting = highlighter.highlighting
        highlighter.highlight(
            text = this,
            identifierStyle = { identifierType(highlighting, it) ?: it.toStyle(highlighting) },
        )
    }
}

fun String.toStyle(codeStyle: Highlighting): SpanStyle? {
    return null
}
