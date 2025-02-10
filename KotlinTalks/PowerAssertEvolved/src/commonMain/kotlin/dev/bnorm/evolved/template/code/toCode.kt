package dev.bnorm.evolved.template.code

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.storyboard.text.highlight.Highlighting
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight

@Composable
fun String.toCode(
    identifierType: (Highlighting, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    val highlighting = Highlighting.Companion.current
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
    return when (this) {
        "days",
        "duration",
        "size",
        "length",
        "values",
        "explanation",
        "offset",
        "source",
        "dispatchReceiver",
        "contextArguments",
        "extensionReceiver",
        "valueArguments",
        "startOffset",
        "endOffset",
        "displayOffset",
        "value",
        "lhs",
        "rhs",
        "expressions",
        "isImplicit",
            -> codeStyle.property

        "NEWBIE",
        "FUNCTION",
            -> codeStyle.staticProperty

        "main",
        "powerAssert",
        "powerAssert_Explained",
        "fillInStackTrace",
            -> codeStyle.functionDeclaration

        "trimIndent",
        "map",
        "toDefaultMessage",
            -> codeStyle.extensionFunctionCall

        "require",
        "buildList",
            -> codeStyle.staticFunctionCall

        "ExplainCall",
            -> codeStyle.annotation

        else -> null
    }
}
