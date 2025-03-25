package dev.bnorm.kc25.template.code

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import dev.bnorm.storyboard.text.highlight.Highlighting
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight

@Composable
fun AnnotatedString.toCode(
    identifierType: (Highlighting, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    val highlighting = Highlighting.current
    return rememberSaveable(highlighting, this) {
        val styled = text.highlight(
            highlighting = highlighting,
            language = Language.Kotlin,
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
    val highlighting = Highlighting.current
    return rememberSaveable(highlighting, this) {
        highlight(
            highlighting = highlighting,
            language = Language.Kotlin,
            identifierStyle = { identifierType(highlighting, it) ?: it.toStyle(highlighting) },
        )
    }
}

@Composable
fun String.toCodeTokens(
    identifierType: (Highlighting, String) -> SpanStyle? = { _, _ -> null },
): List<Token?> {
    val tags = tagRegex.findAll(this).toList()
    val trimmed = tags.asReversed()
        .fold(this) { acc, match -> acc.substring(0, match.range.first) + acc.substring(match.range.last + 1) }
    val code = trimmed.toCode(identifierType)
    return tags.fold(code) { acc, match ->
        acc.subSequence(0, match.range.first) +
                AnnotatedString(match.value) +
                acc.subSequence(match.range.first, acc.length)
    }.toTokens()
}

fun String.toStyle(codeStyle: Highlighting): SpanStyle? {
    return null
}
