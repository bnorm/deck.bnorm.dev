package dev.bnorm.kc24.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.MacWindow
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.LocalShowTheme
import dev.bnorm.librettist.animation.AnimationState
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.rememberAdvancementBoolean
import dev.bnorm.librettist.text.AnimateSequence
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.startAnimation
import dev.bnorm.librettist.text.thenLines

fun ShowBuilder.PowerAssertIntro() {
    slide { SectionHeader(animateToBody = false) { Text("I would assert...") } }

    section(title = { Text("Power-Assert") }) {
        slide { SectionHeader() }

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
    slide {
        val outputPopup by rememberAdvancementBoolean()

        TitleAndBody(
            kodee = {
                show(condition = { outputPopup }) {
                    KodeeBrokenHearted(modifier = Modifier.requiredSize(150.dp))
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(rememberExampleCodeString(powerAssertSample))

                Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)) {
                    AnimatedVisibility(
                        visible = outputPopup,
                        enter = slideInVertically { 2 * it },
                        exit = slideOutVertically { 2 * it },
                    ) {
                        MacWindow {
                            Text(simpleOutput.padLines(3))
                        }
                    }
                }
            }
        }
    }
}

private fun ShowBuilder.WithPowerAssert() {
    slide {
        val outputPopup by rememberAdvancementBoolean()
        val state = rememberAdvancementAnimation()

        TitleAndBody(
            kodee = {
                show(condition = { state.value == AnimationState.COMPLETE }) {
                    KodeeLoving(modifier = Modifier.requiredSize(150.dp).graphicsLayer { rotationY = 180f })
                }

                show(condition = { outputPopup }) {
                    KodeeSurprised(Modifier.requiredSize(100.dp))
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(rememberExampleCodeString(powerAssertSample))

                Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)) {
                    AnimatedVisibility(
                        visible = outputPopup,
                        enter = slideInVertically { 2 * it },
                        exit = slideOutVertically { 2 * it },
                    ) {
                        AnimateSequence(powerAssertOutput, state) { text ->
                            MacWindow {
                                Text(text.padLines(5))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun String.padLines(count: Int): String {
    val lines = count { it == '\n' }
    if (lines >= count) return this

    return buildString {
        append(this@padLines)
        repeat(count - lines) {
            appendLine()
        }
    }
}

@Composable
private fun rememberExampleCodeString(text: String): AnnotatedString {
    val codeStyle = LocalShowTheme.current.code
    return remember(text) {
        buildKotlinCodeString(
            text, codeStyle,
            identifierType = {
                when (it) {
                    // Properties
                    "fellowshipOfTheRing", "size" -> SpanStyle(color = Color(0xFFC77DBB))

                    // Function declarations
                    "`test number of members in the fellowship`" -> SpanStyle(color = Color(0xFF56A8F5))

                    else -> null
                }
            }
        )
    }
}

val powerAssertSample =
    """
        @Test
        fun `test number of members in the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assert(members.size == 9)
        }
    """.trimIndent()

val simpleOutput =
    """
        java.lang.AssertionError: Assertion failed
    """.trimIndent()

val powerAssertOutput = startAnimation(
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
               [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, ...
    """.trimIndent(),
)
