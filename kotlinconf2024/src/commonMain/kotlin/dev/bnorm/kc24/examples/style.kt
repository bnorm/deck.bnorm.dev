package dev.bnorm.kc24.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.rememberHighlighted
import dev.bnorm.librettist.text.buildKotlinCodeString

fun String.toExampleStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "fellowshipOfTheRing", "size", "name", "age", "race" -> codeStyle.property
        "indices" -> codeStyle.property // TODO extension properties
        "`test members of the fellowship`" -> codeStyle.functionDeclaration // TODO "get" not working?
        "hasSize", "find", "any" -> codeStyle.extensionFunctionCall
        "assertTrue", "assertEquals", "assertThat", "assertSoftly", "require" -> codeStyle.staticFunctionCall
        else -> null
    }
}

@Composable
fun String.toExampleCode(
    identifierType: (Highlighting, String) -> SpanStyle? = { highlighting, identifier ->
        identifier.toExampleStyle(highlighting)
    },
): AnnotatedString {
    return rememberHighlighted(this) { highlighting ->
        buildKotlinCodeString(this, highlighting, identifierType = { identifierType(highlighting, it) })
    }
}