package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.mac.MacTerminalPopup
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.MagicCode
import dev.bnorm.evolved.template.code.padLines
import dev.bnorm.evolved.template.code.twice
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.toInt

fun StoryboardBuilder.PowerAssertCall() {
    scene(
        stateCount = 8,
        enterTransition = enter(start = SceneEnter(Alignment.CenterEnd)),
        exitTransition = exit(start = SceneExit(Alignment.CenterEnd)),
    ) {
        HeaderAndBody {
            Box(Modifier.fillMaxSize()) {
                Box(Modifier.padding(horizontal = 32.dp)) {
                    frame.createChildTransition { it.toState() }
                        .MagicCode(
                            CALL_TRANSFORMATIONS,
                            identifierType = { highlighting, identifier ->
                                when (identifier) {
                                    "test" -> highlighting.functionDeclaration
                                    "powerAssert", "powerAssert_Explained" -> highlighting.staticFunctionCall
                                    else -> null
                                }
                            }
                        )
                }
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    MacTerminalPopup(visible = { it.toInt() == 7 }) {
                        Text(EXAMPLE_POWER_ASSERT_OUTPUT)
                    }
                }
            }
        }
    }
}

private val CALL_TRANSFORMATIONS = listOf(
    """
        @Test fun test() {
            powerAssert(<m>"Hello".length</m=1> == "World".substring(1, 4).length)
        }
    """.trimIndent() to """
        @Test fun test() {
        <i>    val tmp1 = </i><m>"Hello".length</m=1>
            powerAssert(<i>tmp1</i> == "World".substring(1, 4).length)
        }
    """.trimIndent(),

    """
        @Test fun test() {
            val tmp1 = "Hello".length
            powerAssert(tmp1 == <m>"World".substring(1, 4)</m=2>.length)
        }
    """.trimIndent() to """
        @Test fun test() {
            val tmp1 = "Hello".length
        <i>    val tmp2 = </i><m>"World".substring(1, 4)</m=2>
            powerAssert(tmp1 == <i>tmp2</i>.length)
        }
    """.trimIndent(),

    """
        @Test fun test() {
            val tmp1 = "Hello".length
            val tmp2 = "World".substring(1, 4)
            powerAssert(tmp1 == <m>tmp2.length</m=3>)
        }
    """.trimIndent() to """
        @Test fun test() {
            val tmp1 = "Hello".length
            val tmp2 = "World".substring(1, 4)
        <i>    val tmp3 = </i><m>tmp2.length</m=3>
            powerAssert(tmp1 == <i>tmp3</i>)
        }
    """.trimIndent(),

    """
        @Test fun test() {
            val tmp1 = "Hello".length
            val tmp2 = "World".substring(1, 4)
            val tmp3 = tmp2.length
            powerAssert(<m>tmp1 == tmp3</m=4>)
        }
    """.trimIndent() to """
        @Test fun test() {
            val tmp1 = "Hello".length
            val tmp2 = "World".substring(1, 4)
            val tmp3 = tmp2.length
        <i>    val tmp4 = </i><m>tmp1 == tmp3</m=4>
            powerAssert(<i>tmp4</i>)
        }
    """.trimIndent(),

    """
        @Test fun test() {
            val tmp1 = "Hello".length
            val tmp2 = "World".substring(1, 4)
            val tmp3 = tmp2.length
            val tmp4 = tmp1 == tmp3
            powerAssert<i></i>(tmp4<i></i>)
        }
    """.trimIndent() to """
        @Test fun test() {
            val tmp1 = "Hello".length
            val tmp2 = "World".substring(1, 4)
            val tmp3 = tmp2.length
            val tmp4 = tmp1 == tmp3
            powerAssert<i>_Explained</i>(tmp4<i>, CallExplanation(/* ... */)</i>)
        }
    """.trimIndent(),

    """
        @Test fun test() {
        <i>    val tmp1 = </i><m>"Hello".length</m=1>
        <i>    val tmp2 = </i><m>"World".substring(1, 4)</m=2>
        <i>    val tmp3 = tmp2</i><m>.length</m=3>
        <i>    val tmp4 = tmp1</i><m> == </m=4><i>tmp3</i>
            powerAssert<i>_Explained</i>(<i>tmp4, CallExplanation(/* ... */)</i>)
        }
    """.trimIndent() to """
        @Test fun test() {
            powerAssert<i></i>(<m>"Hello".length</m=1><m> == </m=4><m>"World".substring(1, 4)</m=2><m>.length</m=3>)
        }
    """.trimIndent(),

    """
        @Test fun test() {
            powerAssert("Hello".length == "World".substring(1, 4).length)
        }
    """.trimIndent().twice(),
)

val EXAMPLE_POWER_ASSERT_OUTPUT = """
Assertion failed:
powerAssert("Hello".length == "World".substring(1, 4).length)
                    |      |          |               |
                    5      false      orl             3

Expected :5
Actual   :3
<Click to see difference>
""".trimIndent().padLines(12)
