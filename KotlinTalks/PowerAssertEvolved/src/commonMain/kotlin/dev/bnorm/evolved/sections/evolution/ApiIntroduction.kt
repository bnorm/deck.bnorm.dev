package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.ApiIntroduction() {
    val code = """
        @ExplainCall fun powerAssert(condition: Boolean) {
            if (!condition) {
                val explanation: CallExplanation? = ExplainCall.explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent()

    fun getSnippetRange(state: Int): IntRange? {
        val snippet = when (state) {
            2 -> "@ExplainCall"
            3 -> "ExplainCall.explanation"
            4 -> "CallExplanation?"
            else -> return null
        }
        val index = code.indexOf(snippet)
        if (index < 0) return null
        return index..(index + snippet.length)
    }

    scene(stateCount = 5) {
        val rememberTextMeasurer = rememberTextMeasurer()

        HeaderAndBody {
            if (currentState >= 1) {
                Box(Modifier.fillMaxSize()) {
                    Text(text = code.toCode())

                    SharedTransitionLayout {
                        frame.AnimatedContent(
                            transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                        ) {
                            Box(Modifier.fillMaxSize())

                            SurroundingBox(
                                rememberTextMeasurer.measure(code.toCode(), style = LocalTextStyle.current),
                                it.toState(),
                                this@SharedTransitionLayout,
                                this@AnimatedContent,
                            ) {
                                getSnippetRange(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> SurroundingBox(
    result: TextLayoutResult,
    item: T,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    getRange: (T) -> IntRange?,
) {
    val range = getRange(item) ?: return
    val path = result.getPathForRange(range.start, range.endInclusive).getBounds()
    val size = (path.bottomRight - path.topLeft + Offset(20f, 0f)).round()
    val offset = (path.topLeft - Offset(10f, 0f)).round()

    with(sharedTransitionScope) {
        with(LocalDensity.current) {
            Box(
                Modifier
                    .size(size.x.toDp(), size.y.toDp())
                    .offset(offset.x.toDp(), offset.y.toDp())
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("text-box"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                    .border(2.dp, MaterialTheme.colors.primary)
            )
        }
    }
}
