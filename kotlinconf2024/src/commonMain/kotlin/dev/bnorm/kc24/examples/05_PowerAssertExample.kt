package dev.bnorm.kc24.examples

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.show.assist.ShowAssistTab
import dev.bnorm.librettist.text.thenLines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

fun ExampleBuilder.SimpleAssertExample() {
    example(
        builder = {
            openOutput()
            openGradle()
            updateGradle()
            closeGradle()
            updateOutput()
        },
        kodee = { transition ->
            transition.both(condition = { it.outputIndex >= 1 && it.showOutput == OutputState.Visible }) {
                KodeeLoving(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
            }

            transition.either(condition = { it.gradleIndex >= 1 && it.outputIndex < 1 }) {
                KodeeSurprised(modifier = Modifier.requiredSize(150.dp))
            }

            transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                KodeeSad(modifier = Modifier.requiredSize(150.dp))
            }
        },
    ) {
        val gradleTextSequence = persistentListOf(GradleText.Initial.animateTo(GradleText.AddPlugin))
        Example(
            exampleTextSequence = persistentListOf(SimpleAssertCode),
            gradleTextSequence = gradleTextSequence,
            outputTextSequence = persistentListOf(SimpleAssertOutput),
        )

        ShowAssistTab("Notes") {
            Text("Finish by 5:00")
        }
    }
}

val SimpleAssertCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assert(members.size == 9)
        }
    """.trimIndent().toExampleCode { highlighting, it ->
        when (it) {
            "assert" -> highlighting.staticFunctionCall
            else -> null
        }
    }

val SimpleAssertOutput: ImmutableList<String> = startAnimation(
    """
        java.lang.AssertionError: Assertion failed
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(members.size == 9)
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(members.size == 9)
                            |
                            false
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(members.size == 9)
                       |    |
                       |    false
                       8
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(members.size == 9)
               |       |    |
               |       |    false
               |       8
               [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
    """.trimIndent(),
).toList()
