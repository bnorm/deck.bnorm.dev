package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.AnimateKodee
import dev.bnorm.deck.shared.DefaultCornerKodee
import dev.bnorm.kc24.elements.AnimatedVisibility
import dev.bnorm.kc24.elements.appendLink
import dev.bnorm.kc24.elements.defaultSpec
import dev.bnorm.kc24.template.SLIDE_CONTENT_SPACING
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.core.toInt
import dev.bnorm.storyboard.easel.SlideSection
import dev.bnorm.storyboard.easel.enter
import dev.bnorm.storyboard.easel.exit
import dev.bnorm.storyboard.easel.notes.NotesTab
import kotlin.time.Duration.Companion.milliseconds

fun StoryboardBuilder.Future() {
    PowerAssertIdeas()
    HowCanYouHelp()
    Resources()
}

fun StoryboardBuilder.PowerAssertIdeas() {
    val lines = listOf(
        AnnotatedString("• Improved diagrams"),
        buildStringWithTicketLink("   • Diagram formatting improvements", "KT-66807"),
        buildStringWithTicketLink("   • Diffs for strings and collections", "KT-66806"),
        AnnotatedString("• Better integration"),
        buildStringWithTicketLink("   • Out-of-box support for kotlin.test", "KT-63622"),
        buildStringWithTicketLink("   • Support for other assertion libraries", "KT-66808"),
        AnnotatedString("• Integration into the language"),
    )

    slide(stateCount = lines.size) {
        TitleAndBody {
            Column(
                modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING),
                verticalArrangement = Arrangement.spacedBy(SLIDE_CONTENT_SPACING),
            ) {
                AnimateByLine(
                    transition = state.createChildTransition { it.toInt() },
                    lines = lines
                )
            }
        }

        NotesTab("Notes") {
            Text("Finish by 13:00")
        }
    }
}

fun StoryboardBuilder.HowCanYouHelp() {
    slide(stateCount = 3) {
        TitleAndBody {
            Column(
                modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING),
                verticalArrangement = Arrangement.spacedBy(SLIDE_CONTENT_SPACING),
            ) {
                state.createChildTransition { it.toInt() >= 0 }.AnimatedVisibility(
                    enter = fadeIn(defaultSpec()) + expandVertically(defaultSpec()),
                    exit = fadeOut(defaultSpec()) + shrinkVertically(defaultSpec()),
                    modifier = Modifier.animateEnterExit(
                        enter = enter(end = { fadeIn(defaultSpec(delay = 300.milliseconds)) }),
                        exit = exit(end = { fadeOut(defaultSpec()) }),
                    ),
                ) {
                    Text("• We're looking for your feedback!")
                }
                state.createChildTransition { it.toInt() >= 1 }.AnimatedVisibility(
                    enter = fadeIn(defaultSpec()) + expandVertically(defaultSpec()),
                    exit = fadeOut(defaultSpec()) + shrinkVertically(defaultSpec()),
                ) {
                    Row {
                        Text(
                            "   • Try out Power-Assert! ",
                            modifier = Modifier.animateEnterExit(
                                enter = enter(end = { fadeIn(defaultSpec(delay = 300.milliseconds)) }),
                                exit = exit(end = { fadeOut(defaultSpec()) }),
                            ),
                        )

                        Text(
                            text = buildAnnotatedString {
                                append("Docs: ")
                                appendLink("kotl.in/power-assert", "https://kotl.in/power-assert")
                            },
                            modifier = Modifier.sharedBounds(
                                rememberSharedContentState("docs-link"),
                                animatedVisibilityScope = this@slide,
                                boundsTransform = { _, _ -> defaultSpec() },
                            ),
                        )
                    }
                }
                state.createChildTransition { it.toInt() >= 2 }.AnimatedVisibility(
                    enter = fadeIn(defaultSpec()) + expandVertically(defaultSpec()),
                    exit = fadeOut(defaultSpec()) + shrinkVertically(defaultSpec()),
                    modifier = Modifier.animateEnterExit(
                        enter = enter(end = { fadeIn(defaultSpec(delay = 300.milliseconds)) }),
                        exit = exit(end = { fadeOut(defaultSpec()) }),
                    ),
                ) {
                    Text("   • Report any compilation errors or strange diagrams")
                }
            }
        }

        NotesTab("Notes") {
            Text("Finish by 14:00")
        }
    }
}

fun StoryboardBuilder.Resources() {
    val lines = listOf(
        buildAnnotatedString {
            append("Slack: ")
            appendLink("#power-assert", "https://kotlinlang.slack.com/archives/C06V6SFE71D")
            append(" (KotlinLang)")
        },
        buildAnnotatedString {
            append("Slides: ")
            appendLink("deck.bnorm.dev/kotlinconf2024", "https://deck.bnorm.dev/kotlinconf2024")
            withStyle(SpanStyle(fontSize = 30.sp)) {
                append("\n                 (Powered by Compose Multiplatform!)")
            }
        },
    )

    slide(stateCount = lines.size + 1) {
        Column(
            modifier = Modifier.animateEnterExit(
                enter = enter(end = { fadeIn(defaultSpec()) + slideInVertically(defaultSpec()) { -it } }),
                exit = exit(end = { fadeOut(defaultSpec()) + slideOutVertically(defaultSpec()) { -it } }),
            )
        ) {
            Box(Modifier.padding(horizontal = SLIDE_PADDING, vertical = SLIDE_CONTENT_SPACING)) {
                ProvideTextStyle(MaterialTheme.typography.h3) {
                    SlideSection.title()
                }
            }
            Spacer(Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
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
                        Text(
                            text = buildAnnotatedString {
                                append("Docs: ")
                                appendLink("kotl.in/power-assert", "https://kotl.in/power-assert")
                            },
                            modifier = Modifier.sharedBounds(
                                rememberSharedContentState("docs-link"),
                                animatedVisibilityScope = this@slide,
                                boundsTransform = { _, _ -> defaultSpec(delay = 300.milliseconds) },
                            ),
                        )

                    AnimateByLine(
                        transition = state.createChildTransition { it.toInt(start = 0) - 1 },
                        lines = lines
                    )
                }
            }
        }

        Box(Modifier.fillMaxSize()) {
            AnimateKodee(
                modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).animateEnterExit(
                    enter = enter(end = { fadeIn(defaultSpec()) + slideInHorizontally(defaultSpec()) { it } }),
                    exit = exit(end = { fadeOut(defaultSpec()) + slideOutHorizontally(defaultSpec()) { it } }),
                )
            ) {
                default { DefaultCornerKodee(Modifier.size(100.dp)) }
            }
        }

        NotesTab("Notes") {
            Text("Finish by 14:00")
        }
    }
}

@Composable
private fun AnimateByLine(
    transition: Transition<out Int>,
    lines: List<AnnotatedString>,
) {
    if (lines.isEmpty()) return

    for ((i, line) in lines.withIndex()) {
        transition.createChildTransition { it >= i }.AnimatedVisibility(
            enter = fadeIn(defaultSpec()) + expandVertically(defaultSpec()),
            exit = fadeOut(defaultSpec()) + shrinkVertically(defaultSpec()),
        ) {
            Text(line)
        }
    }
}

private fun buildStringWithTicketLink(
    line: String,
    ticket: String,
): AnnotatedString {
    return buildAnnotatedString {
        append(line)
        append(" (")
        appendLink(ticket, "https://youtrack.jetbrains.com/issue/$ticket")
        append(")")
    }
}
