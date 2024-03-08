package dev.bnorm.kc24.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.MacWindow
import dev.bnorm.kc24.template.DefaultCornerKodee
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
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
                KotlinCodeText(powerAssertSample)
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
                KotlinCodeText(powerAssertSample)
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
                KotlinCodeText(powerAssertSample)
            }
        }
        slide {
            val kodeeHappy by rememberAdvancementBoolean()
            val state = rememberAdvancementAnimation()

            TitleAndBody(
                kodee = {
                    if (kodeeHappy) {
                        Image(
                            painter = painterResource(DrawableResource("kotlin_mascot/emoji/kodee-loving.png")),
                            contentDescription = "",
                            modifier = Modifier.padding(8.dp).requiredSize(200.dp),
                        )
                    } else {
                        DefaultCornerKodee()
                    }
                }
            ) {
                KotlinCodeText(
                    """
                        fun main() {
                            assert(hello.length == "World".substring(1, 4).length)
                        }
                    """.trimIndent()
                )
                Spacer(Modifier.padding(top = 16.dp))
                AnimateText(powerAssertOutput, state) { text: String ->
                    MacWindow {
                        Text(text)
                    }
                }
            }
        }
    }
}

val powerAssertSample =
    """
        fun main() {
            assert(hello.length == "World".substring(1, 4).length)
        }
    """.trimIndent()

val powerAssertOutput = startTextAnimation(
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
