package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.MagicCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.enter
import dev.bnorm.storyboard.easel.exit

fun StoryboardBuilder.CallTransformation() {
    slide(stateCount = 6) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("How does it work?")
            }
            ProvideTextStyle(MaterialTheme.typography.body2) {
                Box(
                    modifier = Modifier.animateEnterExit(
                        enter = enter(start = { slideInHorizontally { it } + fadeIn() }),
                        exit = exit(start = { slideOutHorizontally { it } + fadeOut() }),
                    ).fillMaxSize()
                ) {
                    state.createChildTransition { it.toState() + 1 }
                        .MagicCode(CALL_TRANSFORMATIONS)
                }
            }
        }
    }
}

private val CALL_TRANSFORMATIONS = listOf(
    """
        @Test fun test() {
            powerAssert("Hello".length == "World".substring(1, 4).length)
        }
    """.trimIndent() to """
        @Test fun test() {
            powerAssert("Hello".length == "World".substring(1, 4).length)
        }
    """.trimIndent(),

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
)
