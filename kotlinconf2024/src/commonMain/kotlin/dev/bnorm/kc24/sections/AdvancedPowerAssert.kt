package dev.bnorm.kc24.sections

import androidx.compose.animation.core.Transition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.AnimatedVisibility
import dev.bnorm.kc24.elements.MacTerminal
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.slideForBoolean
import dev.bnorm.librettist.text.buildGradleKtsCodeString
import dev.bnorm.librettist.text.buildKotlinCodeString

fun ShowBuilder.AdvancedPowerAssert() {
    ComplexExpressions()
    SoftAssert()

    // TODO extra use cases like
    //  3. logging? (dbg)
}

private fun ShowBuilder.ComplexExpressions() {
    slideForBoolean {
        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    Box(modifier = Modifier.padding(SLIDE_PADDING)) {
                        Text(complexAssertExample)
                    }
                    OutputText(complexAssertOutput, transition, modifier = Modifier.align(Alignment.BottomStart))
                }
            }
        }
    }
}

private fun ShowBuilder.SoftAssert() {
    slideForBoolean {
        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    Box(modifier = Modifier.padding(SLIDE_PADDING)) {
                        Text(softAssertSetup)
                    }

                    SidePanel(
                        visible = transition,
                        modifier = Modifier.align(Alignment.TopEnd).requiredWidth(1500.dp),
                    ) {
                        Text(softAsserGradle)
                    }
                }
            }
        }
    }

    slideForBoolean {
        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    Box(modifier = Modifier.padding(SLIDE_PADDING)) {
                        Text(softAssertExample)
                    }

                    OutputText(softAssertOutput, transition, modifier = Modifier.align(Alignment.BottomStart))
                }
            }
        }
    }
}

@Composable
private fun OutputText(text: String, visible: Transition<Boolean>, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        visible.AnimatedVisibility(
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
        ) {
            MacTerminal(modifier = Modifier.heightIn(min = 800.dp)) {
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 32.dp, top = 32.dp)
                        .wrapContentWidth(Alignment.Start, unbounded = true),
                )
            }
        }
    }
}

@Composable
private fun SidePanel(visible: Transition<Boolean>, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxHeight()) {
        visible.AnimatedVisibility(
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it },
        ) {
            Row {
                Spacer(modifier = Modifier.background(Color(0xFF313438)).width(4.dp).fillMaxHeight())
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface)
                        .padding(start = SLIDE_PADDING, top = SLIDE_PADDING)
                        .wrapContentWidth(Alignment.Start, unbounded = true)
                ) {
                    content()
                }
            }
        }
    }
}

// ======================= //
// ===== Text Values ===== //
// ======================= //

private fun String.toSetupStyle(codeStyle: Highlighting) = when (this) {
    "assert", "assertSoftly" -> codeStyle.functionDeclaration
    "R" -> codeStyle.typeParameters
    else -> null
}

private fun String.toExampleStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "fellowshipOfTheRing", "age", "name" -> codeStyle.property
        "`test members of the fellowship`" -> codeStyle.functionDeclaration
        "find", "any" -> codeStyle.extensionFunctionCall
        "assertSoftly" -> codeStyle.staticFunctionCall
        else -> null
    }
}

private fun String.toExtendedExampleStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "assert" -> codeStyle.staticFunctionCall
        else -> null
    }
}

private fun String.toGradleKtsStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "class" -> codeStyle.keyword
        "ExperimentalKotlinGradlePluginApi" -> codeStyle.annotation
        "functions" -> codeStyle.property
        "powerAssert" -> codeStyle.extensionFunctionCall
        else -> null
    }
}

// language=kotlin
private val complexAssertExample: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            buildKotlinCodeString(
                text = """
                    @Test
                    fun `test members of the fellowship`() {
                        val members = fellowshipOfTheRing.getCurrentMembers()
                        assert(members.any { it.name == "Boromir" } &&
                                members.any { it.name == "Aragorn" } ||
                                members.any { it.name == "Elrond" })
                    }
                """.trimIndent(),
                codeStyle = codeStyle,
                identifierType = { it.toExampleStyle(codeStyle) ?: it.toExtendedExampleStyle(codeStyle) }
            )
        }
    }

private val complexAssertOutput = """
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

// language=kotlin
private val softAssertSetup: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            buildKotlinCodeString(
                text = """
                    package com.example    
                
                    typealias LazyMessage = () -> Any
                
                    interface AssertScope {
                        fun assert(assertion: Boolean, lazyMessage: LazyMessage? = null)
                    }
                
                    fun <R> assertSoftly(block: AssertScope.() -> R): R
                """.trimIndent(),
                codeStyle = codeStyle,
                identifierType = { it.toSetupStyle(codeStyle) }
            )
        }
    }

// language=kotlin
private val softAsserGradle: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            buildGradleKtsCodeString(
                text = """
                    @OptIn(ExperimentalKotlinGradlePluginApi::class)
                    powerAssert {
                        functions.addAll(
                            "com.example.AssertScope.assert",
                        )
                    }
                """.trimIndent(),
                codeStyle = codeStyle,
                identifierType = { it.toGradleKtsStyle(codeStyle) }
            )
        }
    }

// language=kotlin
private val softAssertExample: AnnotatedString
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        return remember {
            buildKotlinCodeString(
                text = """
                    @Test
                    fun `test members of the fellowship`() {
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
