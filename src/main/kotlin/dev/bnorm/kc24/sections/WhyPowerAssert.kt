package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.AnimateDiff
import dev.bnorm.kc24.elements.MacWindow
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.SlideGroupScope
import dev.bnorm.librettist.animation.rememberAdvancementAnimations
import dev.bnorm.librettist.rememberAdvancementIndex
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.text.KotlinCodeText
import kotlin.time.Duration.Companion.milliseconds

fun SlideGroupScope.WhyPowerAssert() {
    slide { SectionHeader { Text("I would assert...") } }

    // TODO instead of striking and going to good assertions...
    //  1. describe that Power-Assert produces good assertion output,
    //  2. describe the characteristics of good assertion output,
    //  3. then go into the next section.
    PowerAssertIntro()

    GoodAssertions()

    // TODO analyze good assertion problems
    //  1. lots of assert*() functions ( and that's only kotlin.test )
    //  2. confusing parameter ( what's expect, what's actual, where's the message go, etc )
    //  3. requires a custom message if condition is derived from something complex ( easy to forget until something fails )
}

fun SlideGroupScope.PowerAssertIntro() {
    section(title = { Text(introTitle(strike = false)) }) {
        slide { SectionHeader() }

        slide {
            val index by rememberAdvancementIndex(5)
            TitleAndBody(title = {
                Text(introTitle(strike = index > 3))
            }) {
                when (index) {
                    0 -> Unit

                    1 -> Text("● What is Power-Assert?")

                    2 -> {
                        Text(buildAnnotatedString {
                            append("● ")
                            withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                                append("What is Power-Assert?")
                            }
                        })
                    }

                    else -> {
                        Text(buildAnnotatedString {
                            append("● ")
                            withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                                append("What is Power-Assert?")
                            }
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("● What is a good test assertion?")
                    }
                }
            }
        }
    }
}

fun SlideGroupScope.GoodAssertions() {
    section(title = { Text("Good Assertions") }) {
        slide { SectionHeader() }

        slide { FirstExample() }
        slide { SecondExample() }
        slide { ThirdExample() }
    }
}

@Composable
private fun FirstExample() {
    ExampleTestAssertion(
        header = { Text("Is this a good assertion?") },
        example = {
            KotlinCodeText(
                text = firstTest,
                identifierType = {
                    when (it) {
                        "subject" -> SpanStyle(color = Color(0xFFC77DBB))
                        else -> null
                    }
                }
            )
        },
        output = {
            Text(firstOutput)
        },
    )
}

@Composable
private fun SecondExample() {
    val (example, output) = rememberAdvancementAnimations(2)
    ExampleTestAssertion2(
        header = { Text("What about this assertion?") },
        example = {
            AnimateDiff(
                values = listOf(firstTest, intermediateTest, secondTest),
                state = example,
                charDelay = 25.milliseconds,
            ) { text ->
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
        },
        output = {
            AnimateDiff(
                values = listOf(firstOutput, intermediateOutput, secondOutput),
                state = output,
                charDelay = 25.milliseconds,
            ) { text ->
                Text(text)
            }
        },
    )
}

@Composable
private fun ThirdExample() {
    val (example, output) = rememberAdvancementAnimations(2)
    ExampleTestAssertion2(
        header = {
            Text("And finally, how's this one?")
        },
        example = {
            AnimateDiff(
                values = listOf(secondTest, thirdTest),
                state = example,
                charDelay = 25.milliseconds,
            ) { text ->
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
        },
        output = {
            AnimateDiff(
                values = listOf(secondOutput, thirdOutput),
                state = output,
                charDelay = 25.milliseconds,
            ) { text ->
                Text(text)
            }
        },
    )
}

@Composable
private fun ExampleTestAssertion(
    header: @Composable () -> Unit,
    example: @Composable () -> Unit,
    output: @Composable () -> Unit,
) {
    val index by rememberAdvancementIndex(3)
    TitleAndBody {
        Column {
            header()
            ProvideTextStyle(MaterialTheme.typography.body2) {
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(
                    visible = index > 0,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally(),
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        example()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(
                    visible = index > 1,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally(),
                ) {
                    MacWindow { output() }
                }
            }
        }
    }
}

@Composable
private fun ExampleTestAssertion2(
    header: @Composable () -> Unit,
    example: @Composable () -> Unit,
    output: @Composable () -> Unit,
) {
    TitleAndBody {
        Column {
            header()
            ProvideTextStyle(MaterialTheme.typography.body2) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    example()
                }
                Spacer(modifier = Modifier.height(16.dp))
                MacWindow { output() }
            }
        }
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

private fun introTitle(strike: Boolean) = buildAnnotatedString {
    if (strike) {
        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
            append("Power-Assert!")
        }
        append(" Good Assertions")
    } else {
        append("Power-Assert!")
    }
}
