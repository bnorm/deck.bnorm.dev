package dev.bnorm.kc24.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.ComplexExpressionsExample() {
    slideForExample(
        builder = {
            openOutput()
        }
    ) {
        TitleAndBody {
            Example(
                exampleTextSequence = persistentListOf(ComplexAssertCode),
                outputTextSequence = persistentListOf(persistentListOf(ComplexAssertOutput)),
            )
        }
    }
}

val ComplexAssertCode: AnnotatedString
    @Composable get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assert(members.any { it.name == "Boromir" } &&
                    members.any { it.name == "Aragorn" } ||
                    members.any { it.name == "Elrond" })
        }
    """.trimIndent().toExampleCode { highlighting, it ->
        when (it) {
            "assert" -> highlighting.staticFunctionCall
            else -> null
        }
    }

val ComplexAssertOutput = """
java.lang.AssertionError: Assertion failed
assert(members.any { it.name == "Boromir" } &&
       |       |
       |       false
       [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
        members.any { it.name == "Aragorn" } ||
        members.any { it.name == "Elrond" })
        |       |
        |       false
        [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
    at [...]
""".trimIndent()
