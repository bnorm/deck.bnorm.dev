package dev.bnorm.kc24.examples

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.KodeeBrokenHearted
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.elements.defaultSpec
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.notes.NotesTab
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration.Companion.milliseconds

fun StoryboardBuilder.BasicAssertTrueExample() {
    val conclusions = persistentListOf(
        Conclusion.Pro(text = "Clear assertion condition"),
        Conclusion.Con(text = "Useless failure message"),
    )
    slideForExample(
        builder = {
            openOutput()
            minimizeOutput()
            repeat(conclusions.size) { addConclusion() }
        },
        enterTransition = { direction ->
            when (direction) {
                AdvanceDirection.Forward -> slideInHorizontally(defaultSpec(750.milliseconds)) { it }
                AdvanceDirection.Backward -> EnterTransition.None
            }
        },
        exitTransition = { direction ->
            when (direction) {
                AdvanceDirection.Forward -> ExitTransition.None
                AdvanceDirection.Backward -> slideOutHorizontally(defaultSpec(750.milliseconds)) { it }
            }
        },
    ) {
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeBrokenHearted(modifier = Modifier.requiredSize(200.dp))
                }
            }
        ) {
            Example(
                exampleText = BasicAssertTrueCode,
                gradleText = null,
                outputText = BasicAssertTrueOutput,
                conclusions = conclusions,
            )
        }

        NotesTab("Notes") {
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
