package dev.bnorm.kc24.examples

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.KodeeExcited
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.notes.NotesTab
import kotlinx.collections.immutable.persistentListOf

fun StoryboardBuilder.AssertEqualsMessageExample() {
    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Complete failure message"),
        Conclusion.Con(text = "Forget to add message"),
        Conclusion.Con(text = "Maintenance burden"),
    )
    slideForExample(
        builder = {
            openOutput()
            minimizeOutput()
            repeat(conclusions.size) { addConclusion() }
        }
    ) {
        slideScope.TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeExcited(modifier = Modifier.requiredSize(200.dp))
                }
            }
        ) {
            Example(
                exampleText = AssertEqualsMessageCode,
                outputText = AssertEqualsMessageOutput,
                conclusions = conclusions,
            )
        }

        NotesTab("Notes") {
            Text("Finish by 3:00")
        }
    }
}

val AssertEqualsMessageCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assertEquals(9, members.size, "Members: ${'$'}members")
        }
    """.trimIndent().toExampleCode()

val AssertEqualsMessageOutput: String = """
    java.lang.AssertionError: Members: [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli] expected:<9> but was:<8>
        at [...]
""".trimIndent()
