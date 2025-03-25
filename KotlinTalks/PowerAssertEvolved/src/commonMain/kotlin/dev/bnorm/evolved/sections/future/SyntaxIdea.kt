package dev.bnorm.evolved.sections.future

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.mac.MacTerminalPopup
import dev.bnorm.evolved.sections.intro.BULLET_1
import dev.bnorm.evolved.sections.intro.BULLET_3
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.MagicCode
import dev.bnorm.evolved.template.code.padLines
import dev.bnorm.evolved.template.code.twice
import dev.bnorm.storyboard.core.SceneScope
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.toInt
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

fun StoryboardBuilder.SyntaxIdea() {
    val topicKey = Any()

    @Composable
    fun SceneScope<*>.Topic() {
        ProvideTextStyle(MaterialTheme.typography.h4) {
            Text(
                text = "Language syntax?",
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(topicKey),
                    animatedVisibilityScope = this
                )
            )
        }
    }

    scene(
        stateCount = 8,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderAndBody {
            Topic()
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProvideTextStyle(MaterialTheme.typography.h5) {
                    val reveal = frame.createChildTransition { it.toState() - 1 }
                    RevealEach(reveal) {
                        item { Text("$BULLET_1 Power-Assert as a compiler-plugin means...") }
                        item { Text("    $BULLET_3 Users are required to opt-in to transformation.") }
                        item { Text("    $BULLET_3 Users get to control which source sets are transformed.") }
                        item { Text("$BULLET_1 A proper language feature means...") }
                        item { Text("    $BULLET_3 We could properly support existing functions (require, check, etc.).") }
                        item { Text("    $BULLET_3 Without opt-in, a dependency update could leak call-site information.") }
                        item { Text("    $BULLET_3 We must maintain an opt-in, somewhere.") }
                    }
                }
            }
        }
    }

    scene(
        stateCount = 3,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderAndBody {
            Topic()
            Box(Modifier.fillMaxSize()) {
                Box(Modifier.padding(horizontal = 32.dp)) {
                    frame.createChildTransition { it.toState() }
                        .MagicCode(MACRO_USE_TRANSITIONS)
                }
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    MacTerminalPopup(visible = { it.toInt() == 2 }) {
                        Text(MACRO_OUTPUT)
                    }
                }
            }
        }
    }
}

val MACRO_USE_TRANSITIONS = listOf(
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
""".trimIndent().padLines(10)
