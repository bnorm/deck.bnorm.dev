package dev.bnorm.kc24.sections

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import dev.bnorm.kc24.elements.AnimatedVisibility
import dev.bnorm.kc24.template.SLIDE_CONTENT_SPACING
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder

fun ShowBuilder.PowerAssertFuture() {
    PowerAssertIdeas()
    HowCanYouHelp()
}

private fun ShowBuilder.PowerAssertIdeas() {
    slide(advancements = 7) {
        TitleAndBody {
            Column(
                modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING),
                verticalArrangement = Arrangement.spacedBy(SLIDE_CONTENT_SPACING),
            ) {
                // TODO show examples for compressed and diffs?
                AnimateByLine(
                    transition = transition,
                    "=> Improved diagrams" to emptySet(),
                    "   => Diagram formatting improvements (KT-66807)" to setOf("KT-66807"),
                    "   => Diffs for strings and collections (KT-66806)" to setOf("KT-66806"),
                    "=> Better integration" to emptySet(),
                    "   => Out-of-box support for kotlin.test (KT-63622)" to setOf("KT-63622"),
                    "   => Support for other assertion libraries (KT-66808)" to setOf("KT-66808"),
                    "=> Integration into the language" to emptySet(), // TODO add (create?) a ticket
                )
            }
        }
    }
}

private fun ShowBuilder.HowCanYouHelp() {
    slide(advancements = 4) {
        TitleAndBody {
            Column(
                modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING),
                verticalArrangement = Arrangement.spacedBy(SLIDE_CONTENT_SPACING),
            ) {
                // TODO kotl.in/issues link?
                // TODO kotlinlang Slack channel?
                AnimateByLine(
                    transition = transition,
                    "=> How do you get involved?" to emptySet(),
                    "   => Try out Power-Assert!" to emptySet(),
                    "   => Report any compilation errors" to emptySet(),
                    "   => Report any strange diagrams" to emptySet(),
                )
            }
        }
    }
}

@Composable
private fun AnimateByLine(
    transition: Transition<Int>,
    vararg lines: Pair<String, Set<String>>,
) {
    if (lines.isEmpty()) return

    for ((i, pair) in lines.withIndex()) {
        val (line, tickets) = pair
        transition.createChildTransition { it >= i }.AnimatedVisibility(
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            if (tickets.isEmpty()) {
                Text(line)
            } else {
                // TODO these are needed because clickable text doesn't default to them?!
                val contentColor = LocalContentColor.current
                val textStyle = LocalTextStyle.current
                val lineWithLinks = remember(line, tickets, contentColor) {
                    buildStringWithTicketLink(line, tickets, contentColor)
                }

                // TODO not clickable in overview?
                val uriHandler = LocalUriHandler.current
                ClickableText(
                    text = lineWithLinks,
                    style = textStyle,
                    onClick = { offset ->
                        lineWithLinks.getStringAnnotations("URL", offset, offset).firstOrNull()?.let {
                            uriHandler.openUri(it.item)
                        }
                    }
                )
            }
        }
    }
}

private fun buildStringWithTicketLink(
    line: String,
    tickets: Set<String>,
    contentColor: Color,
): AnnotatedString {

    return buildAnnotatedString {
        withStyle(SpanStyle(color = contentColor)) {
            append(line)
        }
        for (ticket in tickets) {
            val start = line.indexOf(ticket)
            if (start >= 0) {
                val end = start + ticket.length
                addStringAnnotation("URL", "https://youtrack.jetbrains.com/issue/$ticket", start, end)
                addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
            }
        }
    }
}
