package dev.bnorm.evolved.sections.intro

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import dev.bnorm.storyboard.core.SlideState
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit

fun StoryboardBuilder.ExampleOutput() {
    data class State(
        val showExample: Boolean = false,
        val showOutput: Boolean = false,
        val showPowerAssertOutput: Boolean = false,
    )

    slide(
        initial = State(),
        block = {
            next { it.copy(showExample = true) }
            next { it.copy(showOutput = true) }
            next { it.copy(showPowerAssertOutput = true) }
        },
        enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
        exitTransition = SlideExit(alignment = Alignment.CenterEnd),
    ) {
        fun SlideState<State>.toState(): State = when (this) {
            SlideState.Start -> states.first()
            SlideState.End -> states.last().copy(showOutput = false)
            is SlideState.Value -> value
        }

        CornerKodee {
            Column(Modifier.fillMaxSize()) {
                Header()
                Body(Modifier.padding(top = 32.dp, start = 32.dp, end = 32.dp).fillMaxWidth()) {
                    state.AnimatedVisibility(
                        visible = { it.toState().showExample },
                        enter = EnterTransition.None,
                        exit = ExitTransition.None,
                    ) {
                        Text(SAMPLE)
                    }
                }
                Box(Modifier.weight(1f))
                state.AnimatedVisibility(
                    modifier = Modifier.offset(y = 10.dp),
                    visible = { it.toState().showOutput },
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    ProvideTextStyle(MaterialTheme.typography.body2) {
                        MacTerminal(modifier = Modifier.fillMaxWidth()) {
                            if (!currentState.showPowerAssertOutput) {
                                Text(EXAMPLE_OUTPUT)
                            } else {
                                Text(EXAMPLE_POWER_ASSERT_OUTPUT)
                            }
                        }
                    }
                }
            }
        }
    }
}

private val SAMPLE_CODE = """
@Test fun test() {
    assert("Hello".length == "World".substring(1, 4).length)
}
""".trimIndent()

@get:Composable
private val SAMPLE: AnnotatedString
    get() = SAMPLE_CODE.toCode { highlighting, identifier ->
        when (identifier) {
            "test" -> highlighting.functionDeclaration
            else -> null
        }
    }

private val EXAMPLE_OUTPUT = """
Assertion failed.
""".trimIndent().padLines(10)

private val EXAMPLE_POWER_ASSERT_OUTPUT = """
assert("Hello".length == "World".substring(1, 4).length)
               |      |          |               |
               |      |          |               3
               |      |          orl
               |      false
               5
""".trimIndent().padLines(10)
