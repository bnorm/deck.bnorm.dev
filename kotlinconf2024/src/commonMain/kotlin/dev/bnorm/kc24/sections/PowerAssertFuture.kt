package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.rememberAdvancementIndex

fun ShowBuilder.PowerAssertFuture() {
    section(title = { Text("Future of Power-Assert") }) {
        slide { SectionHeader() }
        PowerAssertIdeas()
        HowCanYouHelp()
    }
}

private fun ShowBuilder.PowerAssertIdeas() {
    slide {

        TitleAndBody {
            // TODO almost everything here should have a KT ticket with it
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // TODO show examples for compressed and diffs?
                AnimateByLine(
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
    slide {
        TitleAndBody {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // TODO kotl.in/issues link?
                // TODO kotlinlang Slack channel?
                AnimateByLine(
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
private fun SlideScope.AnimateByLine(
    vararg lines: String,
) {
    if (lines.isEmpty()) return

    val index by rememberAdvancementIndex(lines.size)
    for ((i, line) in lines.withIndex()) {
        AnimatedVisibility(
            visible = index >= i,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) { Text(line) }
    }
}