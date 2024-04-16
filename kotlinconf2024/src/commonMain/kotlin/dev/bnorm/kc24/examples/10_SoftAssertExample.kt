package dev.bnorm.kc24.examples

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.animation.then
import dev.bnorm.librettist.rememberHighlighted
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

fun ShowBuilder.SoftAssertExample() {
    SoftAssertSetupWithoutMessage()
    ExampleCarousel(
        start = { Example(SoftAssertWithoutMessageSetup) },
        end = { Example(SoftAssertCode) }
    )
    SoftAssertExampleWithWarning()
    ExampleCarousel(
        forward = false,
        start = { Example(SoftAssertCode) },
        end = { Example(SoftAssertWithoutMessageSetup) }
    )
    SoftAssertSetupWithMessage()
    ExampleCarousel(
        start = { Example(SoftAssertWithMessageSetup) },
        end = { Example(SoftAssertCode) }
    )
    SoftAssertWithMessageExample()
}

fun ShowBuilder.SoftAssertSetupWithoutMessage() {
    // TODO show what the implementation looks like?

    slideForExample(
        builder = {
            openGradle()
            updateGradle()
            closeGradle()
        }
    ) {
        TitleAndBody {
            val gradleTextSequence = GradleText.AddAssertNotNull.animateTo(GradleText.AddAssertSoftly)
            Example(
                exampleTextSequence = persistentListOf(SoftAssertWithoutMessageSetup),
                gradleTextSequence = persistentListOf(gradleTextSequence),
            )
        }
    }
}

fun ShowBuilder.SoftAssertExampleWithWarning() {
    // TODO better soft-assert example
    //  - iterate over the members and assert something common?

    slideForExample(
        builder = {
            openOutput()
        }
    ) {
        TitleAndBody {
            Example(
                exampleTextSequence = persistentListOf(SoftAssertCode),
                outputTextSequence = persistentListOf(persistentListOf(SoftAssertOutputWarning))
            )
        }
    }
}

fun ShowBuilder.SoftAssertSetupWithMessage() {
    slideForExample(
        builder = {
            updateExample()
        }
    ) {
        TitleAndBody {
            Example(
                exampleTextSequence = SoftAssertSetupSequence,
            )
        }
    }
}

fun ShowBuilder.SoftAssertWithMessageExample() {
    // TODO better soft-assert example

    slideForExample(
        builder = {
            openOutput()
        }
    ) {
        TitleAndBody {
            Example(
                exampleTextSequence = persistentListOf(SoftAssertCode),
                outputTextSequence = persistentListOf(persistentListOf(SoftAssertOutput)),
            )
        }
    }
}

@Composable
private fun String.toSetupCode(): AnnotatedString {
    return rememberHighlighted(this) { highlighting ->
        buildKotlinCodeString(
            text = this,
            codeStyle = highlighting,
            identifierType = {
                when (it) {
                    "assert", "assertSoftly" -> highlighting.functionDeclaration
                    "R" -> highlighting.typeParameters
                    else -> null
                }
            }
        )
    }
}

val SoftAssertWithoutMessageSetup: AnnotatedString
    @Composable get() = """
        package example                    
        
        fun <R> assertSoftly(block: AssertScope.() -> R): R
        
        interface AssertScope {
            fun assert(assertion: Boolean)
        }
    """.trimIndent().toSetupCode()

val SoftAssertWithMessageSetup: AnnotatedString
    @Composable get() = """
        package example                    
        
        fun <R> assertSoftly(block: AssertScope.() -> R): R
        
        interface AssertScope {
            fun assert(
                assertion: Boolean, 
                message: (() -> String)? = null,
            )
        }
    """.trimIndent().toSetupCode()

private val SoftAssertSetupSequence: ImmutableList<AnnotatedString>
    @Composable
    get() {
        val start = SoftAssertWithoutMessageSetup
        val middle1 = """
            package example                    
            
            fun <R> assertSoftly(block: AssertScope.() -> R): R
            
            interface AssertScope {
                fun assert(
                    assertion: Boolean, 
                )
            }
        """.trimIndent().toSetupCode()
        val middle2 = """
            package example                    
            
            fun <R> assertSoftly(block: AssertScope.() -> R): R
            
            interface AssertScope {
                fun assert(
                    assertion: Boolean, 
                    
                )
            }
        """.trimIndent().toSetupCode()
        val end = SoftAssertWithMessageSetup
        return remember {
            startAnimation(start).then(middle1).then(middle2).thenLineEndDiff(end).toList()
        }
    }

val SoftAssertCode: AnnotatedString
    @Composable
    get() = """
        @Test fun `test members of the fellowship`() {
            val members = fellowshipOfTheRing.getCurrentMembers()
            assertSoftly {
                assert(members.find { it.name == "Frodo" }?.age == 23)
                assert(members.find { it.name == "Aragorn" }?.age == 60)
            }
        }
    """.trimIndent().toExampleCode()

private val SoftAssertOutputWarning = """
w: file://[...] Unable to find overload of function example.AssertScope.assert for power-assert transformation callable as:
 - example.AssertScope.assert(String)
 - example.AssertScope.assert(() -> String)
 - example.AssertScope.assert(kotlin.Boolean, String)
 - example.AssertScope.assert(kotlin.Boolean, () -> String)

Multiple failed assertions
java.lang.AssertionError: Multiple failed assertions
    at [...]
    Suppressed: java.lang.AssertionError
        at [...]
    Suppressed: java.lang.AssertionError
        at [...]
""".trimIndent()

private val SoftAssertOutput = """
Multiple failed assertions
java.lang.AssertionError: Multiple failed assertions
    at [...]
    Suppressed: java.lang.AssertionError: Assertion failed
assert(members.find { it.name == "Frodo" }?.age == 23)
       |       |                            |   |
       |       |                            |   false
       |       |                            50
       |       Frodo
       [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
        at [...]
    Suppressed: java.lang.AssertionError: Assertion failed
assert(members.find { it.name == "Aragorn" }?.age == 60)
       |       |                              |   |
       |       |                              |   false
       |       |                              87
       |       Aragorn
       [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli]
        at [...]
""".trimIndent()
