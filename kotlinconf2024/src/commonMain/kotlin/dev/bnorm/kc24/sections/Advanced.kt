package dev.bnorm.kc24.sections

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.kc24.elements.AnimatedVisibility
import dev.bnorm.kc24.elements.defaultSpec
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.animation.then
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.toInt
import dev.bnorm.librettist.text.buildGradleKtsCodeString
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import kotlinx.collections.immutable.ImmutableList

fun ShowBuilder.Advanced() {
    // TODO
    //  - this as a whole separate section doesn't make a lot of sense
    //  - could easily make it an aside in the examples section after the `assertEquals` example

    FunctionSignature()
    ExampleCarousel(
        start = {
            updateTransition(6).FunctionSignature()
        },
        end = {
            Box(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                Text(softAssertSetup)
            }
        }
    )
    SoftAssertSetup()
    ExampleCarousel { softAssertSetup to softAssertExample }
    SoftAssertExample()
}

private fun ShowBuilder.FunctionSignature() {
    slide(states = 7) {
        val state = transition.createChildTransition { it.toInt() }
        TitleAndBody {
            state.FunctionSignature()
        }
    }
}

@Composable
private fun Transition<out Int>.FunctionSignature() {
    // TODO explain function signature requirements
    // Start with: assertCustom(value: CustomClass)
    //
    // Looks for:
    // - assertCustom(value: CustomClass, message: String)
    // - assertCustom(value: CustomClass, message: () -> String)
    // - assertCustom(message: String)
    // - assertCustom(message: () -> String)

    Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
        AnimateByLine(
            { Text("When a call is made to the following function:") },
            { Text("fun assertCustom(value: CustomClass)") },

            { Text("Power-Assert will look for the following functions to call instead:") },
            { Text("assertCustom(value: CustomClass, message: String)") },
            { Text("assertCustom(value: CustomClass, message: () -> String)") },
            { Text("assertCustom(message: String)") },
            { Text("assertCustom(message: () -> String)") },
        )
    }
}

private fun ShowBuilder.SoftAssertSetup() {
    // TODO show what the implementation looks like?

    slideForExample(
        builder = {
            openGradle()
            updateGradle()
            closeGradle()
        }
    ) {
        TitleAndBody {
            Example(softAssertSetup, softAsserGradleSequence)
        }
    }
}

private fun ShowBuilder.SoftAssertExample() {
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

@Composable
private fun Transition<out Int>.AnimateByLine(
    vararg lines: @Composable () -> Unit,
) {
    if (lines.isEmpty()) return

    for ((i, line) in lines.withIndex()) {
        createChildTransition { it >= i }.AnimatedVisibility(
            enter = fadeIn(defaultSpec()) + expandVertically(defaultSpec()),
            exit = fadeOut(defaultSpec()) + shrinkVertically(defaultSpec()),
        ) {
            line()
        }
    }
}

private fun String.toSetupStyle(codeStyle: Highlighting) = when (this) {
    "assert", "assertSoftly" -> codeStyle.functionDeclaration
    "R" -> codeStyle.typeParameters
    else -> null
}

private fun String.toExampleStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "fellowshipOfTheRing", "name", "age", "race" -> codeStyle.property
        "`test members of the fellowship`" -> codeStyle.functionDeclaration
        "find", "any" -> codeStyle.extensionFunctionCall
        "assertTrue", "assertEquals", "assertSoftly", "require" -> codeStyle.staticFunctionCall
        else -> null
    }
}

private fun String.toGradleKtsStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "class" -> codeStyle.keyword
        "ExperimentalKotlinGradlePluginApi" -> codeStyle.annotation
        "functions", "includedSourceSets" -> codeStyle.property
        "kotlin", "version", "powerAssert" -> codeStyle.extensionFunctionCall
        else -> null
    }
}

// region <assertSoftly Setup>
private val softAssertSetup: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            buildKotlinCodeString(
                // language=kotlin
                text = """
                    package example                    
                    
                    fun <R> assertSoftly(block: AssertScope.() -> R): R
                    
                    interface AssertScope {
                        fun assert(
                            assertion: Boolean, 
                            lazyMessage: (() -> String)? = null,
                        )
                    }
                """.trimIndent(),
                codeStyle = codeStyle,
                identifierType = { it.toSetupStyle(codeStyle) }
            )
        }
    }
// endregion

// region <assertSoftly Setup2>
private val softAssertSetup2: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
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
                codeStyle = codeStyle,
                identifierType = { it.toSetupStyle(codeStyle) }
            )
        }
    }
// endregion

// region <assertSoftly Gradle>
private val softAsserGradleSequence: ImmutableList<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        fun build(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { it.toGradleKtsStyle(codeStyle) }
        )

        return remember {
            startAnimation(
                build(
                    text = """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                            kotlin("plugin.power-assert") version "2.0.0"
                        }
                        
                        @OptIn(ExperimentalKotlinGradlePluginApi::class)
                        powerAssert {
                            functions.addAll(
                                "kotlin.require",
                                "kotlin.test.assertTrue",
                                "kotlin.test.assertEquals",
                            )
                            includedSourceSets.addAll("main", "test")
                        }
                    """.trimIndent() + "\n",
                )
            ).then(
                build(
                    text = """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                            kotlin("plugin.power-assert") version "2.0.0"
                        }
                        
                        @OptIn(ExperimentalKotlinGradlePluginApi::class)
                        powerAssert {
                            functions.addAll(
                                "kotlin.require",
                                "kotlin.test.assertTrue",
                                "kotlin.test.assertEquals",

                            )
                            includedSourceSets.addAll("main", "test")
                        }
                    """.trimIndent(),
                )
            ).thenLineEndDiff(
                build(
                    text = """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                            kotlin("plugin.power-assert") version "2.0.0"
                        }
                        
                        @OptIn(ExperimentalKotlinGradlePluginApi::class)
                        powerAssert {
                            functions.addAll(
                                "kotlin.require",
                                "kotlin.test.assertTrue",
                                "kotlin.test.assertEquals",
                                "example.AssertScope.assert",
                            )
                            includedSourceSets.addAll("main", "test")
                        }
                    """.trimIndent(),
                )
            ).toList()
        }
    }
// endregion

// region <assertSoftly Example>
private val softAssertExample: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
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
                codeStyle = codeStyle,
                identifierType = { it.toExampleStyle(codeStyle) }
            )
        }
    }
// endregion

// region <assertSoftly Output>
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
