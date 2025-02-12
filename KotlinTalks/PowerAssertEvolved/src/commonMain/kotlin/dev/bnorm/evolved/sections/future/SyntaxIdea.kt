package dev.bnorm.evolved.sections.future

import androidx.compose.animation.*
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import dev.bnorm.deck.shared.mac.MacTerminal
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.MagicCode
import dev.bnorm.evolved.template.code.twice
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide

fun StoryboardBuilder.SyntaxIdea() {
    // TODO slides which talk about why we need a call-site syntax
    // TODO do we want to talk about any other syntax ideas?

    slide(stateCount = 4) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("Language syntax?")
            }
            ProvideTextStyle(MaterialTheme.typography.body2) {
                Column(Modifier.fillMaxSize()) {
                    state.AnimatedVisibility(
                        visible = { it.toState() >= 1 },
                        enter = EnterTransition.None,
                        exit = ExitTransition.None,
                    ) {
                        state.createChildTransition { it.toState() }
                            .MagicCode(MACRO_USE_TRANSITIONS)
                    }
                    Box(Modifier.weight(1f))
                    state.AnimatedVisibility(
                        visible = { it.toState() >= 3 },
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it }),
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.body2) {
                            MacTerminal(modifier = Modifier.fillMaxWidth()) {
                                Text(MACRO_OUTPUT)
                            }
                        }
                    }
                }
            }
        }
    }
}

val MACRO_USE_TRANSITIONS = listOf(
    """
        require("Hello".length == "World".substring(1, 4).length)
    """.trimIndent().twice(),

    """
        require<i></i>("Hello".length == "World".substring(1, 4).length)
    """.trimIndent() to """
        require<i>!</i>("Hello".length == "World".substring(1, 4).length)
    """.trimIndent(),

    """
        require!("Hello".length == "World".substring(1, 4).length)
    """.trimIndent().twice(),
)

val MACRO_OUTPUT = """
Failed requirement.
require!("Hello".length == "World".substring(1, 4).length)
                 |      |          |               |
                 5      false      orl             3



""".trimIndent()
