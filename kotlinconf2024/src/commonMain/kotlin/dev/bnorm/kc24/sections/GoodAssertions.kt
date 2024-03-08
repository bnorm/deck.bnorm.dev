package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.MacWindow
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.rememberAdvancementIndex
import dev.bnorm.librettist.text.*
import kotlin.time.Duration.Companion.milliseconds

fun ShowBuilder.GoodAssertions() {
    section(title = { Text("Good Assertions") }) {
        slide { SectionHeader() }

        slide { FirstExample() }
        slide { SecondExample() }
        slide { ThirdExample() }

        // TODO instead of striking and going to good assertions...
        //  1. describe that Power-Assert produces good assertion output,
        //  2. describe the characteristics of good assertion output,
        //  3. then go into the next section.

        // TODO analyze good assertion problems
        //  1. lots of assert*() functions ( and that's only kotlin.test )
        //  2. confusing parameter ( what's expect, what's actual, where's the message go, etc )
        //  3. requires a custom message if condition is derived from something complex ( easy to forget until something fails )
    }
}

@Composable
private fun SlideScope.FirstExample() {
    ExampleTestAssertion(
        example = { SampleCode(firstTest) },
        output = { SampleOutput(firstOutput) },
        problem = { Text("PROBLEM!") },
    )
}

@Composable
private fun SlideScope.SecondExample() {
    ExampleTestAssertion(
        example = {
            val sequence = startTextAnimation(firstTest)
                .thenLineEndDiff(secondTest)
            AnimateText(sequence, delay = 25.milliseconds) { SampleCode(it) }
        },
        output = { SampleOutput(secondOutput) },
        problem = { Text("PROBLEM!") },
    )
}

@Composable
private fun SlideScope.ThirdExample() {
    ExampleTestAssertion(
        example = {
            val sequence = startTextAnimation(secondTest)
                .thenLineEndDiff(thirdTest)
            AnimateText(sequence, delay = 25.milliseconds) { SampleCode(it) }
        },
        output = { SampleOutput(thirdOutput) },
        problem = { Text("PROBLEM!") },
    )
}

@Composable
private fun SlideScope.ExampleTestAssertion(
    example: @Composable () -> Unit,
    output: @Composable () -> Unit,
    problem: @Composable () -> Unit,
) {
    val advancement by rememberAdvancementIndex(4)
    TitleAndBody {
        ProvideTextStyle(MaterialTheme.typography.body2) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.TopStart).padding(start = 16.dp)) {
                    example()
                    AnimatedVisibility(
                        visible = advancement == 3,
                        enter = slideInVertically { 2 * it },
                        exit = slideOutVertically { 2 * it },
                    ) { problem() }
                }

                Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)) {
                    AnimatedVisibility(
                        visible = advancement == 1,
                        enter = slideInVertically { 2 * it },
                        exit = slideOutVertically { 2 * it },
                    ) { output() }
                }
            }
        }
    }
}

@Composable
private fun SampleCode(text: String) {
    KotlinCodeText(
        text = text,
        identifierType = {
            when (it) {
                "subject" -> SpanStyle(color = Color(0xFFC77DBB))
                else -> null
            }
        }
    )
}

@Composable
private fun SampleOutput(text: String) {
    // TODO maybe switch to an IntelliJ style test display?
    MacWindow {
        Text(text)
    }
}

private val intermediateTest: String = """
    @Test
    fun test() {
        val actual: List<Int> = subject.operation()
    
    }
""".trimIndent()

private val intermediateOutput: String = """
    org.opentest4j.AssertionFailedError: 
        [...]
        at kotlin.test.AssertionsKt.assertEquals${'$'}default(Unknown Source)
        at SampleTest.test(SampleTest.kt:14)
        [...]
""".trimIndent()

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
