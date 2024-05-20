package dev.bnorm.kc24.examples

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.template.KodeeExcited
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.AssertEqualsMessageExample() {
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
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeExcited(modifier = Modifier.requiredSize(200.dp))
                }
            }
        ) {
            Example(
                exampleTextSequence = persistentListOf(AssertEqualsMessageCode),
                outputTextSequence = persistentListOf(persistentListOf(AssertEqualsMessageOutput)),
                conclusions = conclusions,
            )
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
