package dev.bnorm.kc24.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.RequireExample() {
    slideForExample(
        builder = {
            openGradle()
            updateGradle()
            updateGradle()
            closeGradle()
            openOutput()
        }
    ) {
        TitleAndBody {
            val gradleTextSequence = persistentListOf(
                GradleText.AddAssertTrue.animateTo(GradleText.AddRequire),
                GradleText.AddRequire.animateTo(GradleText.AddSourceSet),
            )
            Example(
                exampleTextSequence = persistentListOf(RequireCode),
                gradleTextSequence = gradleTextSequence,
                outputTextSequence = persistentListOf(persistentListOf(RequireOutput)),
            )
        }
    }
}

// TODO
//  - surround example with a little more context?
//  - is there a better example than this?
val RequireCode: AnnotatedString
    @Composable get() = """
        operator fun get(index: Int): Character {
            require(index in members.indices)
            return members[index]
        }
    """.trimIndent().toExampleCode { highlighting, it ->
        it.toExampleStyle(highlighting) ?: when (it) {
            "members" -> highlighting.property
            else -> null
        }
    }

// TODO !!! THIS IS DOCTORED OUTPUT !!!
//  - `members` prints out "FellowshipOfTheRing" as well because it is a receiver
//  - can/should we ignore implicit receivers?
//  - should "Assertion failed" as the prefix also be removed?
//  - should we show a link to tickets if these things doesn't get fixed?
val RequireOutput = """
java.lang.IllegalArgumentException: Assertion failed
require(index in members.indices)
        |     |  |       |
        |     |  |       0..8
        |     |  [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli, Boromir]
        |     false
        10
    at [...]
""".trimIndent()
