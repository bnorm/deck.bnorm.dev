package dev.bnorm.kc24.examples

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.KodeeSad
import dev.bnorm.deck.shared.KodeeSurprised
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.elements.defaultSpec
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.notes.NotesTab
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.highlight.rememberHighlighted
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration.Companion.milliseconds

fun StoryboardBuilder.SoftAssertExample() {
    SoftAssertSetupWithoutMessage()

    // TODO prefix the failure with a warning that we're going to see it fail
    //  - make clear that it is a compiler time warning

    SoftAssertExampleWithWarning()
    SoftAssertSetupWithMessage()
    SoftAssertWithMessageExample()
}

fun StoryboardBuilder.SoftAssertSetupWithoutMessage() {
    // TODO show what the implementation looks like?

    slideForExample(
        builder = {
            openGradle()
            updateGradle()
            closeGradle()
        },
        enterTransition = EnterForward,
        exitTransition = ExitForward,
    ) {
        TitleAndBody {
            val gradleTextSequence = GradleText.AddAssertNotNull.animateTo(GradleText.AddAssertSoftly)
            Example(
                exampleText = SoftAssertWithoutMessageSetup,
                gradleTextSequence = persistentListOf(gradleTextSequence),
                outputTextSequence = null,
                conclusions = null,
            )
        }
    }
}

fun StoryboardBuilder.SoftAssertExampleWithWarning() {
    slideForExample(
        builder = {
            openOutput()
        },
        enterTransition = { slideInHorizontally(defaultSpec(750.milliseconds)) { it } },
        exitTransition = { slideOutHorizontally(defaultSpec(750.milliseconds)) { it } },
    ) {
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeSad(modifier = Modifier.requiredSize(150.dp))
                }
            }
        ) {
            Example(
                exampleText = SoftAssertCode,
                gradleTextSequence = null,
                outputTextSequence = persistentListOf(persistentListOf(SoftAssertOutputWarning)),
                conclusions = null,
            )

            NotesTab("Notes") {
                Text("Finish by 12:00")
            }
        }
    }
}

fun StoryboardBuilder.SoftAssertSetupWithMessage() {
    slideForExample(
        builder = {
            updateExample()
        },
        enterTransition = { slideInHorizontally(defaultSpec(750.milliseconds)) { -it } },
        exitTransition = { slideOutHorizontally(defaultSpec(750.milliseconds)) { -it } },
    ) {
        TitleAndBody {
            val exampleText = transition.createChildTransition {
                when (it.exampleIndex) {
                    0 -> SoftAssertWithoutMessageSetupMagic
                    1 -> SoftAssertWithMessageSetupMagic
                    else -> error("!")
                }
            }

            Example(
                exampleText = exampleText,
                gradleTextSequence = null,
                outputTextSequence = null,
                conclusions = null,
            )
        }
    }
}

fun StoryboardBuilder.SoftAssertWithMessageExample() {
    slideForExample(
        builder = {
            openOutput()
        },
        enterTransition = EnterForward,
        exitTransition = ExitForward,
    ) {
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeSurprised(modifier = Modifier.requiredSize(150.dp))
                }
            }
        ) {
            Example(
                exampleText = SoftAssertCode,
                gradleTextSequence = null,
                outputTextSequence = persistentListOf(persistentListOf(SoftAssertOutput)),
                conclusions = null,
            )
        }
    }
}

@Composable
private fun String.toSetupCode(): AnnotatedString {
    return rememberHighlighted(this) { highlighting ->
        highlight(
            highlighting = highlighting,
            language = Language.Kotlin,
            identifierStyle = {
                when (it) {
                    "assert", "assertSoftly" -> highlighting.functionDeclaration
                    "R" -> highlighting.typeParameters
                    else -> null
                }
            }
        )
    }
}

@Composable
private fun buildSetupCode(
    vararg text: String,
): List<AnnotatedString> {
    val merged = text.joinToString("")
    val highlighted = merged.toSetupCode()
    val split = buildList {
        var index = 0
        for (element in text) {
            this.add(highlighted.subSequence(index, index + element.length))
            index += element.length
        }
    }
    return split
}

val SoftAssertWithoutMessageSetup: AnnotatedString
    @Composable get() = """
        package example                    
        
        fun <R> assertSoftly(block: AssertScope.() -> R): R
        
        interface AssertScope {
            fun assert(assertion: Boolean)
        }
    """.trimIndent().toSetupCode()

val SoftAssertWithoutMessageSetupMagic: List<AnnotatedString>
    @Composable get() = buildSetupCode(
        "package example\n",
        "\n",
        "fun <R> assertSoftly(block: AssertScope.() -> R): R\n",
        "\n",
        "interface AssertScope {\n",
        "    fun assert(", "assertion: Boolean", ")\n",
        "}\n",
    )

val SoftAssertWithMessageSetup: AnnotatedString
    @Composable get() = """
        package example                    
        
        fun <R> assertSoftly(block: AssertScope.() -> R): R
        
        interface AssertScope {
            fun assert(
                assertion: Boolean, 
                message: (() -> String)? = null,
            )
        }
    """.trimIndent().toSetupCode()

val SoftAssertWithMessageSetupMagic: List<AnnotatedString>
    @Composable get() = buildSetupCode(
        "package example\n",
        "\n",
        "fun <R> assertSoftly(block: AssertScope.() -> R): R\n",
        "\n",
        "interface AssertScope {\n",
        "    fun assert(", "\n",
        "        ", "assertion: Boolean", ",\n",
        "        message: (() -> String)? = null,\n",
        "    ", ")\n",
        "}\n",
    )

val SoftAssertCode: AnnotatedString
    @Composable
    get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assertSoftly {
                for (member in members) {
                    assert(member.age < 100)
                }
            }
        }
    """.trimIndent().toExampleCode()

private val SoftAssertOutputWarning = """
w: file://[...] Unable to find overload of function example.AssertScope.assert for power-assert transformation callable as:
 - example.AssertScope.assert(String)
 - example.AssertScope.assert(() -> String)
 - example.AssertScope.assert(kotlin.Boolean, String)
 - example.AssertScope.assert(kotlin.Boolean, () -> String)

Multiple failed assertions
java.lang.AssertionError: Multiple failed assertions
    at [...]
    Suppressed: java.lang.AssertionError
        at [...]
    Suppressed: java.lang.AssertionError
        at [...]
    Suppressed: java.lang.AssertionError
        at [...]
""".trimIndent()

private val SoftAssertOutput = """
Multiple failed assertions
java.lang.AssertionError: Multiple failed assertions
    at [...]
    Suppressed: java.lang.AssertionError: Assertion failed
assert(member.age < 100)
       |      |   |
       |      |   false
       |      10000
       Gandalf
        at [...]
    Suppressed: java.lang.AssertionError: Assertion failed
assert(member.age < 100)
       |      |   |
       |      |   false
       |      2931
       Legolas
        at [...]
    Suppressed: java.lang.AssertionError: Assertion failed
assert(member.age < 100)
       |      |   |
       |      |   false
       |      139
       Gimli
        at [...]
""".trimIndent()
