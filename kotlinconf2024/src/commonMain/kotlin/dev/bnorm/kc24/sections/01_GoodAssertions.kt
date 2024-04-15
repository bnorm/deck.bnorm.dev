package dev.bnorm.kc24.sections

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.rememberHighlighted
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import dev.bnorm.librettist.text.thenLines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.GoodAssertions() {
    ExampleCarousel(
        start = { },
        end = { Example(rememberExampleCodeString(firstTest)) }
    )
    AssertTrueExample()
    ExampleTransition { firstToSecondTest }
    AssertEqualsExample()
    ExampleTransition { secondToThirdTest }
    AssertEqualsMessageExample()
    ExampleTransition { thirdToForth }
    AssertKExample()

    // TODO summarize pros/cons before going to the Power-Assert example?

    // TODO should we push this into the next section?
    //  - show other assertion library examples...
    //  - ...then section can be "Why Power-Assert?"


//    ExampleTransition { forthToFifth }

    // TODO do we need an exit transition before the next section?
}

private fun ShowBuilder.AssertTrueExample() {
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
            Example(
                exampleTextSequence = persistentListOf(rememberExampleCodeString(firstTest)),
                outputTextSequence = persistentListOf(persistentListOf(firstOutput)),
                conclusions = conclusions,
            )
        }
    }
}

private fun ShowBuilder.AssertEqualsExample() {
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
            Example(
                exampleTextSequence = persistentListOf(rememberExampleCodeString(secondTest)),
                outputTextSequence = persistentListOf(persistentListOf(secondOutput)),
                conclusions = conclusions,
            )
        }
    }
}

private fun ShowBuilder.AssertEqualsMessageExample() {
    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Complete failure message"),
        Conclusion.Con(text = "Message can be forgotten"),
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
                    KodeeExcited(modifier = Modifier.requiredSize(200.dp))
                }
            }
        ) {
            Example(
                exampleTextSequence = persistentListOf(rememberExampleCodeString(thirdTest)),
                outputTextSequence = persistentListOf(persistentListOf(thirdOutput)),
                conclusions = conclusions,
            )
        }
    }
}

private fun ShowBuilder.AssertKExample() {
    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Complete failure message"),
        Conclusion.Con(text = "Mental load for functions"),
        Conclusion.Con(text = "Assertions for custom types"),
        Conclusion.Con(text = "Library bike-shedding"),
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
            Example(
                exampleTextSequence = persistentListOf(rememberExampleCodeString(forthTest)),
                outputTextSequence = persistentListOf(persistentListOf(forthOutput)),
                conclusions = conclusions,
            )
        }
    }
}

fun ShowBuilder.PowerAssertExample() {
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
            val gradleTextSequence = persistentListOf(GradleText.Initial.animateTo(GradleText.AddPlugin))
            Example(
                exampleTextSequence = persistentListOf(finalTest),
                gradleTextSequence = gradleTextSequence,
                outputTextSequence = persistentListOf(fifthOutput),
            )
        }
    }
}

@Composable
private fun rememberExampleCodeString(text: String): AnnotatedString {
    return rememberHighlighted(text) { highlighting ->
        buildKotlinCodeString(text, highlighting, identifierType = { it.toExampleStyle(highlighting) })
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
    get() = rememberHighlighted("firstToSecondTest") { highlighting ->
        val first = buildKotlinCodeString(firstTest, highlighting, identifierType = { it.toExampleStyle(highlighting) })
        val second = buildKotlinCodeString(secondTest, highlighting, identifierType = { it.toExampleStyle(highlighting) })
        startAnimation(first).thenLineEndDiff(second).toList()
    }

private val secondOutput: String = """
    java.lang.AssertionError: expected:<9> but was:<8>
        at [...]
""".trimIndent()

private val secondToThirdTest: ImmutableList<AnnotatedString>
    @Composable
    get() = rememberHighlighted("secondToThirdTest") { highlighting ->
        val second = buildKotlinCodeString(secondTest, highlighting, identifierType = { it.toExampleStyle(highlighting) })
        val third = buildKotlinCodeString(thirdTest, highlighting, identifierType = { it.toExampleStyle(highlighting) })
        startAnimation(second).thenLineEndDiff(third).toList()
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
    get() = rememberHighlighted("thirdToForth") { highlighting ->
        val third = buildKotlinCodeString(thirdTest, highlighting, identifierType = { it.toExampleStyle(highlighting) })
        val forth = buildKotlinCodeString(forthTest, highlighting, identifierType = { it.toExampleStyle(highlighting) })
        startAnimation(third).thenLineEndDiff(forth).toList()
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
        val end = finalTest
        return rememberHighlighted("forthToFifth") { highlighting ->
            val forth = buildKotlinCodeString(forthTest, highlighting, identifierType = { it.toExampleStyle(highlighting) })
            startAnimation(forth).thenLineEndDiff(end).toList()
        }
    }

val finalTest: AnnotatedString
    @Composable
    get() = rememberHighlighted("finalTest") { highlighting ->
        buildKotlinCodeString(
            // language=kotlin
            text = """
                @Test fun `test members of the fellowship`() {
                    val members = fellowshipOfTheRing.getCurrentMembers()
                    assert(members.size == 9)
                }
            """.trimIndent(),
            codeStyle = highlighting,
            identifierType = { it.toExampleStyle(highlighting) }
        )
    }

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
