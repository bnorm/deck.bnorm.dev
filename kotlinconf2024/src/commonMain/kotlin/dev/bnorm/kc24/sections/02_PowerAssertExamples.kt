package dev.bnorm.kc24.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.elements.typingSpec
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.rememberHighlighted
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.thenDiff
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

fun ShowBuilder.PowerAssertExamples() {
    // TODO examples
    //  - assertNotNull -> surrounding deep not null checks
    //    - make part of another example?
    //  - at this point we could compare assertTrue/assertEquals/assertNotNull as the primary toolbox

    // TODO should each example have conclusions?
    // TODO should each example end with a question that leads into the next example?

    ExampleCarousel { AnnotatedString("") to complexAssertExample }
    ComplexExpressions()
    ExampleCarousel { complexAssertExample to assertTrueExample }
    AssertTrue()
    ExampleCarousel { assertTrueExample to requireExample }
    Require()
    ExampleCarousel { requireExample to assertEqualsExample }
    AssertEquals()
    ExampleTransition(transitionSpec = {
        typingSpec(count = abs(targetState - initialState), charDelay = 30.milliseconds)
    }) { startAnimation(assertEqualsExample).thenDiff(assertEqualsAndNotNullExample).toList() }
    AssertEqualsAndNotNull()

    // TODO summary slide before going into the next example?

    ExampleCarousel { assertEqualsAndNotNullExample to softAssertSetupWithoutMessage }
    SoftAssertSetupWithoutMessage()
    ExampleCarousel { softAssertSetupWithoutMessage to softAssertExample }
    SoftAssertExampleWithWarning()
    ExampleCarousel(forward = false) { softAssertExample to softAssertSetupWithoutMessage }
    SoftAssertSetupWithMessage()
    ExampleCarousel { softAssertSetupWithMessage to softAssertExample }
    SoftAssertExample()
}

private fun ShowBuilder.ComplexExpressions() {
    slideForExample(
        builder = {
            openOutput()
            closeOutput()
        }
    ) {
        TitleAndBody {
            Example(
                exampleTextSequence = persistentListOf(complexAssertExample),
                outputTextSequence = persistentListOf(persistentListOf(complexAssertOutput)),
            )
        }
    }
}

private fun ShowBuilder.AssertTrue() {
    // TODO add some transitions to walk through...
    //  - start with assert and show error for UNSAFE_CALL?
    //  - switch to assertTrue and show without diagram?

    slideForExample(
        builder = {
            openGradle()
            updateGradle()
            updateGradle()
            closeGradle()
            openOutput()
            closeOutput()
        }
    ) {
        TitleAndBody {
            val gradleTextSequence = persistentListOf(
                GradleText.AddPlugin.animateTo(GradleText.AddConfig),
                GradleText.AddConfig.animateTo(GradleText.AddAssertTrue),
            )
            Example(
                exampleTextSequence = persistentListOf(assertTrueExample),
                gradleTextSequence = gradleTextSequence,
                outputTextSequence = persistentListOf(persistentListOf(assertTrueOutput)),
            )
        }
    }
}

private fun ShowBuilder.Require() {
    slideForExample(
        builder = {
            openGradle()
            updateGradle()
            updateGradle()
            closeGradle()
            openOutput()
            closeOutput()
        }
    ) {
        TitleAndBody {
            val gradleTextSequence = persistentListOf(
                GradleText.AddAssertTrue.animateTo(GradleText.AddRequire),
                GradleText.AddRequire.animateTo(GradleText.AddSourceSet),
            )
            Example(
                exampleTextSequence = persistentListOf(requireExample),
                gradleTextSequence = gradleTextSequence,
                outputTextSequence = persistentListOf(persistentListOf(requireOutput)),
            )
        }
    }
}

