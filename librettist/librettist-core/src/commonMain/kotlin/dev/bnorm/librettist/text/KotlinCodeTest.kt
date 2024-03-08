package dev.bnorm.librettist.text

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import ch.deletescape.highlight.core.Highlighter
import ch.deletescape.highlight.highlight.render.AnnotatedStringRenderer
import dev.bnorm.librettist.LocalShowTheme

@Composable
fun CodeText(
    code: String,
    language: String,
    modifier: Modifier = Modifier,
) {
    val showTheme = LocalShowTheme.current
    Column(modifier) {
        val highlighter = Highlighter { AnnotatedStringRenderer(showTheme.code) }
        for (line in code.lines()) {
            val result = highlighter.highlight(language, line, graceful = false)
            Text(text = result?.result ?: AnnotatedString(line))
        }
    }
}

@Composable
fun KotlinCodeText(
    text: String,
    modifier: Modifier = Modifier,
    identifierType: (String) -> SpanStyle? = { null },
) {
    CodeText(text, "kotlin", modifier)
}

@Composable
fun GroovyCodeText(
    text: String,
    modifier: Modifier = Modifier,
) {
    CodeText(text, "groovy", modifier)
}
