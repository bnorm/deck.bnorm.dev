@file:OptIn(ExperimentalTransitionApi::class, ExperimentalAnimationApi::class)

package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.AnimatedVisibility
import dev.bnorm.kc24.elements.MacTerminal
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideState
import dev.bnorm.librettist.show.toInt
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import dev.bnorm.librettist.text.thenLines
import kotlinx.collections.immutable.ImmutableList

fun ShowBuilder.GoodAssertions() {
    // TODO kodee changes for all the difference examples
    FirstExample()
    ExampleTransition { firstToSecondTest }
    SecondExample()
    ExampleTransition { secondToThirdTest }
    ThirdExample()
    ExampleTransition { thirdToForth }
    ForthExample()
    ExampleTransition { forthToFifth }
    FinalExample()
}

private fun ShowBuilder.FirstExample() {
    slide(states = 3) {
        val state = transition.exampleTransition()
        ExampleTestAssertion(
            transition = state,
            example = { problemVisible ->
                val test = rememberExampleCodeString(firstTest)
                TextWithError(test, problemVisible, firstTestRange)
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
            kodee = {
                show(condition = { state.at(ExampleState.ShowOutput) }) {
                    KodeeBrokenHearted(modifier = Modifier.requiredSize(200.dp))
                }
            }
        )
    }
}

private fun ShowBuilder.SecondExample() {
    slide(states = 3) {
        val state = transition.exampleTransition()
        ExampleTestAssertion(
            transition = state,
            example = { problemVisible ->
                val test = rememberExampleCodeString(secondTest)
                TextWithError(test, problemVisible, secondTestRange)
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
            kodee = {
                show(condition = { state.at(ExampleState.ShowOutput) }) {
                    KodeeLost(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
                }
            }
        )
    }
}

private fun ShowBuilder.ThirdExample() {
    slide(states = 3) {
        val state = transition.exampleTransition()
        ExampleTestAssertion(
            transition = state,
            example = { problemVisible ->
                val test = rememberExampleCodeString(thirdTest)
                TextWithError(test, problemVisible, thirdTestRange)
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
            kodee = {
                show(condition = { state.at(ExampleState.ShowOutput) }) {
                    KodeeExcited(modifier = Modifier.requiredSize(200.dp))
                }
            }
        )
    }
}

private fun ShowBuilder.ForthExample() {
    slide(states = 3) {
        val state = transition.exampleTransition()
        ExampleTestAssertion(
            transition = state,
            example = { problemVisible ->
                val test = rememberExampleCodeString(forthTest)
                TextWithError(test, problemVisible, forthTestRange)
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
            kodee = {
                show(condition = { state.at(ExampleState.ShowOutput) }) {
                    KodeeExcited(modifier = Modifier.requiredSize(200.dp))
                }
            }
        )
    }
}

private fun ShowBuilder.FinalExample() {
    slide(states = 3) {
        val output = fifthOutput
        val outputIndex = transition.createChildTransition {
            val value = it.toInt()
            when {
                value <= 1 -> 0
                value >= 2 -> output.lastIndex
                else -> error("!") // Exhaustive
            }
        }
        val exampleState = transition.createChildTransition {
            val value = it.toInt()
            when {
                value <= 0 -> ExampleState.ShowExample
                value >= 1 -> ExampleState.ShowOutput
                else -> error("!") // Exhaustive
            }
        }
        ExampleTestAssertion(
            transition = exampleState,
            example = { problemVisible ->
                val test = rememberExampleCodeString(finalTest)
                TextWithError(test, problemVisible, fifthTestRange)
            },
            output = {
                val outputText = outputIndex.animateList(output) { it }.value
                Text(outputText)
            },
            kodee = {
                show(condition = { transition.currentState.toInt() >= 2 && transition.targetState.toInt() >= 2 }) {
                    KodeeLoving(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
                }

                show(condition = { transition.currentState.toInt() >= 1 && transition.targetState.toInt() >= 1 }) {
                    KodeeBrokenHearted(modifier = Modifier.requiredSize(200.dp))
                }
            }
        )
    }
}

enum class ExampleState {
    ShowExample,
    ShowOutput,
    ShowProblem,
}

@Composable
fun Transition<out SlideState<Int>>.exampleTransition(): Transition<ExampleState> {
    return createChildTransition {
        when (it.toInt()) {
            1 -> ExampleState.ShowOutput
            2 -> ExampleState.ShowProblem
            else -> ExampleState.ShowExample
        }
    }
}

@Composable
private fun ExampleTestAssertion(
    transition: Transition<ExampleState>, // TODO convert to an enum?
    example: @Composable (Boolean) -> Unit,
    output: @Composable (Boolean) -> Unit = {},
    problem: @Composable () -> Unit = {},
    kodee: KodeeScope.() -> Unit = {},
) {
    val showProblem = transition.createChildTransition { it == ExampleState.ShowProblem }
    val outputOffset by transition.createChildTransition {
        when (it) {
            ExampleState.ShowExample -> 600.dp // Output off-screen
            ExampleState.ShowOutput -> 40.dp // Output in middle of screen
            ExampleState.ShowProblem -> 320.dp // Output at bottom of screen
        }
    }.animateDp { it }

    TitleAndBody(kodee = kodee) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                Box {
                    example(showProblem.targetState)
                }
                showProblem.AnimatedVisibility(
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) { problem() }
            }

            ProvideTextStyle(MaterialTheme.typography.body2) {
                Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)) {
                    MacTerminal(
                        modifier = Modifier.animateContentSize()
                            .requiredHeight(600.dp)
                            .offset(y = outputOffset)
                    ) {
                        Box(modifier = Modifier.padding(32.dp)) {
                            output(showProblem.targetState)
                        }
                    }
                }
            }
        }
    }
}

private fun ShowBuilder.ExampleTransition(
    strings: @Composable () -> ImmutableList<AnnotatedString>,
) {
    slide(states = 0) {
        val next = transition.createChildTransition { it == SlideState.Exiting }
        ExampleTestAssertion(
            transition = transition.createChildTransition { ExampleState.ShowExample },
            example = { _ ->
                val values = strings()
                val text = next.animateList(
                    values = values,
                    targetIndexByState = { if (it) values.lastIndex else 0 },
                )

                Text(text.value)
            },
            output = { },
            problem = { },
        )
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
            if (i >= layout.layoutInput.text.length) break

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

val firstTestRange = firstTest.rangeOf("assertTrue")

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

private val firstToSecondTest: ImmutableList<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            val first = buildKotlinCodeString(firstTest, codeStyle, identifierType = { it.toExampleStyle(codeStyle) })
            val second = buildKotlinCodeString(secondTest, codeStyle, identifierType = { it.toExampleStyle(codeStyle) })
            startAnimation(first).thenLineEndDiff(second).toList()
        }
    }

private val secondOutput: String = """
    java.lang.AssertionError: expected:<9> but was:<8>
        at [...]
""".trimIndent()

val secondOutputRange = secondOutput.rangeOf("expected:<9> but was:<8>")

private val secondToThirdTest: ImmutableList<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            val second = buildKotlinCodeString(secondTest, codeStyle, identifierType = { it.toExampleStyle(codeStyle) })
            val third = buildKotlinCodeString(thirdTest, codeStyle, identifierType = { it.toExampleStyle(codeStyle) })
            startAnimation(second).thenLineEndDiff(third).toList()
        }
    }

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

private val thirdToForth: ImmutableList<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            val third = buildKotlinCodeString(thirdTest, codeStyle, identifierType = { it.toExampleStyle(codeStyle) })
            val forth = buildKotlinCodeString(forthTest, codeStyle, identifierType = { it.toExampleStyle(codeStyle) })
            startAnimation(third).thenLineEndDiff(forth).toList()
        }
    }

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


private val forthToFifth: ImmutableList<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            val forth = buildKotlinCodeString(forthTest, codeStyle, identifierType = { it.toExampleStyle(codeStyle) })
            val fifth = buildKotlinCodeString(finalTest, codeStyle, identifierType = { it.toExampleStyle(codeStyle) })
            startAnimation(forth).thenLineEndDiff(fifth).toList()
        }
    }

// language=kotlin
private val finalTest: String = """
    @Test
    fun `test members of the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assert(members.size == 9)
    }
""".trimIndent()

val fifthTestRange = finalTest.rangeOf("hasSize(9)")

private val fifthOutput = startAnimation(
    """
        java.lang.AssertionError: Assertion failed
    """.trimIndent(),
).thenLines(
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
).toList()