private fun ShowBuilder.AssertEquals() {
    slideForExample(
        builder = {
            openGradle()
            updateGradle()
            closeGradle()
            openOutput()
            closeOutput()
        }
    ) {
        TitleAndBody {
            val gradleTextSequence = GradleText.AddSourceSet.animateTo(GradleText.AddAssertEquals)
            Example(
                exampleTextSequence = persistentListOf(assertEqualsExample),
                gradleTextSequence = persistentListOf(gradleTextSequence),
                outputTextSequence = persistentListOf(persistentListOf(assertEqualsOutput)),
            )
        }
    }
}

private fun ShowBuilder.AssertEqualsAndNotNull() {
    slideForExample(
        builder = {
            openGradle()
            updateGradle()
            closeGradle()
            openOutput()
            closeOutput()
        }
    ) {
        TitleAndBody {
            val gradleTextSequence = GradleText.AddAssertEquals.animateTo(GradleText.AddAssertNotNull)
            Example(
                exampleTextSequence = persistentListOf(assertEqualsAndNotNullExample),
                gradleTextSequence = persistentListOf(gradleTextSequence),
                outputTextSequence = persistentListOf(persistentListOf(assertEqualsAndNotNullOutput)),
            )
        }
    }
}

// ======================= //
// ===== Text Values ===== //
// ======================= //

private fun String.toExampleStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "fellowshipOfTheRing", "name", "age", "race" -> codeStyle.property
        "indices" -> codeStyle.property // TODO extension properties
        "`test members of the fellowship`", "get" -> codeStyle.functionDeclaration // TODO "get" not working?
        "find", "any" -> codeStyle.extensionFunctionCall
        "assertTrue", "assertEquals", "assertSoftly", "require" -> codeStyle.staticFunctionCall
        else -> null
    }
}

// region <Complex assert Example>
private val complexAssertExample: AnnotatedString
    @Composable
    get() = rememberHighlighted("complexAssertExample") { highlighting ->
        buildKotlinCodeString(
            // language=kotlin
            text = """
                @Test fun `test members of the fellowship`() {
                    val members = fellowshipOfTheRing.getCurrentMembers()
                    assert(members.any { it.name == "Boromir" } &&
                            members.any { it.name == "Aragorn" } ||
                            members.any { it.name == "Elrond" })
                }
            """.trimIndent(),
            codeStyle = highlighting,
            identifierType = {
                it.toExampleStyle(highlighting) ?: when (it) {
                    "assert" -> highlighting.staticFunctionCall
                    else -> null
                }
            }
        )
    }
// endregion

// region <Complex assert Output>
private val complexAssertOutput = """
java.lang.AssertionError: Assertion failed
assert(members.any { it.name == "Boromir" } &&
       |       |
       |       false
       [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
        members.any { it.name == "Aragorn" } ||
        members.any { it.name == "Elrond" })
        |       |
        |       false
        [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
    at [...]
""".trimIndent()
// endregion

// region <assertTrue Example>
private val assertTrueExample: AnnotatedString
    @Composable
    get() = rememberHighlighted("assertTrueExample") { highlighting ->
        buildKotlinCodeString(
            // language=kotlin
            text = """
                @Test fun `test members of the fellowship`() {
                    val members = fellowshipOfTheRing.getCurrentMembers()
                    val aragorn = members.find { it.name == "Aragorn" }
                    assertTrue(aragorn != null)
                    assertTrue(aragorn.age < 60)
                }
            """.trimIndent(),
            codeStyle = highlighting,
            identifierType = { it.toExampleStyle(highlighting) }
        )
    }
// endregion

// region <assertTrue Output>
private val assertTrueOutput = """
java.lang.AssertionError: Assertion failed
assertTrue(aragorn.age < 60)
           |       |   |
           |       |   false
           |       87
           Aragorn
    at [...]
""".trimIndent()
// endregion

