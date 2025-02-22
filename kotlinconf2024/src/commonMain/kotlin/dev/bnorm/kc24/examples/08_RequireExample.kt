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

fun StoryboardBuilder.RequireExample() {
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
        slideScope.TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeSurprised(modifier = Modifier.requiredSize(150.dp))
                }
            }
        ) {
            val gradleTextSequence = persistentListOf(
                GradleText.AddAssertTrue.animateTo(GradleText.AddRequire),
                GradleText.AddRequire.animateTo(GradleText.AddSourceSet),
            )
            Example(
                exampleText = RequireCode,
                gradleTextSequence = gradleTextSequence,
                outputTextSequence = persistentListOf(persistentListOf(RequireOutput)),
                conclusions = null,
            )

            NotesTab("Notes") {
                Text("Finish by 8:00")
            }
        }
    }
}

// TODO
//  - surround example with a little more context?
val RequireCode: AnnotatedString
    @Composable get() = """
        fun addToFellowship(character: Character) {
            require(character.alive)
            members.add(character)
        }
    """.trimIndent().toExampleCode { highlighting, it ->
        when (it) {
            "members" -> highlighting.property
            else -> null
        }
    }

// TODO
//  - should "Assertion failed" as the prefix also be removed?
//  - should we show a link to tickets if these things doesn't get fixed?
val RequireOutput = """
java.lang.IllegalArgumentException: Assertion failed
require(character.alive)
        |         |
        |         false
        Boromir
    at [...]
""".trimIndent()
