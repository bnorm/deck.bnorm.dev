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
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.MacWindow
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
            MacWindow {
                Text(firstOutput)
                AnimatedVisibility(visible = it, enter = fadeIn(), exit = fadeOut()) {
                    Text("\n                                     ~~~~~~~~~~~~~~~~~~~~~~~~~~", color = Color.Red)
                }
            }
        },
        problem = {
            Text(
                text = "=> assertTrue does not result in a helpful failure",
                modifier = Modifier.padding(start = 32.dp, top = 32.dp),
            )
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
                Text("\n\n\n\n                    ~~~~~~~~~~~", color = Color.Red)
            }
        },
        output = {
            MacWindow {
                Text(secondOutput)
                AnimatedVisibility(visible = it, enter = fadeIn(), exit = fadeOut()) {
                    Text("\n                                     ~~~~~~~~~~~~~~~~~~~~~~~~~~", color = Color.Red)
                }
            }
        },
        problem = {
            Text(
                text = "=> assertEquals only shows the final values",
                modifier = Modifier.padding(start = 32.dp, top = 32.dp),
            )
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
                Text("\n\n\n\n                                 ~~~~~~~~~~~~~~~~~", color = Color.Red)
            }
        },
        output = {
            MacWindow { Text(thirdOutput) }
        },
        problem = {
            Text(
                text = "=> Assertion messages are a maintenance burden",
                modifier = Modifier.padding(start = 32.dp, top = 32.dp),
            )
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
            AnimateSequence(sequence, state, delay = 15.milliseconds) { Text(it) }
            AnimatedVisibility(visible = it, enter = fadeIn(), exit = fadeOut()) {
                Text("\n\n\n\n                       ~~~~~~~", color = Color.Red)
            }
        },
        output = {
            MacWindow { Text(forthOutput) }
        },
        problem = {
            Text(
                text = "=> Lots of assertion functions to choose from",
                modifier = Modifier.padding(start = 32.dp, top = 32.dp),
            )
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
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.TopStart).padding(start = 16.dp)) {
                Box {
                    example(showProblem)
                }
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
                        enter = slideInVertically { 2 * it },
                        exit = slideOutVertically { 2 * it },
                    ) { output(showProblem) }
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
                    "subject", "size" -> SpanStyle(color = Color(0xFFC77DBB))

                    // Function declarations
                    "test" -> SpanStyle(color = Color(0xFF56A8F5))

                    else -> null
                }
            }
        )
    }
}

private val firstTest: String = """
    @Test
    fun test() {
        val actual: List<Int> = subject.operation()
        assertTrue(4 == actual.size)
    }
""".trimIndent()

private val firstOutput: String = """
    org.opentest4j.AssertionFailedError: Expected value to be true.
        [...]
        at kotlin.test.AssertionsKt.assertEquals${'$'}default(Unknown Source)
        at SampleTest.test(SampleTest.kt:14)
        [...]
""".trimIndent()

private val secondTest: String = """
    @Test
    fun test() {
        val actual: List<Int> = subject.operation()
        assertEquals(4, actual.size)
    }
""".trimIndent()

private val secondOutput: String = """
    org.opentest4j.AssertionFailedError: expected: <4> but was: <3>
        [...]
        at kotlin.test.AssertionsKt.assertEquals${'$'}default(Unknown Source)
        at SampleTest.test(SampleTest.kt:14)
        [...]
""".trimIndent()

private val thirdTest: String = """
    @Test
    fun test() {
        val actual: List<Int> = subject.operation()
        assertEquals(4, actual.size, "Actual: ${'$'}actual")
    }
""".trimIndent()

private val thirdOutput: String = """
    org.opentest4j.AssertionFailedError: Actual: [1, 2, 3] ==> expected: <4> but was: <3>
        [...]
        at kotlin.test.AssertionsKt.assertEquals${'$'}default(Unknown Source)
        at SampleTest.test(SampleTest.kt:14)
        [...]
""".trimIndent()

private val forthTest: String = """
    @Test
    fun test() {
        val actual: List<Int> = subject.operation()
        assertThat(actual).hasSize(4)
    }
""".trimIndent()

private val forthOutput: String = """
    org.opentest4j.AssertionFailedError: expected [size]:<[4]> but was:<[3]> ([1, 2, 3])
        at SampleTest.test(SampleTest.kt:16)



""".trimIndent()