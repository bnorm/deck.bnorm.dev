package dev.bnorm.kc24.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.MacTerminal
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.section
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.thenLines

fun ShowBuilder.PowerAssertIntro() {
    SectionHeader(animateToBody = false) { Text("I would assert...") }

    section(title = { Text("Power-Assert") }) {
        SectionHeader()

        WithoutPowerAssert()

        // TODO adding the magic slide
        //  gradle symbol going across the screen?
        slide { TitleSlide { Text("Power-Assert Magic!") } }

        WithPowerAssert()

        // TODO add more complex example?
        //  assert(members.filter { it.age > 50 }.size == 3)
        //     => gandalf, legolas, gimli, aragorn
    }
}

private fun ShowBuilder.WithoutPowerAssert() {
    slide(advancements = 2) {
        val outputPopup = transition.createChildTransition { it == 1 }

        TitleAndBody(
            kodee = {
                show(condition = { outputPopup.currentState }) {
                    KodeeBrokenHearted(modifier = Modifier.requiredSize(300.dp))
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                    Text(rememberExampleCodeString(powerAssertSample))
                }

                TestFailureOutput(
                    visible = outputPopup.currentState,
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)
                ) {
                    Text(simpleOutput)
                }
            }
        }
    }
}

private fun ShowBuilder.WithPowerAssert() {
    slide(advancements = 4) {

        val outputPopup = transition.createChildTransition { it >= 1 }
        val showCode = transition.createChildTransition { it >= 2 }
        val state = transition.createChildTransition { it == 3 }
        val outputText by state.animateList(powerAssertOutput) { if (it) powerAssertOutput.lastIndex else 0 }

        TitleAndBody(
            kodee = {
                show(condition = { !state.isRunning && state.currentState }) {
                    // TODO can this be moved out of the way of the example?
                    KodeeLoving(modifier = Modifier.requiredSize(300.dp).graphicsLayer { rotationY = 180f })
                }

                show(condition = { showCode.currentState }) {
                    KodeeSurprised(Modifier.requiredSize(200.dp))
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                    Text(rememberExampleCodeString(powerAssertSample))
                }

                TestFailureOutput(
                    visible = outputPopup.currentState,
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)
                ) {
                    if (showCode.currentState) {
                        Text(outputText, modifier = Modifier.wrapContentWidth(Alignment.Start, unbounded = true))
                    } else {
                        Text(simpleOutput)
                    }
                }
            }
        }
    }
}

@Composable
private fun TestFailureOutput(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
        ) {
            MacTerminal(modifier = Modifier.requiredHeight(560.dp)) {
                Box(modifier = Modifier.padding(32.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun rememberExampleCodeString(text: String): AnnotatedString {
    val codeStyle = ShowTheme.code
    return remember(text) {
        buildKotlinCodeString(text, codeStyle, identifierType = { it.toExampleStyle(codeStyle) })
    }
}

private fun String.toExampleStyle(codeStyle: Highlighting) = when (this) {
    "fellowshipOfTheRing", "size" -> codeStyle.property
    "`test members of the fellowship`" -> codeStyle.functionDeclaration
    "hasSize" -> codeStyle.extensionFunctionCall
    "assertTrue", "assertEquals", "assertThat", "assert" -> codeStyle.staticFunctionCall
    else -> null
}

// language=kotlin
private val powerAssertSample =
    """
        @Test
        fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assert(members.size == 9)
        }
    """.trimIndent()

private val simpleOutput =
    """
        java.lang.AssertionError: Assertion failed
    """.trimIndent()

private val powerAssertOutput = startAnimation(
    """
        java.lang.AssertionError: Assertion failed
        assert(members.size == 9)
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(members.size == 9)
                            |
                            false
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(members.size == 9)
                       |    |
                       |    false
                       8
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(members.size == 9)
               |       |    |
               |       |    false
               |       8
               [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
    """.trimIndent(),
).sequence.toList()
