package dev.bnorm.kc25.template.code

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.highlight.CodeStyle
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight

fun AnnotatedString.toCode(
    codeStyle: CodeStyle,
    scope: CodeScope = CodeScope.File,
    identifierType: (CodeStyle, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    val styled = text.highlight(
        codeStyle = codeStyle,
        language = Language.Kotlin,
        scope = scope,
        identifierStyle = { identifierType(codeStyle, it) ?: it.toStyle(codeStyle) }
    )
    return buildAnnotatedString {
        append(this@toCode)
        for (range in styled.spanStyles) {
            addStyle(range.item, range.start, range.end)
        }
    }
}

fun String.toCode(
    codeStyle: CodeStyle,
    scope: CodeScope = CodeScope.File,
    identifierType: (CodeStyle, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    return highlight(
        codeStyle = codeStyle,
        language = Language.Kotlin,
        scope = scope,
        identifierStyle = { identifierType(codeStyle, it) ?: it.toStyle(codeStyle) },
    )
}

fun String.toStyle(codeStyle: CodeStyle): SpanStyle? {
    return null
}
