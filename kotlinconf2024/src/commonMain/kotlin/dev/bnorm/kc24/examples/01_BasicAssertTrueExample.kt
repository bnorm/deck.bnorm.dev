package dev.bnorm.kc24.examples

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.assist.ShowAssistTab
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.BasicAssertTrueExample() {
    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Clear assertion condition"),
        Conclusion.Con(text = "Useless failure message"),
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
                exampleTextSequence = persistentListOf(BasicAssertTrueCode),
                outputTextSequence = persistentListOf(persistentListOf(BasicAssertTrueOutput)),
                conclusions = conclusions,
            )
        }

        ShowAssistTab("Notes") {
            Text("Finish by 1:00")
        }
    }
}

val BasicAssertTrueCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assertTrue(members.size == 9)
        }
    """.trimIndent().toExampleCode()

val BasicAssertTrueOutput: String = """
    java.lang.AssertionError: Expected value to be true.
        at [...]
""".trimIndent()
