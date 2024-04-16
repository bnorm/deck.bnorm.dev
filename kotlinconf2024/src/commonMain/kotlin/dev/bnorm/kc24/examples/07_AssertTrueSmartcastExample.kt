package dev.bnorm.kc24.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.AssertTrueSmartcastExample() {
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
                GradleText.AddPlugin.animateTo(GradleText.AddConfig),
                GradleText.AddConfig.animateTo(GradleText.AddAssertTrue),
            )
            Example(
                exampleTextSequence = persistentListOf(AssertTrueSmartcastCode),
                gradleTextSequence = gradleTextSequence,
                outputTextSequence = persistentListOf(persistentListOf(AssertTrueSmartcastOutput)),
            )
        }
    }
}

val AssertTrueSmartcastCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            val aragorn = members.find { it.name == "Aragorn" }
            assertTrue(aragorn != null)
            assertTrue(aragorn.age < 60)
        }
    """.trimIndent().toExampleCode()

val AssertTrueSmartcastOutput = """
java.lang.AssertionError: Assertion failed
assertTrue(aragorn.age < 60)
           |       |   |
           |       |   false
           |       87
           Aragorn
    at [...]
""".trimIndent()