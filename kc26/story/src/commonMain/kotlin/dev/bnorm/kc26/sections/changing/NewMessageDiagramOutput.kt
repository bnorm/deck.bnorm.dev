package dev.bnorm.kc26.sections.changing

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.MagicCodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.deck.shared.mac.MacTerminalPopup
import dev.bnorm.kc26.template.*
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.NewMessageDiagramOutput() {
    carouselScene(
        listOf(
            Output[0] to false,
            Output[0] to true,
            Output[1] to true,
        )
    ) {
        SectionSceneScaffold { padding ->
            Box(Modifier.padding(padding)) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    MagicText(Sample)
                }
            }
        }

        val value = transition.createChildTransition { it.toValue(end = Output[1] to false) }
        MacTerminalPopup(visible = value.createChildTransition { it.second }) {
            Box(Modifier.height(256.dp)) {
                ProvideTextStyle(MaterialTheme.typography.code1 + TextStyle(fontFamily = FontFamily.Monospace)) {
                    MagicCodeSample(value.createChildTransition { it.first })
                }
            }
        }
    }
}

fun StoryboardBuilder.PowerAssertMessageExamples() {
    carouselScene {
        SectionSceneScaffold { padding ->
            Box(Modifier.padding(padding)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("With these classes `toDefaultMessage()` can provide a more dynamic diagram")
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicText(Sample)
                    }
                }

                WIP(modifier = Modifier.align(Alignment.Center))
            }
        }

        MacTerminalPopup(visible = transition.createChildTransition { true }) {
            Box(Modifier.height(256.dp)) {
                ProvideTextStyle(MaterialTheme.typography.code1 + TextStyle(fontFamily = FontFamily.Monospace)) {
                    MagicCodeSample(transition.createChildTransition { Output[1] })
                }
            }
        }
    }
}

private val Sample = """
    @Test fun test() {
        val hello = "Hello"
        assert(hello.length == "World".substring(1, 4).length)
    }
""".trimIndent().toKotlin {
    when (it) {
        "substring" -> extensionFunctionCall
        "length" -> property
        "assert" -> staticFunctionCall
        else -> null
    }
}

private val Output = buildCodeSamples {
    fun String.toCodeSample(): CodeSample = CodeSample { extractTags(trimIndent()) }
    val s by tag("splitter")

    listOf(
        """
            java.lang.AssertionError: Assertion failed
            assert(hello.length == "World".substring(1, 4).length)
                   |     |      |          |               |
                   |     |      |          |               ${s}3${s}
                   |     |      |          ${s}orl${s}
                   |     |      ${s}false${s}
                   |     ${s}5${s}
                   ${s}Hello${s}
        """.toCodeSample(),
        """
            java.lang.AssertionError: Assertion failed
            assert(hello.length == "World".substring(1, 4).length)
                   |     |      |          |               |
                   |     ${s}5${s}      ${s}false${s}      "${s}orl${s}"           ${s}3${s}
                   "${s}Hello${s}"     
        """.toCodeSample(),
    )

}
