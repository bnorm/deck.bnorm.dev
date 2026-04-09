package dev.bnorm.kc26.sections.it

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.mac.MacTerminalPopup
import dev.bnorm.kc26.template.*
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.PowerAssertIntroExample() {
    carouselScene(frameCount = 5) {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding)) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    Text(Sample)
                }
            }
        }

        MacTerminalPopup(visible = { it.toValue(end = Int.MAX_VALUE) in 1..3 }) {
            Box(Modifier.height(192.dp)) {
                ProvideTextStyle(MaterialTheme.typography.code1 + TextStyle(fontFamily = FontFamily.Monospace)) {
                    val output by transition.createChildTransition { it.toValue() - 1 }.animateList(
                        values = Output,
                        transitionSpec = {
                            if (!(1 isTransitioningTo 2) && !(2 isTransitioningTo 1)) snap()
                            else tween(2_000, easing = LinearEasing)
                        }
                    ) { if (it >= 2) Output.lastIndex else it }
                    Text(output)
                }
            }
        }
    }
}

val Sample = """
    @Test fun test() {
        val hello = "Hello"
        assert(hello.length == "World".substring(1, 4).length)
    }
""".trimIndent().toKotlin {
    when (it) {
        "substring" -> extensionFunctionCall
        "length" -> property
        "assert" -> staticFunctionCall
        else -> null
    }
}

private val Output = buildList {
    add(
        """
            java.lang.AssertionError: Assertion failed
        """.trimIndent()
    )
    add(
        """
            java.lang.AssertionError: Assertion failed
            assert(hello.length == "World".substring(1, 4).length)
        """.trimIndent()
    )
    addAll(
        animateLines(
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
    )
}

@Composable
inline fun <S, T> Transition<S>.animateList(
    values: List<T>,
    noinline transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Int>,
    label: String = "ListAnimation",
    targetIndexByState: @Composable (state: S) -> Int,
): State<T> {
    val index = animateInt(
        transitionSpec = transitionSpec,
        label = label,
        targetValueByState = targetIndexByState,
    )
    // TODO is this the best way to map a state object?
    return derivedStateOf { values[index.value.coerceIn(values.indices)] }
}

fun animateLines(vararg checkpoints: String): Sequence<String> {
    require(checkpoints.size >= 2)
    return sequence {
        var last = checkpoints[0]
        yield(last)

        for (i in checkpoints.indices) {
            if (i + 1 >= checkpoints.size) break

            val start = checkpoints[i]
            val end = checkpoints[i + 1]

            val startLines = start.lines()
            val endLines = end.lines()
            require(endLines.size >= startLines.size)

            for (line in endLines.indices) {
                val value = buildString {
                    for (i in 0..line) {
                        appendLine(endLines[i])
                    }
                    for (i in line + 1..<startLines.size) {
                        appendLine(startLines[i])
                    }
                }.trim()
                if (value != last) {
                    yield(value)
                    last = value
                }
            }
        }
    }
}