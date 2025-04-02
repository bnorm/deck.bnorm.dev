package dev.bnorm.kc24.examples

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.KodeeSurprised
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.notes.NotesTab
import kotlinx.collections.immutable.persistentListOf

fun StoryboardBuilder.ComplexExpressionsExample() {
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
                exampleText = ComplexAssertCode,
                gradleTextSequence = null,
                outputTextSequence = persistentListOf(persistentListOf(ComplexAssertOutput)),
                conclusions = null,
            )

            NotesTab("Notes") {
                Text("Finish by 6:00")
            }
        }
    }
}

val ComplexAssertCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assert(members.any { it.name == "Boromir" } &&
                    members.any { it.name == "Aragorn" } ||
                    members.any { it.name == "Elrond" })
        }
    """.trimIndent().toExampleCode { highlighting, it ->
        when (it) {
            "assert" -> highlighting.staticFunctionCall
            else -> null
        }
    }

val ComplexAssertOutput = """
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
