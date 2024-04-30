@file:OptIn(ExperimentalTextApi::class)

package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.AnimatedVisibility
import dev.bnorm.kc24.elements.defaultSpec
import dev.bnorm.kc24.template.AnimateKodee
import dev.bnorm.kc24.template.SLIDE_CONTENT_SPACING
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideSection
import dev.bnorm.librettist.show.SlideState
import dev.bnorm.librettist.show.toInt

fun ShowBuilder.Future() {
    PowerAssertIdeas()
    HowCanYouHelp()
    Resources()
}

fun ShowBuilder.PowerAssertIdeas() {
    slide(states = 7) {
        TitleAndBody {
            Column(
                modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING),
                verticalArrangement = Arrangement.spacedBy(SLIDE_CONTENT_SPACING),
            ) {
                // TODO show examples for compressed and diffs?
                AnimateByLine(
                    transition = transition.createChildTransition { it.toInt() },
                    "• Improved diagrams" to emptySet(),
                    "   • Diagram formatting improvements (KT-66807)" to setOf("KT-66807"),
                    "   • Diffs for strings and collections (KT-66806)" to setOf("KT-66806"),
                    "• Better integration" to emptySet(),
                    "   • Out-of-box support for kotlin.test (KT-63622)" to setOf("KT-63622"),
                    "   • Support for other assertion libraries (KT-66808)" to setOf("KT-66808"),
                    "• Integration into the language" to emptySet(), // TODO add (create?) a ticket
                )
            }
        }
    }
}

fun ShowBuilder.HowCanYouHelp() {
    slide(states = 4) {
        TitleAndBody {
            Column(
                modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING),
                verticalArrangement = Arrangement.spacedBy(SLIDE_CONTENT_SPACING),
            ) {
                // TODO is this the same as the summary slide?
                // TODO combine with the summary slide?
                AnimateByLine(
                    transition = transition.createChildTransition { it.toInt() },
                    "• We're looking for your feedback!" to emptySet(),
                    "   • Try out Power-Assert!" to emptySet(),
                    "   • Report any compilation errors" to emptySet(),
                    "   • Report any strange diagrams" to emptySet(),
                )
            }
        }
    }
}

fun ShowBuilder.Resources() {
    slide(states = 3) {
        val state = transition.createChildTransition { it != SlideState.Exiting }

        Box {
            state.AnimatedVisibility(
                enter = fadeIn(defaultSpec()) + slideInVertically(defaultSpec()) { -it },
                exit = fadeOut(defaultSpec()) + slideOutVertically(defaultSpec()) { -it },
            ) {
                Column {
                    Box(Modifier.padding(horizontal = SLIDE_PADDING, vertical = SLIDE_CONTENT_SPACING)) {
                        ProvideTextStyle(MaterialTheme.typography.h3) {
                            SlideSection.header()
                        }
                    }
                    Spacer(Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
                }
            }
        }

        Column {
            // This creates constant spacing for when the header disappears.
            // Otherwise, there is a moment when the header is no longer shown and
            Spacer(Modifier.size(SLIDE_CONTENT_SPACING))
            Text("", style = MaterialTheme.typography.h3)
            Spacer(Modifier.size(SLIDE_CONTENT_SPACING))
            Spacer(Modifier.size(4.dp))

            Column(
                modifier = Modifier.padding(SLIDE_PADDING),
                verticalArrangement = Arrangement.spacedBy(SLIDE_CONTENT_SPACING),
            ) {
                ProvideTextStyle(MaterialTheme.typography.body1) {
                    // TODO create these links
                    // TODO make these links clickable
                    AnimateByLine(
                        transition = transition.createChildTransition { it.toInt() },
                        "Docs: kotl.in/power-assert" to emptySet(),
                        "Slack: #power-assert (KotlinLang)" to emptySet(),
                        "Slides: deck.bnorm.dev/kotlinconf2024" to emptySet(),
                    )
                }
            }
        }

        Box(Modifier.fillMaxSize()) {
            state.AnimatedVisibility(
                enter = fadeIn(defaultSpec()) + slideInHorizontally(defaultSpec()) { it },
                exit = fadeOut(defaultSpec()) + slideOutHorizontally(defaultSpec()) { it },
                modifier = Modifier.align(Alignment.BottomEnd),
            ) {
                Box(Modifier.padding(8.dp)) {
                    AnimateKodee {}
                }
            }
        }
    }
}

@Composable
private fun AnimateByLine(
    transition: Transition<out Int>,
    vararg lines: Pair<String, Set<String>>,
) {
    if (lines.isEmpty()) return

    for ((i, pair) in lines.withIndex()) {
        val (line, tickets) = pair
        transition.createChildTransition { it >= i }.AnimatedVisibility(
            enter = fadeIn(defaultSpec()) + expandVertically(defaultSpec()),
            exit = fadeOut(defaultSpec()) + shrinkVertically(defaultSpec()),
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
                        lineWithLinks.getUrlAnnotations(offset, offset).firstOrNull()?.let {
                            uriHandler.openUri(it.item.url)
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
                addUrlAnnotation(UrlAnnotation("https://youtrack.jetbrains.com/issue/$ticket"), start, end)
                addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
            }
        }
    }
}
