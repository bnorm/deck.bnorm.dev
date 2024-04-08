package dev.bnorm.kc24.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.kc24.elements.GradleText
import dev.bnorm.kc24.elements.animateTo
import dev.bnorm.kc24.template.Example
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.kc24.template.slideForExample
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.animation.then
import dev.bnorm.librettist.rememberHighlighted
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import kotlinx.collections.immutable.ImmutableList

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
            Example(softAssertSetupWithoutMessage, gradleTextSequence, null)
        }
    }
}

fun ShowBuilder.SoftAssertExampleWithWarning() {
    // TODO better soft-assert example
    //  - iterate over the members and assert something common?

    // TODO start with output scrolled to the bottom and scroll up to the warning?

    slideForExample(
        builder = {
            openOutput()
        }
    ) {
        TitleAndBody {
            Example(softAssertExample, null, softAssertOutputWarning)
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
            Example(softAssertSetupSequence, null, null)
        }
    }
}

fun ShowBuilder.SoftAssertExample() {
    // TODO better soft-assert example

    slideForExample(
        builder = {
            openOutput()
        }
    ) {
        TitleAndBody {
            Example(softAssertExample, null, softAssertOutput)
        }
    }
}

private fun String.toSetupStyle(highlighting: Highlighting) = when (this) {
    "assert", "assertSoftly" -> highlighting.functionDeclaration
    "R" -> highlighting.typeParameters
    else -> null
}

private fun String.toSample(highlighting: Highlighting) = buildKotlinCodeString(
    text = this,
    codeStyle = highlighting,
    identifierType = { it.toSetupStyle(highlighting) }
)

private fun String.toExampleStyle(highlighting: Highlighting): SpanStyle? {
    return when (this) {
        "fellowshipOfTheRing", "name", "age", "race" -> highlighting.property
        "`test members of the fellowship`" -> highlighting.functionDeclaration
        "find", "any" -> highlighting.extensionFunctionCall
        "assertTrue", "assertEquals", "assertSoftly", "require" -> highlighting.staticFunctionCall
        else -> null
    }
}

// region assertSoftly Setup without message
val softAssertSetupWithoutMessage: AnnotatedString
    @Composable
    get() {
        return rememberHighlighted("softAssertSetupWithoutMessage") { highlighting ->
            // language=kotlin
            """
                package example                    
                
                fun <R> assertSoftly(block: AssertScope.() -> R): R
                
                interface AssertScope {
                    fun assert(assertion: Boolean)
                }
            """.trimIndent().toSample(highlighting)
        }
    }
// endregion

// region assertSoftly Setup without message
val softAssertSetupWithMessage: AnnotatedString
    @Composable
    get() {
        return rememberHighlighted("softAssertSetupWithMessage") { highlighting ->
            // language=kotlin
            """
                package example                    
                
                fun <R> assertSoftly(block: AssertScope.() -> R): R
                
                interface AssertScope {
                    fun assert(
                        assertion: Boolean, 
                        message: (() -> String)? = null,
                    )
                }
            """.trimIndent().toSample(highlighting)
        }
    }
// endregion


private val softAssertSetupSequence: ImmutableList<AnnotatedString>
    @Composable
    get() {
        val start = softAssertSetupWithoutMessage
        val end = softAssertSetupWithMessage
        return rememberHighlighted("softAssertSetupSequence") { highlighting ->
            startAnimation(start)
                .then(
                    // language=kotlin
                    """
                        package example                    
                        
                        fun <R> assertSoftly(block: AssertScope.() -> R): R
                        
                        interface AssertScope {
                            fun assert(
                                assertion: Boolean, 
                            )
                        }
                    """.trimIndent().toSample(highlighting)
                )
                .then(
                    // language=kotlin
                    """
                        package example                    
                        
                        fun <R> assertSoftly(block: AssertScope.() -> R): R
                        
                        interface AssertScope {
                            fun assert(
                                assertion: Boolean, 
                                
                            )
                        }
                    """.trimIndent().toSample(highlighting)
                )
                .thenLineEndDiff(end)
                .toList()
        }
    }

// region <assertSoftly Setup2>
val softAssertSetup2: AnnotatedString
    @Composable
    get() {
        return rememberHighlighted("softAssertSetup2") { highlighting ->
            buildKotlinCodeString(
                // language=kotlin
                text = """
                    package example
                    
                    fun assertSoftly(block: AssertScope.() -> Unit) {
                        val assertions = mutableListOf<Throwable>()
                        val scope = object : AssertScope {
                            override fun assert(assertion: Boolean, lazyMessage: (() -> Any)?) {
                                if (!assertion) {
                                    val message = lazyMessage?.invoke()?.toString()
                                    assertions.add(AssertionError(message))
                                }
                            }
                        }
                        scope.block()
                    
                        if (assertions.isNotEmpty()) {
                            throw AssertionError("Soft assertions").apply {
                                assertions.forEach(this::addSuppressed)
                            }
                        }
                    }

                """.trimIndent(),
                codeStyle = highlighting,
                identifierType = { it.toSetupStyle(highlighting) }
            )
        }
    }
// endregion

// region <assertSoftly Example>
val softAssertExample: AnnotatedString
    @Composable
    get() {
        return rememberHighlighted("softAssertExample") { highlighting ->
            buildKotlinCodeString(
                // language=kotlin
                text = """
                    @Test fun `test members of the fellowship`() {
                        val members = fellowshipOfTheRing.getCurrentMembers()
                        assertSoftly {
                            assert(members.find { it.name == "Frodo" }?.age == 23)
                            assert(members.find { it.name == "Aragorn" }?.age == 60)
                        }
                    }
                """.trimIndent(),
                codeStyle = highlighting,
                identifierType = { it.toExampleStyle(highlighting) }
            )
        }
    }
// endregion

// region assertSoftly Output without Power-Assert
private val softAssertOutputWarning = """
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
// endregion


// region assertSoftly Output with Power-Assert
private val softAssertOutput = """
Multiple failed assertions
java.lang.AssertionError: Multiple failed assertions
    at [...]
    Suppressed: java.lang.AssertionError: Assertion failed
assert(members.find { it.name == "Frodo" }?.age == 23)
       |       |                            |   |
       |       |                            |   false
       |       |                            50
       |       Character(name=Frodo, age=50)
       [Character(name=Frodo, age=50), Character(name=Sam, age=38), Character(name=Merry, age=36), Character(name=Pippin, age=28), Character(name=Gandalf, age=10000), Character(name=Aragorn, age=87), Character(name=Legolas, age=2931), Character(name=Gimli, age=139), Character(name=Boromir, age=40)]
        at [...]
    Suppressed: java.lang.AssertionError: Assertion failed
assert(members.find { it.name == "Aragorn" }?.age == 60)
       |       |                              |   |
       |       |                              |   false
       |       |                              87
       |       Character(name=Aragorn, age=87)
       [Character(name=Frodo, age=50), Character(name=Sam, age=38), Character(name=Merry, age=36), Character(name=Pippin, age=28), Character(name=Gandalf, age=10000), Character(name=Aragorn, age=87), Character(name=Legolas, age=2931), Character(name=Gimli, age=139), Character(name=Boromir, age=40)]
        at [...]
""".trimIndent()
// endregion
