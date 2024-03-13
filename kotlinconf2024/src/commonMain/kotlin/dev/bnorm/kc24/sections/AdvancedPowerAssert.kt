package dev.bnorm.kc24.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.MacWindow
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.LocalShowTheme
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.rememberAdvancementBoolean
import dev.bnorm.librettist.text.buildKotlinCodeString

fun ShowBuilder.AdvancedPowerAssert() {
    section(title = { Text("Advanced Power-Assert") }) {
        slide { SectionHeader() }

        // TODO extra use cases like
        //  1. advanced expressions (chaining, &&/||, multi-line)
        //  2. soft-assert
        //  3. logging?

        slide {
            TitleAndBody {
                val showOutput by rememberAdvancementBoolean()

                Box(modifier = Modifier.padding(SLIDE_PADDING)) {
                    Text(rememberExampleCodeString(softAssertExample))
                }
                Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)) {
                    AnimatedVisibility(
                        visible = showOutput,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it },
                    ) {
                        MacWindow(modifier = Modifier.background(MaterialTheme.colors.background)) {
                            Text(
                                text = softAssertOutput,
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp).wrapContentWidth(Alignment.Start, unbounded = true),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberSetupCodeString(text: String): AnnotatedString {
    val codeStyle = LocalShowTheme.current.code
    return remember(text) {
        buildKotlinCodeString(
            text, codeStyle,
            identifierType = {
                when (it) {
                    // Function declarations
                    "assert", "assertSoftly",
                    -> SpanStyle(color = Color(0xFF56A8F5))

                    // Type parameter
                    "R",
                    -> SpanStyle(color = Color(0xFF16BAAC))

                    else -> null
                }
            }
        )
    }
}

@Composable
private fun rememberExampleCodeString(text: String): AnnotatedString {
    val codeStyle = LocalShowTheme.current.code
    return remember(text) {
        buildKotlinCodeString(
            text, codeStyle,
            identifierType = {
                when (it) {
                    // Properties
                    "fellowshipOfTheRing", "age", "name",
                    -> SpanStyle(color = Color(0xFFC77DBB))

                    // Function declarations
                    "`test members of the fellowship`",
                    -> SpanStyle(color = Color(0xFF56A8F5))

                    // Extension functions
                    "find",
                    -> SpanStyle(fontStyle = FontStyle.Italic, color = Color(0xFF56A8F5))

                    // Top-level functions
                    "assertSoftly",
                    -> SpanStyle(fontStyle = FontStyle.Italic)

                    else -> null
                }
            }
        )
    }
}

// language=kotlin
private val softAssertSetup = """
    typealias LazyMessage = () -> Any

    interface AssertScope {
        fun assert(assertion: Boolean, lazyMessage: LazyMessage? = null)
    }

    fun <R> assertSoftly(block: AssertScope.() -> R): R
""".trimIndent()

// language=kotlin
private val softAssertExample = """
    @Test
    fun `test members of the fellowship`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertSoftly {
            assert(members.find { it.name == "Frodo" }?.age == 23)
            assert(members.find { it.name == "Aragorn" }?.age == 60)
        }
    }
""".trimIndent()

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
