package dev.bnorm.kc24.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.rememberHighlighted
import dev.bnorm.librettist.text.buildKotlinCodeString

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
        buildKotlinCodeString(
            this,
            highlighting,
            identifierType = { identifierType(highlighting, it) ?: it.toExampleStyle(highlighting) })
    }
}