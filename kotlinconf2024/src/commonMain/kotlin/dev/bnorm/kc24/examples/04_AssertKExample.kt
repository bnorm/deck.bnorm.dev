package dev.bnorm.kc24.examples

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.show.ShowBuilder
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.AssertKExample() {
    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Complete failure message"),
        Conclusion.Con(text = "Mental load for functions"),
        Conclusion.Con(text = "Assertions for custom types"),
        Conclusion.Con(text = "Library choice fatigue"),
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
                exampleTextSequence = persistentListOf(AssertKCode),
                outputTextSequence = persistentListOf(persistentListOf(AssertKOutput)),
                conclusions = conclusions,
            )
        }
    }
}

val AssertKCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assertThat(members).hasSize(9)
        }
    """.trimIndent().toExampleCode()

val AssertKOutput: String = """
    org.opentest4j.AssertionFailedError: expected [size]:<[9]> but was:<[8]> ([Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli])
        at [...]
""".trimIndent()
