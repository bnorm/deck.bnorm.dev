package dev.bnorm.kc24.sections

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.animation.then
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.text.buildGradleKtsCodeString
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import dev.bnorm.librettist.text.thenLines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.Assertions() {
    ExampleCarousel { AnnotatedString("") to rememberExampleCodeString(firstTest) }
    FirstExample()
    ExampleTransition { firstToSecondTest }
    SecondExample()
    ExampleTransition { secondToThirdTest }
    ThirdExample()
    ExampleTransition { thirdToForth }
    ForthExample()

    // TODO summarize pros/cons before going to the Power-Assert example?

    ExampleTransition { forthToFifth }
    FinalExample()

    // TODO do we need an exit transition before the next section?
}

private fun ShowBuilder.FirstExample() {
    // TODO first example should use `assert` instead?

    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Clear assertion condition"),
        Conclusion.Con(text = "Useless failure message"),
        // TODO more conclusions?
    )
    slideForExample(
        builder = {
            openOutput()
            minimizeOutput()
            repeat(conclusions.size) { addConclusion() }
        }
    ) {
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeBrokenHearted(modifier = Modifier.requiredSize(200.dp))
                }
            }
        ) {
            Example(rememberExampleCodeString(firstTest), null, firstOutput, conclusions)
        }
    }
}

private fun ShowBuilder.SecondExample() {
    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Improved failure message"),
        Conclusion.Con(text = "No intermediate values"),
        // TODO more conclusions?
    )
    slideForExample(
        builder = {
            openOutput()
            minimizeOutput()
            repeat(conclusions.size) { addConclusion() }
        }
    ) {
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeLost(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
                }
            }
        ) {
            Example(rememberExampleCodeString(secondTest), null, secondOutput, conclusions)
        }
    }
}

private fun ShowBuilder.ThirdExample() {
    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Complete failure message"),
        Conclusion.Con(text = "Message before difference"),
        Conclusion.Con(text = "Message maintenance burden"),
        // TODO more conclusions?
    )
    slideForExample(
        builder = {
            openOutput()
            minimizeOutput()
            repeat(conclusions.size) { addConclusion() }
        }
    ) {
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeLost(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
                }
            }
        ) {
            Example(rememberExampleCodeString(thirdTest), null, thirdOutput, conclusions)
        }
    }
}

private fun ShowBuilder.ForthExample() {
    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Complete failure message"),
        Conclusion.Con(text = "Mental load for functions"),
        Conclusion.Con(text = "Library bike-shedding"),
        Conclusion.Pro(text = "Custom failure message"),
        // TODO more conclusions?
    )
    slideForExample(
        builder = {
            openOutput()
            minimizeOutput()
            repeat(conclusions.size) { addConclusion() }
        }
    ) {
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeExcited(modifier = Modifier.requiredSize(200.dp))
                }
            }
        ) {
            Example(rememberExampleCodeString(forthTest), null, forthOutput, conclusions)
        }
    }
}

private fun ShowBuilder.FinalExample() {
    slideForExample(
        builder = {
            openOutput()
            openGradle()
            updateGradle()
            closeGradle()
            updateOutput()
        }
    ) {
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.outputIndex >= 1 }) {
                    KodeeLoving(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
                }

                transition.either(condition = { it.gradleIndex >= 1 }) {
                    KodeeSurprised(modifier = Modifier.requiredSize(150.dp))
                }

                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeSad(modifier = Modifier.requiredSize(150.dp))
                }
            }
        ) {
            Example(rememberExampleCodeString(finalTest), ktsSequence, fifthOutput)
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

private fun String.toStyle(codeStyle: Highlighting) = when (this) {
    "class" -> codeStyle.keyword
    "ExperimentalKotlinGradlePluginApi" -> codeStyle.annotation
    "functions", "excludedSourceSets" -> codeStyle.property
    "kotlin", "version", "powerAssert" -> codeStyle.extensionFunctionCall
    else -> null
}

// language=kotlin
private val firstTest: String = """
    @Test fun `test members of the fellowship`() {
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
    @Test fun `test members of the fellowship`() {
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
    @Test fun `test members of the fellowship`() {
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
    @Test fun `test members of the fellowship`() {
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
    @Test fun `test members of the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assert(members.size == 9)
    }
""".trimIndent()

private val fifthOutput: ImmutableList<String> = startAnimation(
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
