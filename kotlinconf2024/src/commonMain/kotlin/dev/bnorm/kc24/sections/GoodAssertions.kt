package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.MacWindow
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.LocalShowTheme
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.rememberAdvancementIndex
import dev.bnorm.librettist.text.AnimateSequence
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.startAnimation
import dev.bnorm.librettist.text.thenLineEndDiff
import kotlin.time.Duration.Companion.milliseconds

fun ShowBuilder.GoodAssertions() {
    section(title = { Text("Good Assertions") }) {
        slide { SectionHeader() }

        // TODO kodee changes for all the difference examples
        slide { FirstExample() }
        slide { SecondExample() }
        slide { ThirdExample() }
        slide { ForthExample() }
    }
}

@Composable
private fun SlideScope.FirstExample() {
    ExampleTestAssertion(
        example = {
            Text(rememberExampleCodeString(firstTest))
            AnimatedVisibility(visible = it, enter = fadeIn(), exit = fadeOut()) {
                Text("\n\n\n\n    ~~~~~~~~~~", color = Color.Red)
            }
        },
        output = {
            Text(firstOutput)
            AnimatedVisibility(visible = it, enter = fadeIn(), exit = fadeOut()) {
                Text("\n                          ~~~~~~~~~~~~~~~~~~~~~~~~~~", color = Color.Red)
            }
        },
        problem = {
            Row(modifier = Modifier.padding(start = 32.dp, top = 32.dp)) {
                Text("=> ", color = Color.Red)
                Text("assertTrue does not result in a helpful failure")
            }
        },
    )
}

@Composable
private fun SlideScope.SecondExample() {
    val state = rememberAdvancementAnimation()
    ExampleTestAssertion(
        example = {
            val sequence =
                startAnimation(rememberExampleCodeString(firstTest))
                    .thenLineEndDiff(rememberExampleCodeString(secondTest))
            AnimateSequence(sequence, state, delay = 20.milliseconds) { Text(it) }
            AnimatedVisibility(visible = it, enter = fadeIn(), exit = fadeOut()) {
                Text("\n\n\n\n                    ~~~~~~~~~~~~", color = Color.Red)
            }
        },
        output = {
            Text(secondOutput)
            AnimatedVisibility(visible = it, enter = fadeIn(), exit = fadeOut()) {
                Text("\n                          ~~~~~~~~~~~~~~~~~~~~~~~~", color = Color.Red)
            }
        },
        problem = {
            Row(modifier = Modifier.padding(start = 32.dp, top = 32.dp)) {
                Text("=> ", color = Color.Red)
                Text("assertEquals only shows the final values")
            }
        },
    )
}

@Composable
private fun SlideScope.ThirdExample() {
    val state = rememberAdvancementAnimation()
    ExampleTestAssertion(
        example = {
            val sequence =
                startAnimation(rememberExampleCodeString(secondTest))
                    .thenLineEndDiff(rememberExampleCodeString(thirdTest))
            AnimateSequence(sequence, state, delay = 25.milliseconds) { Text(it) }
            AnimatedVisibility(visible = it, enter = fadeIn(), exit = fadeOut()) {
                Text("\n\n\n\n                                  ~~~~~~~~~~~~~~~~~~~", color = Color.Red)
            }
        },
        output = {
            Text(thirdOutput)
        },
        problem = {
            Row(modifier = Modifier.padding(start = 32.dp, top = 32.dp)) {
                Text("=> ", color = Color.Red)
                Text("Assertion messages are a maintenance burden")
            }
        },
    )
}

@Composable
private fun SlideScope.ForthExample() {
    val state = rememberAdvancementAnimation()
    ExampleTestAssertion(
        example = {
            val sequence =
                startAnimation(rememberExampleCodeString(thirdTest))
                    .thenLineEndDiff(rememberExampleCodeString(forthTest))
            AnimateSequence(sequence, state, delay = 12.milliseconds) { Text(it) }
            AnimatedVisibility(visible = it, enter = fadeIn(), exit = fadeOut()) {
                Text("\n\n\n\n                        ~~~~~~~~~~", color = Color.Red)
            }
        },
        output = {
            Text(forthOutput)
        },
        problem = {
            Row(modifier = Modifier.padding(start = 32.dp, top = 32.dp)) {
                Text("=> ", color = Color.Red)
                Text("Lots of assertion functions to choose from")
            }
        },
    )
}

@Composable
private fun SlideScope.ExampleTestAssertion(
    example: @Composable (Boolean) -> Unit,
    output: @Composable (Boolean) -> Unit,
    problem: @Composable () -> Unit,
) {
    val advancement by rememberAdvancementIndex(3)
    val showOutput = advancement >= 1
    val showProblem = advancement == 2

    TitleAndBody {
        Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
            Box {
                example(showProblem)
            }
            // TODO make the output appear higher and then shrink when the problem is displayed
            AnimatedVisibility(
                visible = showProblem,
                enter = fadeIn(),
                exit = fadeOut(),
            ) { problem() }
        }

        ProvideTextStyle(MaterialTheme.typography.body2) {
            Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)) {
                AnimatedVisibility(
                    visible = showOutput,
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it },
                ) {
                    MacWindow(
                        modifier = Modifier.animateContentSize()
                            .requiredHeight(if (showProblem) 140.dp else 280.dp)
                    ) {
                        output(showProblem)
                    }
                }
            }
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

                    // Extension functions
                    "hasSize" -> SpanStyle(fontStyle = FontStyle.Italic, color = Color(0xFF56A8F5))

                    // Top-level functions
                    "assertTrue", "assertEquals", "assertThat", "assert" -> SpanStyle(fontStyle = FontStyle.Italic)

                    else -> null
                }
            }
        )
    }
}

private val firstTest: String = """
    @Test
    fun `test number of members in the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertTrue(members.size == 9)
    }
""".trimIndent()

private val firstOutput: String = """
    java.lang.AssertionError: Expected value to be true.
        at [...]
""".trimIndent()

private val secondTest: String = """
    @Test
    fun `test number of members in the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertEquals(9, members.size)
    }
""".trimIndent()

private val secondOutput: String = """
    java.lang.AssertionError: expected:<9> but was:<8>
        at [...]
""".trimIndent()

private val thirdTest: String = """
    @Test
    fun `test number of members in the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertEquals(9, members.size, "Members: ${'$'}members")
    }
""".trimIndent()

private val thirdOutput: String = """
    java.lang.AssertionError: Members: [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli] expected:<9> but was:<8>
        at [...]
""".trimIndent()

private val forthTest: String = """
    @Test
    fun `test number of members in the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertThat(members).hasSize(9)
    }
""".trimIndent()

private val forthOutput: String = """
    org.opentest4j.AssertionFailedError: expected [size]:<[9]> but was:<[8]> ([Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli])
        at [...]
""".trimIndent()
