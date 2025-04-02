package dev.bnorm.kc24.examples

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.KodeeSurprised
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.notes.NotesTab
import kotlinx.collections.immutable.persistentListOf

fun StoryboardBuilder.AssertTrueSmartcastExample() {
    slideForExample(
        builder = {
            openGradle()
            updateGradle()
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
            val gradleTextSequence = persistentListOf(
                GradleText.AddPlugin.animateTo(GradleText.AddConfig),
                GradleText.AddConfig.animateTo(GradleText.AddAssertTrue),
            )
            Example(
                exampleText = AssertTrueSmartcastCode,
                gradleTextSequence = gradleTextSequence,
                outputTextSequence = persistentListOf(persistentListOf(AssertTrueSmartcastOutput)),
                conclusions = null,
            )

            NotesTab("Notes") {
                Text("Finish by 7:00")
            }
        }
    }
}

val AssertTrueSmartcastCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            val aragorn = members.find { it.name == "Aragorn" }
            assertTrue(aragorn != null)
            assertTrue(aragorn.age < 60)
        }
    """.trimIndent().toExampleCode()

val AssertTrueSmartcastOutput = """
java.lang.AssertionError: Assertion failed
assertTrue(aragorn.age < 60)
           |       |   |
           |       |   false
           |       87
           Aragorn
    at [...]
""".trimIndent()
