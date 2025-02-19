package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide

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

    slide(stateCount = 5) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("What's new?")
            }
            if (currentState >= 1) {
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    Box(Modifier.Companion.fillMaxSize()) {
                        var textLayoutResult: TextLayoutResult? by remember { mutableStateOf(null) }
                        Text(
                            text = code.toCode(),
                            onTextLayout = { textLayoutResult = it },
                        )

                        SharedTransitionLayout {
                            state.AnimatedContent(
                                transitionSpec = { EnterTransition.Companion.None togetherWith ExitTransition.Companion.None },
                            ) {
                                Box(Modifier.Companion.fillMaxSize())

                                textLayoutResult?.let { result ->
                                    SurroundingBox(
                                        result,
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
                Modifier.Companion
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
