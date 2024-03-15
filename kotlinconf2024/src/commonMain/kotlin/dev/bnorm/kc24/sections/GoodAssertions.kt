package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.MacTerminal
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.animation.AnimateSequence
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.show.section
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.rememberAdvancementIndex
import dev.bnorm.librettist.text.buildKotlinCodeString
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
        example = { problemVisible ->
            TextWithError(rememberExampleCodeString(firstTest), problemVisible, firstTest.rangeOf("assertTrue"))
        },
        output = { problemVisible ->
            TextWithError(firstOutput, problemVisible, firstOutputRange)
        },
        problem = {
            Row(modifier = Modifier.padding(start = 64.dp, top = 32.dp)) {
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
        example = { problemVisible ->
            val sequence = startAnimation(rememberExampleCodeString(firstTest))
                    .thenLineEndDiff(rememberExampleCodeString(secondTest))
            AnimateSequence(sequence, state, delay = 20.milliseconds) {
                TextWithError(it, problemVisible, secondTestRange)
            }
        },
        output = { problemVisible ->
            TextWithError(secondOutput, problemVisible, secondOutputRange)
        },
        problem = {
            Row(modifier = Modifier.padding(start = 64.dp, top = 32.dp)) {
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
        example = { problemVisible ->
            val sequence = startAnimation(rememberExampleCodeString(secondTest))
                    .thenLineEndDiff(rememberExampleCodeString(thirdTest))
            AnimateSequence(sequence, state, delay = 25.milliseconds) {
                TextWithError(it, problemVisible, thirdTestRange)
            }
        },
        output = {
            Text(thirdOutput)
        },
        problem = {
            Row(modifier = Modifier.padding(start = 64.dp, top = 32.dp)) {
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
        example = { problemVisible ->
            val sequence = startAnimation(rememberExampleCodeString(thirdTest))
                    .thenLineEndDiff(rememberExampleCodeString(forthTest))
            AnimateSequence(sequence, state, delay = 12.milliseconds) {
                TextWithError(it, problemVisible, forthTestRange)
            }
        },
        output = {
            Text(forthOutput)
        },
        problem = {
            Row(modifier = Modifier.padding(start = 64.dp, top = 32.dp)) {
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
    val outputOffset by animateDpAsState(
        targetValue = when (showProblem) {
            true -> 280.dp
            false -> 0.dp
        }
    )

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
                    MacTerminal(
                        modifier = Modifier.animateContentSize()
                            .requiredHeight(560.dp)
                            .offset(y = outputOffset)
                    ) {
                        Box(modifier = Modifier.padding(32.dp)) {
                            output(showProblem)
                        }
                    }
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

@Composable
private fun TextWithError(text: String, errorVisible: Boolean, textRange: TextRange) {
    TextWithError(AnnotatedString(text), errorVisible, textRange)
}

@Composable
private fun TextWithError(text: AnnotatedString, errorVisible: Boolean, textRange: TextRange) {
    val color by animateColorAsState(
        when (errorVisible) {
            true -> Color.Red
            false -> Color.Transparent
        }
    )

    var layout by remember { mutableStateOf<TextLayoutResult?>(null) }
    Text(
        text = text,
        onTextLayout = { layout = it },
        modifier = if (errorVisible) Modifier.errorUnderline(layout, textRange, color) else Modifier
    )
}

fun Modifier.errorUnderline(layout: TextLayoutResult?, textRange: TextRange, color: Color): Modifier {
    if (layout == null || color == Color.Transparent) return this
    return drawBehind {
        val thickness = 4f
        val amplitude = 2.dp.toPx()
        val wiggles = 4

        // TODO combine indexes that are adjacent?
        for (i in textRange.min..<textRange.max) {
            val rect = layout.getBoundingBox(i)

            val width = rect.right - rect.left
            val halfPeriod = width / wiggles
            val wavyPath = Path().apply {
                moveTo(x = rect.left, y = rect.bottom)
                repeat(wiggles) { i ->
                    relativeQuadraticBezierTo(
                        dx1 = halfPeriod / 2,
                        dy1 = 2 * amplitude * (if (i % 2 == 0) 1 else -1),
                        dx2 = halfPeriod,
                        dy2 = 0f,
                    )
                }
            }

            drawPath(
                path = wavyPath,
                color = color,
                style = Stroke(width = thickness)
            )
        }
    }
}

// TODO make range an annotation within the code somehow? `/*<error>*/`?
fun String.rangeOf(string: String): TextRange {
    val index = indexOf(string)
    if (index == -1) return TextRange.Zero
    return TextRange(index, index + string.length)
}

// language=kotlin
private val firstTest: String = """
    @Test
    fun `test members of the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertTrue(members.size == 9)
    }
""".trimIndent()

private val firstOutput: String = """
    java.lang.AssertionError: Expected value to be true.
        at [...]
""".trimIndent()

val firstOutputRange = firstOutput.rangeOf("Expected value to be true.")

// language=kotlin
private val secondTest: String = """
    @Test
    fun `test members of the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertEquals(9, members.size)
    }
""".trimIndent()

val secondTestRange = secondTest.rangeOf("members.size")

private val secondOutput: String = """
    java.lang.AssertionError: expected:<9> but was:<8>
        at [...]
""".trimIndent()

val secondOutputRange = secondOutput.rangeOf("expected:<9> but was:<8>")

// language=kotlin
private val thirdTest: String = """
    @Test
    fun `test members of the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertEquals(9, members.size, "Members: ${'$'}members")
    }
""".trimIndent()

val thirdTestRange = thirdTest.rangeOf("\"Members: ${'$'}members\"")

private val thirdOutput: String = """
    java.lang.AssertionError: Members: [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli] expected:<9> but was:<8>
        at [...]
""".trimIndent()

// language=kotlin
private val forthTest: String = """
    @Test
    fun `test members of the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertThat(members).hasSize(9)
    }
""".trimIndent()

val forthTestRange = forthTest.rangeOf("hasSize(9)")

private val forthOutput: String = """
    org.opentest4j.AssertionFailedError: expected [size]:<[9]> but was:<[8]> ([Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli])
        at [...]
""".trimIndent()