// TODO
//  - surround example with a little more context?
//  - is there a better example than this?
// region <require Example>
private val requireExample: AnnotatedString
    @Composable
    get() = rememberHighlighted("requireExample") { highlighting ->
        buildKotlinCodeString(
            // language=kotlin
            text = """
                operator fun get(index: Int): Character {
                    require(index in members.indices)
                    return members[index]
                }
            """.trimIndent(),
            codeStyle = highlighting,
            identifierType = {
                it.toExampleStyle(highlighting) ?: when (it) {
                    "members" -> highlighting.property
                    else -> null
                }
            }
        )
    }
// endregion

// TODO !!! THIS IS DOCTORED OUTPUT !!!
//  - `members` prints out "FellowshipOfTheRing" as well because it is a receiver
//  - can/should we ignore implicit receivers?
//  - should "Assertion failed" as the prefix also be removed?
//  - should we show a link to tickets if these things doesn't get fixed?
// region <require Output>
private val requireOutput = """
java.lang.IllegalArgumentException: Assertion failed
require(index in members.indices)
        |     |  |       |
        |     |  |       0..8
        |     |  [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli, Boromir]
        |     false
        10
    at [...]
""".trimIndent()
// endregion

// TODO is race a good example to use?
//  - may be a touchy topic to use during a talk.
//    - does lotr make that not a concern?
//    - man being the race is a little weird... while not technically correct, should it be human?
// region <assertEquals Example>
private val assertEqualsExample: AnnotatedString
    @Composable
    get() = rememberHighlighted("assertEqualsExample") { highlighting ->
        buildKotlinCodeString(
            // language=kotlin
            text = """
                @Test fun `test members of the fellowship`() {
                    val members = fellowshipOfTheRing.getCurrentMembers()
                    val aragorn = members.find { it.name == "Aragorn" }
                    val boromir = members.find { it.name == "Boromir" }
                    assertEquals(aragorn?.race, boromir?.race)
                }
            """.trimIndent(),
            codeStyle = highlighting,
            identifierType = { it.toExampleStyle(highlighting) }
        )
    }
// endregion

// TODO !!! THIS IS DOCTORED OUTPUT !!!
//  - `assertEquals` prints on the same line as the exception name
//  - "expected:[...]" prints on the same line as "Aragorn"
//  - do we need to hardcode a newline before and after the diagram?
//  - should we show a link to a ticket if it doesn't get fixed?
// region <assertEquals Output>
private val assertEqualsOutput = """
java.lang.AssertionError:
assertEquals(aragorn?.race, boromir?.race)
             |        |     |        |
             |        |     |        null
             |        |     null
             |        Dúnadan
             Aragorn
expected:<Dúnadan> but was:<null>
    at [...]
""".trimIndent()
// endregion

// region <assertEquals+assertNotNull Example>
private val assertEqualsAndNotNullExample: AnnotatedString
    @Composable
    get() = rememberHighlighted("assertEqualsAndNotNullExample") { highlighting ->
        buildKotlinCodeString(
            // language=kotlin
            text = """
                @Test fun `test members of the fellowship`() {
                    val members = fellowshipOfTheRing.getCurrentMembers()
                    val aragorn = assertNotNull(members.find { it.name == "Aragorn" })
                    val boromir = assertNotNull(members.find { it.name == "Boromir" })
                    assertEquals(aragorn.race, boromir.race)
                }
            """.trimIndent(),
            codeStyle = highlighting,
            identifierType = { it.toExampleStyle(highlighting) }
        )
    }
// endregion

// TODO !!! THIS IS DOCTORED OUTPUT !!!
//  - `assertNotNull` prints on the same line as the exception name
//  - do we need to hardcode a newline before and after the diagram?
//  - should we show a link to a ticket if it doesn't get fixed?
// region <assertEquals+assertNotNull Output>
private val assertEqualsAndNotNullOutput = """
java.lang.AssertionError:
assertNotNull(members.find { it.name == "Boromir" })
              |       |
              |       null
              [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
    at [...]
""".trimIndent()
// endregion