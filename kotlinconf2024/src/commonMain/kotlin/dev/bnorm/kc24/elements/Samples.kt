package dev.bnorm.kc24.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.animation.AnimationState
import dev.bnorm.librettist.animation.LaunchedAnimation
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.show.rememberAdvancementBoolean
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.text.KotlinCodeText
import dev.bnorm.librettist.text.flowDiff
import dev.bnorm.librettist.text.flowLines
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun ShowBuilder.Samples() {
    section(title = { Text("Samples") }) {
        slide { SectionHeader() }
        TextLinesAnimationSample()
        slide { TextAnimationSample() }
        slide { VisibilitySample() }
    }
}

@Composable
fun SlideScope.TextAnimationSample() {
    val values = listOf(
        """
            fun main() {
                println("Hello, World!")
            }
        """.trimIndent(),
        """
            fun main() {
                println("Hello, !")
            }
        """.trimIndent(),
        """
            fun main() {
                println("Hello, Power-Assert!")
            }
        """.trimIndent(),
        """
            fun main() {
            
            
            
                println("Hello, Power-Assert!")
            }
        """.trimIndent(),
        """
            fun main() {
                println("Hello!")
            
            
                println("Hello, Power-Assert!")
            }
        """.trimIndent(),
    )

    AnimateDiff(values) { text ->
        TitleAndBody { KotlinCodeText(text) }
    }
}

fun ShowBuilder.TextLinesAnimationSample() {
    slide {
        TitleAndBody {
            KotlinCodeText(
                """
                        fun main() {
                            assert(hello.length == "World".substring(1, 4).length)
                        }
                    """.trimIndent()
            )
        }
    }
    slide {
        TitleAndBody {
            KotlinCodeText(
                """
                        fun main() {
                            assert(hello.length == "World".substring(1, 4).length)
                        }
                    """.trimIndent()
            )
            Spacer(Modifier.padding(top = 16.dp))
            MacWindow {
                Text("java.lang.AssertionError: Assertion failed")
            }
        }
    }
    slide {
        val values = listOf(
            """
                    java.lang.AssertionError: Assertion failed
                    assert(hello.length == "World".substring(1, 4).length)
                """.trimIndent(),
            """
                    java.lang.AssertionError: Assertion failed
                    assert(hello.length == "World".substring(1, 4).length)
                                                                   |
                                                                   3
                """.trimIndent(),
            """
                    java.lang.AssertionError: Assertion failed
                    assert(hello.length == "World".substring(1, 4).length)
                                                   |               |
                                                   |               3
                                                   orl
                """.trimIndent(),
            """
                    java.lang.AssertionError: Assertion failed
                    assert(hello.length == "World".substring(1, 4).length)
                                        |          |               |
                                        |          |               3
                                        |          orl
                                        false
                """.trimIndent(),
            """
                    java.lang.AssertionError: Assertion failed
                    assert(hello.length == "World".substring(1, 4).length)
                                 |      |          |               |
                                 |      |          |               3
                                 |      |          orl
                                 |      false
                                 5
                """.trimIndent(),
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

        AnimateLines(values) { text ->
            TitleAndBody {
                KotlinCodeText(
                    """
                            fun main() {
                                assert(hello.length == "World".substring(1, 4).length)
                            }
                        """.trimIndent()
                )
                Spacer(Modifier.padding(top = 16.dp))
                MacWindow {
                    Text(text)
                }
            }
        }
    }
}

@Composable
fun SlideScope.VisibilitySample() {
    TitleAndBody {
        val visible by rememberAdvancementBoolean()
        Column {
            Box {

            }
            Row {
                AnimatedVisibility(visible) { Text("Test ", modifier = Modifier.alpha(0.0f)) }
                Text("Test")
            }
            Text("Test Test")

            AnimatedVisibility(visible) { Text("Test", modifier = Modifier.alpha(0.0f)) }
            Text("Test")
        }
    }
}

@Composable
fun SlideScope.AnimateDiff(values: List<String>, charDelay: Duration = 50.milliseconds, content: @Composable (String) -> Unit) {
    val state = rememberAdvancementAnimation()
    AnimateDiff(values, state, charDelay, content)
}

@Composable
fun AnimateDiff(
    values: List<String>,
    state: MutableState<AnimationState>,
    charDelay: Duration = 50.milliseconds,
    content: @Composable (String) -> Unit
) {
    require(values.size >= 2)

    var text by remember {
        mutableStateOf(if (state.value == AnimationState.PENDING) values.first() else values.last())
    }

    LaunchedAnimation(state) {
        when (it) {
            AnimationState.PENDING -> text = values.first()
            AnimationState.RUNNING -> flowDiff(values, charDelay).collect { text = it }
            AnimationState.COMPLETE -> text = values.last()
        }
    }

    content(text)
}

@Composable
fun SlideScope.AnimateLines(values: List<String>, charDelay: Duration = 50.milliseconds, content: @Composable (String) -> Unit) {
    val state = rememberAdvancementAnimation()
    AnimateLines(values, state, charDelay, content)
}

@Composable
fun AnimateLines(
    values: List<String>,
    state: MutableState<AnimationState>,
    charDelay: Duration = 50.milliseconds,
    content: @Composable (String) -> Unit
) {
    require(values.size >= 2)

    var text by remember(values, state) {
        mutableStateOf(if (state.value == AnimationState.PENDING) values.first() else values.last())
    }

    LaunchedAnimation(state) {
        when (it) {
            AnimationState.PENDING -> text = values.first()
            AnimationState.RUNNING -> flowLines(values, charDelay).collect { text = it }
            AnimationState.COMPLETE -> text = values.last()
        }
    }

    content(text)
}
