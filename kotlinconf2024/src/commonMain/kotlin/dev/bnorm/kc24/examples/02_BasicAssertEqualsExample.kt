package dev.bnorm.kc24.examples

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.show.ShowBuilder
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.BasicAssertEqualsExample() {
    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Improved failure message"),
        Conclusion.Con(text = "No intermediate values"),
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
                    KodeeLost(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
                }
            }
        ) {
            Example(
                exampleTextSequence = persistentListOf(BasicAssertEqualsCode),
                outputTextSequence = persistentListOf(persistentListOf(BasicAssertEqualsOutput)),
                conclusions = conclusions,
            )
        }
    }
}

val BasicAssertEqualsCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assertEquals(9, members.size)
        }
    """.trimIndent().toExampleCode()

val BasicAssertEqualsOutput: String = """
    java.lang.AssertionError: expected:<9> but was:<8>
        at [...]
""".trimIndent()
