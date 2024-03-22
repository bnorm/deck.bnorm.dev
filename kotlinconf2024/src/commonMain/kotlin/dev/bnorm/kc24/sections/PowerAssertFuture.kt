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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            // TODO almost everything here should have a KT ticket with it
            Column(
                modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING),
                verticalArrangement = Arrangement.spacedBy(SLIDE_CONTENT_SPACING),
            ) {
                // TODO show examples for compressed and diffs?
                AnimateByLine(
                    transition = transition,
                    "=> Improved diagrams",
                    "   => Diagram formatting improvements",
                    "   => Diffs for strings and collections",
                    "=> Better integration",
                    "   => Out-of-box support for kotlin.test",
                    "   => Support for other assertion libraries",
                    "=> Integration into the language",
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
                    "=> How do you get involved?",
                    "   => Try out Power-Assert!",
                    "   => Report any compilation errors",
                    "   => Report any strange diagrams",
                )
            }
        }
    }
}

@Composable
private fun AnimateByLine(
    transition: Transition<Int>,
    vararg lines: String,
) {
    if (lines.isEmpty()) return

    for ((i, line) in lines.withIndex()) {
        transition.createChildTransition { it >= i }.AnimatedVisibility(
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) { Text(line) }
    }
}
