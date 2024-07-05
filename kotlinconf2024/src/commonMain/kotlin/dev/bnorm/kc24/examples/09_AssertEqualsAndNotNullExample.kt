package dev.bnorm.kc24.examples

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.template.KodeeSurprised
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.assist.ShowAssistTab
import dev.bnorm.librettist.text.thenDiff
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.AssertEqualsAndNotNullExample() {
    slideForExample(
        builder = {
            openGradle()
            updateGradle()
            closeGradle()
            openOutput()
            closeOutput()

            updateExample()

            openGradle()
            updateGradle()
            closeGradle()
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
            val exampleTextSequence =
                startAnimation(AssertEqualsCode).thenDiff(AssertEqualsAndNotNullCode).toList()
            val gradleTextSequence = persistentListOf(
                GradleText.AddSourceSet.animateTo(GradleText.AddAssertEquals),
                GradleText.AddAssertEquals.animateTo(GradleText.AddAssertNotNull),
            )
            val outputText =
                if (transition.currentState.exampleIndex > 0) assertEqualsAndNotNullOutput else AssertEqualsOutput
            Example(
                exampleTextSequence = exampleTextSequence,
                gradleTextSequence = gradleTextSequence,
                outputTextSequence = persistentListOf(persistentListOf(outputText)),
            )

            ShowAssistTab("Notes") {
                Text("Finish by 10:00")
            }
        }
    }
}

// TODO is race a good example to use?
//  - may be a touchy topic to use during a talk.
//  - does lotr make that not a concern?
val AssertEqualsCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            val aragorn = members.find { it.name == "Aragorn" }
            val boromir = members.find { it.name == "Boromir" }
            assertEquals(aragorn?.race, boromir?.race)
        }
    """.trimIndent().toExampleCode()

// TODO !!! THIS IS DOCTORED OUTPUT !!!
//  - `assertEquals` prints on the same line as the exception name
//  - "expected:[...]" prints on the same line as "Aragorn"
//  - do we need to hardcode a newline before and after the diagram?
//  - should we show a link to a ticket if it doesn't get fixed?
val AssertEqualsOutput = """
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

val AssertEqualsAndNotNullCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            val aragorn = assertNotNull(members.find { it.name == "Aragorn" })
            val boromir = assertNotNull(members.find { it.name == "Boromir" })
            assertEquals(aragorn.race, boromir.race)
        }
    """.trimIndent().toExampleCode()

// TODO !!! THIS IS DOCTORED OUTPUT !!!
//  - `assertNotNull` prints on the same line as the exception name
//  - do we need to hardcode a newline before and after the diagram?
//  - should we show a link to a ticket if it doesn't get fixed?
val assertEqualsAndNotNullOutput = """
java.lang.AssertionError:
assertNotNull(members.find { it.name == "Boromir" })
              |       |
              |       null
              [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
    at [...]
""".trimIndent()
