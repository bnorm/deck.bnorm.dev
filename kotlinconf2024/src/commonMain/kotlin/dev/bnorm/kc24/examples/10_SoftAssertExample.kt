package dev.bnorm.kc24.examples

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.template.KodeeSad
import dev.bnorm.kc24.template.KodeeSurprised
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

    // TODO prefix the failure with a warning that we're going to see it fail
    //  - make clear that it is a compiler time warning

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
    slideForExample(
        builder = {
            openOutput()
        }
    ) {
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeSad(modifier = Modifier.requiredSize(150.dp))
                }
            }
        ) {
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
    slideForExample(
        builder = {
            openOutput()
        }
    ) {
        TitleAndBody(
            kodee = {
                transition.both(condition = { it.showOutput != OutputState.Hidden }) {
                    KodeeSurprised(modifier = Modifier.requiredSize(150.dp))
                }
            }
        ) {
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
                for (member in members) {
                    assert(member.age < 100)
                }
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
    Suppressed: java.lang.AssertionError
        at [...]
""".trimIndent()

private val SoftAssertOutput = """
Multiple failed assertions
java.lang.AssertionError: Multiple failed assertions
    at [...]
    Suppressed: java.lang.AssertionError: Assertion failed
assert(member.age < 100)
       |      |   |
       |      |   false
       |      10000
       Gandalf
        at [...]
    Suppressed: java.lang.AssertionError: Assertion failed
assert(member.age < 100)
       |      |   |
       |      |   false
       |      2931
       Legolas
        at [...]
    Suppressed: java.lang.AssertionError: Assertion failed
assert(member.age < 100)
       |      |   |
       |      |   false
       |      139
       Gimli
        at [...]
""".trimIndent()