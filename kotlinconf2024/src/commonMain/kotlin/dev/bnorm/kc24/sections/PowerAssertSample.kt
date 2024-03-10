package dev.bnorm.kc24.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.MacWindow
import dev.bnorm.kc24.template.DefaultCornerKodee
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.LocalShowTheme
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.rememberAdvancementBoolean
import dev.bnorm.librettist.text.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

fun ShowBuilder.PowerAssertSample() {
    TextLinesAnimationSample()
}

@OptIn(ExperimentalResourceApi::class)
fun ShowBuilder.TextLinesAnimationSample() {
    section(title = { Text("No Power-Assert") }) {
        slide { SectionHeader() }
        slide {
            TitleAndBody {
                Text(rememberExampleCodeString(powerAssertSample))
            }
        }
        slide {
            val kodeeSad by rememberAdvancementBoolean()
            TitleAndBody(
                kodee = {
                    if (kodeeSad) {
                        Image(
                            painter = painterResource(DrawableResource("kotlin_mascot/emoji/kodee-broken-hearted.png")),
                            contentDescription = "",
                            modifier = Modifier.padding(8.dp).requiredSize(200.dp),
                        )
                    } else {
                        DefaultCornerKodee()
                    }
                }
            ) {
                Text(rememberExampleCodeString(powerAssertSample))
                Spacer(Modifier.padding(top = 16.dp))
                MacWindow {
                    Text("java.lang.AssertionError: Assertion failed")
                }
            }
        }
    }
    section(title = { Text("With Power-Assert") }) {
        slide { SectionHeader() }
        slide {
            TitleAndBody {
                Text(rememberExampleCodeString(powerAssertSample))
            }
        }
        slide {
            val state = rememberAdvancementAnimation()
            val kodeeHappy by rememberAdvancementBoolean()

            TitleAndBody(
                kodee = {
                    if (kodeeHappy) {
                        Image(
                            painter = painterResource(DrawableResource("kotlin_mascot/emoji/kodee-loving.png")),
                            contentDescription = "",
                            modifier = Modifier.padding(8.dp).requiredSize(200.dp).graphicsLayer { rotationY = 180f },
                        )
                    } else {
                        DefaultCornerKodee()
                    }
                }
            ) {
                Text(rememberExampleCodeString(powerAssertSample))
                Spacer(Modifier.padding(top = 16.dp))
                AnimateSequence(powerAssertOutput, state) { text: String ->
                    MacWindow {
                        Text(text)
                    }
                }
            }
        }
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
                    "length" -> SpanStyle(color = Color(0xFFC77DBB))

                    // Function definitions
                    "main" -> SpanStyle(color = Color(0xFF56A8F5))

                    else -> null
                }
            }
        )
    }
}

val powerAssertSample =
    """
        fun main() {
            assert(hello.length == "World".substring(1, 4).length)
        }
    """.trimIndent()

val powerAssertOutput = startAnimation(
    """
        java.lang.AssertionError: Assertion failed
        assert(hello.length == "World".substring(1, 4).length)
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(hello.length == "World".substring(1, 4).length)
                                                       |
                                                       3
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(hello.length == "World".substring(1, 4).length)
                                       |               |
                                       |               3
                                       orl
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(hello.length == "World".substring(1, 4).length)
                            |          |               |
                            |          |               3
                            |          orl
                            false
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(hello.length == "World".substring(1, 4).length)
                     |      |          |               |
                     |      |          |               3
                     |      |          orl
                     |      false
                     5
    """.trimIndent(),
).thenLines(
    """
        java.lang.AssertionError: Assertion failed
        assert(hello.length == "World".substring(1, 4).length)
               |     |      |          |               |
               |     |      |          |               3
               |     |      |          orl
               |     |      false
               |     5
               Hello
    """.trimIndent(),
)
