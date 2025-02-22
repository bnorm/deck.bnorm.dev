package dev.bnorm.kc24.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.storyboard.text.highlight.Highlighting
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.highlight.rememberHighlighted

fun String.toExampleStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "fellowshipOfTheRing", "size", "name", "age", "race", "alive" -> codeStyle.property
        "`test members of the fellowship`", "addToFellowship" -> codeStyle.functionDeclaration
        "hasSize", "find", "any" -> codeStyle.extensionFunctionCall
        "assertTrue", "assertEquals", "assertThat", "assertSoftly", "require" -> codeStyle.staticFunctionCall
        else -> null
    }
}

@Composable
fun String.toExampleCode(
    identifierType: (Highlighting, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    return rememberHighlighted(this) { highlighting ->
        highlight(
            highlighting = highlighting,
            language = Language.Kotlin,
            identifierStyle = { identifierType(highlighting, it) ?: it.toExampleStyle(highlighting) },
        )
    }
}
