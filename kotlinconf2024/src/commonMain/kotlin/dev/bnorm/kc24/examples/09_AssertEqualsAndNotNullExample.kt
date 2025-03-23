package dev.bnorm.kc24.examples

import androidx.compose.animation.core.createChildTransition
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

fun StoryboardBuilder.AssertEqualsAndNotNullExample() {
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
        sceneScope.TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeSurprised(modifier = Modifier.requiredSize(150.dp))
                }
            }
        ) {
            val exampleText = transition.createChildTransition {
                when (it.exampleIndex) {
                    0 -> AssertEqualsCode
                    1 -> AssertEqualsAndNotNullCode
                    else -> error("!")
                }
            }
            val gradleTextSequence = persistentListOf(
                GradleText.AddSourceSet.animateTo(GradleText.AddAssertEquals),
                GradleText.AddAssertEquals.animateTo(GradleText.AddAssertNotNull),
            )
            val outputText =
                if (transition.currentState.exampleIndex > 0) assertEqualsAndNotNullOutput else AssertEqualsOutput
            Example(
                exampleText = exampleText,
                gradleTextSequence = gradleTextSequence,
                outputTextSequence = persistentListOf(persistentListOf(outputText)),
                conclusions = null,
            )

            NotesTab("Notes") {
                Text("Finish by 10:00")
            }
        }
    }
}

val AssertEqualsCode: List<AnnotatedString>
    @Composable get() = buildExampleCode(
        "@Test fun `test members of the fellowship`() {\n",
        "    val members = fellowshipOfTheRing.getCurrentMembers()\n",
        "    val aragorn = ", "members.find { it.name == \"Aragorn\" }", "\n",
        "    val boromir = ", "members.find { it.name == \"Boromir\" }", "\n",
        "    assertEquals(aragorn", "?", ".race, boromir", "?", ".race)\n",
        "}\n",
    )

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

val AssertEqualsAndNotNullCode: List<AnnotatedString>
    @Composable get() = buildExampleCode(
        "@Test fun `test members of the fellowship`() {\n",
        "    val members = fellowshipOfTheRing.getCurrentMembers()\n",
        "    val aragorn = ", "assertNotNull(", "members.find { it.name == \"Aragorn\" }", ")", "\n",
        "    val boromir = ", "assertNotNull(", "members.find { it.name == \"Boromir\" }", ")", "\n",
        "    assertEquals(aragorn", ".race, boromir", ".race)\n",
        "}\n",
    )

val assertEqualsAndNotNullOutput = """
java.lang.AssertionError:
assertNotNull(members.find { it.name == "Boromir" })
              |       |
              |       null
              [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
    at [...]
""".trimIndent()

@Composable
private fun buildExampleCode(
    vararg text: String,
): List<AnnotatedString> {
    val merged = text.joinToString("")
    val highlighted = merged.toExampleCode()
    val split = buildList {
        var index = 0
        for (element in text) {
            this.add(highlighted.subSequence(index, index + element.length))
            index += element.length
        }
    }
    return split
}
