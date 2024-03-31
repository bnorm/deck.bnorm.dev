@file:OptIn(ExperimentalTransitionApi::class, ExperimentalAnimationApi::class)

package dev.bnorm.kc24.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.*
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.animation.then
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideState
import dev.bnorm.librettist.show.slideForValues
import dev.bnorm.librettist.text.buildGradleKtsCodeString
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import dev.bnorm.librettist.text.thenLines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration.Companion.milliseconds

fun ShowBuilder.Assertions() {
    ExampleCarousel { AnnotatedString("") to rememberExampleCodeString(firstTest) }
    FirstExample()
    ExampleTransition { firstToSecondTest }
    SecondExample()
    ExampleTransition { secondToThirdTest }
    ThirdExample()
    ExampleTransition { thirdToForth }
    ForthExample()
    ExampleTransition { forthToFifth }
    FinalExample()

    // TODO do we need an exit transition before the next section?
}

private fun ShowBuilder.FirstExample() {
    // TODO first example should use `assert` instead?

    val states = persistentListOf(
        TemplateState.Example,
        TemplateState.Output(index = 0),
        TemplateState.Conclusion.Pro(text = "Clear assertion condition"),
        TemplateState.Conclusion.Con(text = "Useless failure message"),
        // TODO more conclusions?
    )
    slideForValues(states) {
        val state = transition.toTemplateState()
        TitleAndBody(
            kodee = {
                show(condition = {
                    (state.currentState is TemplateState.Output || state.currentState is TemplateState.Conclusion) &&
                            (state.targetState is TemplateState.Output || state.targetState is TemplateState.Conclusion)
                }) {
                    KodeeBrokenHearted(modifier = Modifier.requiredSize(200.dp))
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                    Text(rememberExampleCodeString(firstTest))
                    state.ShowConclusions(states)
                }

                OutputText(
                    text = firstOutput,
                    state = state.toOutputState(),
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

private fun ShowBuilder.SecondExample() {
    val states = persistentListOf(
        TemplateState.Example,
        TemplateState.Output(index = 0),
        TemplateState.Conclusion.Pro(text = "Improved failure message"),
        TemplateState.Conclusion.Con(text = "No intermediate values"),
        // TODO more conclusions?
    )
    slideForValues(states) {
        val state = transition.toTemplateState()
        TitleAndBody(
            kodee = {
                show(condition = {
                    (state.currentState is TemplateState.Output || state.currentState is TemplateState.Conclusion) &&
                            (state.targetState is TemplateState.Output || state.targetState is TemplateState.Conclusion)
                }) {
                    KodeeLost(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                    Text(rememberExampleCodeString(secondTest))
                    state.ShowConclusions(states)
                }

                OutputText(
                    text = secondOutput,
                    state = state.toOutputState(),
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

private fun ShowBuilder.ThirdExample() {
    val states = persistentListOf(
        TemplateState.Example,
        TemplateState.Output(index = 0),
        TemplateState.Conclusion.Pro(text = "Complete failure message"),
        TemplateState.Conclusion.Con(text = "Message maintenance burden"),
        // TODO more conclusions?
    )
    slideForValues(states) {
        val state = transition.toTemplateState()
        TitleAndBody(
            kodee = {
                show(condition = {
                    (state.currentState is TemplateState.Output || state.currentState is TemplateState.Conclusion) &&
                            (state.targetState is TemplateState.Output || state.targetState is TemplateState.Conclusion)
                }) {
                    KodeeExcited(modifier = Modifier.requiredSize(200.dp))
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                    Text(rememberExampleCodeString(thirdTest))
                    state.ShowConclusions(states)
                }

                OutputText(
                    text = thirdOutput,
                    state = state.toOutputState(),
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

private fun ShowBuilder.ForthExample() {
    val states = persistentListOf(
        TemplateState.Example,
        TemplateState.Output(index = 0),
        TemplateState.Conclusion.Pro(text = "Complete failure message"),
        TemplateState.Conclusion.Con(text = "Mental load for functions"),
        TemplateState.Conclusion.Con(text = "Library bike-shedding"),
        // TODO more conclusions?
    )
    slideForValues(states) {
        val state = transition.toTemplateState()
        TitleAndBody(
            kodee = {
                show(condition = {
                    (state.currentState is TemplateState.Output || state.currentState is TemplateState.Conclusion) &&
                            (state.targetState is TemplateState.Output || state.targetState is TemplateState.Conclusion)
                }) {
                    KodeeExcited(modifier = Modifier.requiredSize(200.dp))
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                    Text(rememberExampleCodeString(forthTest))
                    state.ShowConclusions(states)
                }

                OutputText(
                    text = forthOutput,
                    state = state.toOutputState(),
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}

private fun ShowBuilder.FinalExample() {
    val states = persistentListOf(
        TemplateState.Example,
        TemplateState.Output(index = 0), // show output
        TemplateState.Output(index = 1), // show gradle
        TemplateState.Output(index = 2), // change gradle
        TemplateState.Output(index = 3), // hide gradle
        TemplateState.Output(index = 4), // change output
    )
    slideForValues(states) {
        val state = transition.toTemplateState(exiting = states.last())

        val outputValues = fifthOutput
        val outputState = state.createChildTransition { it.index >= 4 }
        val outputText by outputState.animateList(
            values = outputValues,
            transitionSpec = { typingSpec(count = outputValues.size - 1, charDelay = 100.milliseconds) },
        ) { if (it) outputValues.lastIndex else 0 }

        val gradleValues = ktsSequence
        val gradleState = state.createChildTransition { it.index >= 2 }
        val gradleText by gradleState.animateList(
            values = gradleValues,
            transitionSpec = { typingSpec(count = gradleValues.size - 1) },
        ) { if (it) gradleValues.lastIndex else 0 }

        TitleAndBody(
            kodee = {
                show(condition = { state.currentState.index >= 4 && state.targetState.index >= 4 }) {
                    KodeeLoving(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
                }

                show(condition = { state.currentState.index >= 2 || state.targetState.index >= 2 }) {
                    KodeeSurprised(modifier = Modifier.requiredSize(150.dp))
                }

                show(condition = { state.currentState is TemplateState.Output || state.targetState is TemplateState.Output }) {
                    KodeeSad(modifier = Modifier.requiredSize(150.dp))
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                    Text(rememberExampleCodeString(finalTest))
                }

                OutputText(
                    text = outputText,
                    state = state.toOutputState(),
                    modifier = Modifier.align(Alignment.BottomStart)
                )

                GradleFile(
                    text = gradleText,
                    visible = state.createChildTransition { it.index in 1..2 },
                    modifier = Modifier.align(Alignment.TopEnd),
                )
            }
        }
    }
}

@Composable
fun Transition<out SlideState<TemplateState>>.toTemplateState(
    entering: TemplateState = TemplateState.Example,
    exiting: TemplateState = TemplateState.Example,
): Transition<TemplateState> {
    return createChildTransition {
        when (it) {
            SlideState.Entering -> entering
            SlideState.Exiting -> exiting
            is SlideState.Index -> it.value
        }
    }
}

@Composable
private fun Transition<out TemplateState>.toOutputState(): Transition<OutputState> {
    return createChildTransition {
        when (it) {
            is TemplateState.Output -> OutputState.Visible
            is TemplateState.Conclusion -> OutputState.Minimized
            else -> OutputState.Hidden
        }
    }
}

sealed interface TemplateState {
    data object Example : TemplateState
    data class Output(val index: Int) : TemplateState
    sealed interface Conclusion : TemplateState {
        val text: String

        data class Pro(override val text: String) : Conclusion
        data class Con(override val text: String) : Conclusion
    }
}

val TemplateState.index: Int
    get() = when (this) {
        TemplateState.Example -> Int.MIN_VALUE
        is TemplateState.Output -> index
        is TemplateState.Conclusion -> Int.MIN_VALUE
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

private fun String.toStyle(codeStyle: Highlighting) = when (this) {
    "class" -> codeStyle.keyword
    "ExperimentalKotlinGradlePluginApi" -> codeStyle.annotation
    "functions", "excludedSourceSets" -> codeStyle.property
    "kotlin", "version", "powerAssert" -> codeStyle.extensionFunctionCall
    else -> null
}

@Composable
private fun Transition<TemplateState>.ShowConclusions(
    states: ImmutableList<TemplateState>,
) {
    val conclusions = remember(states) { states.filterIsInstance<TemplateState.Conclusion>() }
    val withIndex = remember(conclusions) { conclusions.mapIndexed { index, conclusion -> index to conclusion } }
    val state = createChildTransition { conclusions.indexOf(it) }

    @Composable
    fun Conclusion(index: Int, text: String, pro: Boolean) {
        state.AnimatedVisibility(
            visible = { it >= index },
            enter = fadeIn(defaultSpec()),
            exit = fadeOut(defaultSpec()),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (pro) {
                    Icon(
                        Icons.Filled.Done, tint = Color.White, contentDescription = "",
                        modifier = Modifier.size(48.dp).background(Color(0xFF009900), shape = CircleShape)
                    )
                } else {
                    Icon(
                        Icons.Filled.Close, tint = Color.White, contentDescription = "",
                        modifier = Modifier.size(48.dp).background(Color(0xFF990000), shape = CircleShape)
                    )
                }
                Spacer(Modifier.width(16.dp))
                Text(text)
            }
        }
    }

    Row(modifier = Modifier.fillMaxWidth().padding(SLIDE_CONTENT_SPACING)) {
        Column(modifier = Modifier.weight(1f)) {
            for ((index, conclusion) in withIndex.filter { it.second is TemplateState.Conclusion.Pro }) {
                Conclusion(index, conclusion.text, pro = true)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            for ((index, conclusion) in withIndex.filter { it.second is TemplateState.Conclusion.Con }) {
                Conclusion(index, conclusion.text, pro = false)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
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

// language=kotlin
private val secondTest: String = """
    @Test
    fun `test members of the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertEquals(9, members.size)
    }
""".trimIndent()

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

val ktsSequence: ImmutableList<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { it.toStyle(codeStyle) }
        )

        return remember {
            startAnimation(
                buildString(
                    """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                        }
                    """.trimIndent(),
                ),
            ).then(
                buildString(
                    """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                        
                        }
                    """.trimIndent(),
                ),
            ).thenLineEndDiff(
                buildString(
                    """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                            kotlin("plugin.power-assert") version "2.0.0"
                        }
                    """.trimIndent(),
                ),
            ).toList()
        }
    }
