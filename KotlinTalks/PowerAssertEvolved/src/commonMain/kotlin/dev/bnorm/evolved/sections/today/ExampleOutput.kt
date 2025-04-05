package dev.bnorm.evolved.sections.today

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.mac.MacTerminal
import dev.bnorm.evolved.template.Body
import dev.bnorm.evolved.template.CornerKodee
import dev.bnorm.evolved.template.Header
import dev.bnorm.evolved.template.code.padLines
import dev.bnorm.evolved.template.code.toCode
import dev.bnorm.evolved.template.slide
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.currentState

fun StoryboardBuilder.ExampleOutput() {
    data class State(
        val sample: Sample,
        val showExample: Boolean = false,
        val showOutput: Boolean = false,
        val showPowerAssertOutput: Boolean = false,
    )

    for (sample in listOf(Sample.ASSERT)) {
        slide(
            initial = State(sample),
            block = {
                next { it.copy(showExample = true) }
                next { it.copy(showOutput = true) }
                next { it.copy(showPowerAssertOutput = true) }
            },
        ) {
            fun Frame<State>.toState(): State = when (this) {
                Frame.Start -> states.first()
                Frame.End -> states.last().copy(showOutput = false)
                is Frame.State -> state
            }

            CornerKodee {
                Column(Modifier.fillMaxSize()) {
                    Header()
                    Body(Modifier.padding(top = 32.dp, start = 32.dp, end = 32.dp).fillMaxWidth()) {
                        frame.AnimatedVisibility(
                            visible = { it.toState().showExample },
                            enter = EnterTransition.None,
                            exit = ExitTransition.None,
                        ) {
                            Text(currentState.sample.code)
                        }
                    }
                    Box(Modifier.weight(1f))
                    frame.AnimatedVisibility(
                        modifier = Modifier.offset(y = 10.dp),
                        visible = { it.toState().showOutput },
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it }),
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.body2) {
                            MacTerminal(modifier = Modifier.fillMaxWidth()) {
                                if (!currentState.showPowerAssertOutput) {
                                    Text(currentState.sample.output)
                                } else {
                                    Text(currentState.sample.powerOutput)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class Sample {
    ASSERT {
        private val text = """
            assert("Hello".length == "World".substring(1, 4).length)
        """.trimIndent()

        @get:Composable
        override val code: AnnotatedString
            get() = text.toCode { highlighting, identifier ->
                when (identifier) {
                    "test" -> highlighting.functionDeclaration
                    else -> null
                }
            }

        override val output = """
            Assertion failed.
        """.trimIndent().padLines(10)

        override val powerOutput = """
            assert("Hello".length == "World".substring(1, 4).length)
                           |      |          |               |
                           |      |          |               3
                           |      |          orl
                           |      false
                           5
        """.trimIndent().padLines(10)
    },

    REQUIRE {
        private val text = """
            require("Hello".length == "World".substring(1, 4).length)
        """.trimIndent()

        @get:Composable
        override val code: AnnotatedString
            get() = text.toCode { highlighting, identifier ->
                when (identifier) {
                    else -> null
                }
            }

        override val output = """
            Failed requirement.
        """.trimIndent().padLines(10)

        override val powerOutput = """
            require("Hello".length == "World".substring(1, 4).length)
                            |      |          |               |
                            |      |          |               3
                            |      |          orl
                            |      false
                            5
        """.trimIndent().padLines(10)
    },

    ;

    @get:Composable
    abstract val code: AnnotatedString
    abstract val output: String
    abstract val powerOutput: String
}
