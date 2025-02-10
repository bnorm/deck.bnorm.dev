package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.*
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.MagicCode
import dev.bnorm.storyboard.core.SlideScope
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.enter
import dev.bnorm.storyboard.easel.exit

fun StoryboardBuilder.FunctionTransformation() {
    slide(stateCount = 6) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("How does it work?")
            }
            ProvideTextStyle(MaterialTheme.typography.body2) {
                state.AnimatedContent(
                    modifier = Modifier.animateEnterExit(
                        enter = enter(end = { slideInHorizontally { -it } + fadeIn() }),
                        exit = exit(end = { slideOutHorizontally { -it } + fadeOut() }),
                    ),
                    transitionSpec = { EnterTransition.None togetherWith ExitTransition.None }
                ) {
                    when (it.toState()) {
                        0 -> Box(Modifier.fillMaxSize())
                        1 -> {
                            Box(Modifier.fillMaxSize()) {
                                OriginalFunction(this@AnimatedContent)
                                SyntheticFunction(this@AnimatedContent)
                            }
                        }

                        else -> {
                            Column(Modifier.fillMaxSize()) {
                                OriginalFunction(this@AnimatedContent)
                                Spacer(Modifier.height(16.dp))
                                SyntheticFunction(this@AnimatedContent)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SlideScope<Int>.OriginalFunction(scope: AnimatedVisibilityScope) {
    Box(
        modifier = Modifier.sharedElement(
            sharedContentState = rememberSharedContentState("original-fun"),
            animatedVisibilityScope = scope,
            boundsTransform = { _, _ ->
                tween(moveDuration, delayMillis = fadeDuration)
            },
        )
    ) {
        state.createChildTransition { it.toState() - 3 }
            .MagicCode(ORIGINAL_FUN_TRANSFORMATIONS)
    }
}

@Composable
private fun SlideScope<Int>.SyntheticFunction(scope: AnimatedVisibilityScope) {
    Box(
        modifier = Modifier.sharedElement(
            sharedContentState = rememberSharedContentState("synthetic-fun"),
            animatedVisibilityScope = scope,
            boundsTransform = { _, _ ->
                tween(moveDuration, delayMillis = fadeDuration)
            },
        )
    ) {
        state.createChildTransition { it.toState() - 1 }
            .MagicCode(SYNTHETIC_FUN_TRANSFORMATIONS)
    }
}

private val SYNTHETIC_FUN_TRANSFORMATIONS = listOf(
    """
        @ExplainCall fun powerAssert(condition: Boolean) {
            if (!condition) {
                val explanation = ExplainCall.explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent() to """
        @ExplainCall fun powerAssert(condition: Boolean) {
            if (!condition) {
                val explanation = ExplainCall.explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent(),

    """
        <i>@ExplainCall </i>fun powerAssert<i></i>(condition: Boolean<i></i>) {
            if (!condition) {
                val explanation = ExplainCall.explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent() to """
        <i></i>fun powerAssert<i>_Explained</i>(condition: Boolean<i>, _explanation: CallExplanation</i>) {
            if (!condition) {
                val explanation = ExplainCall.explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent(),

    """
        fun powerAssert_Explained(condition: Boolean, _explanation: CallExplanation) {
            if (!condition) {
                val explanation = <i>ExplainCall.explanation</i>
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent() to """
        fun powerAssert_Explained(condition: Boolean, _explanation: CallExplanation) {
            if (!condition) {
                val explanation = <i>_explanation</i>
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent(),

    """
        fun powerAssert_Explained(condition: Boolean, _explanation: CallExplanation) {
            if (!condition) {
                val explanation = _explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent() to """
        fun powerAssert_Explained(condition: Boolean, _explanation: CallExplanation) {
            if (!condition) {
                val explanation = _explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent(),
)

private val ORIGINAL_FUN_TRANSFORMATIONS = buildList {
    add(
        """
            @ExplainCall fun powerAssert(condition: Boolean) {
                if (!condition) {
                    val explanation = ExplainCall.explanation
                        ?: throw AssertionError("Assertion failed")
                    // ...
                }
            }
        """.trimIndent() to """
            @ExplainCall fun powerAssert(condition: Boolean) {
                if (!condition) {
                    val explanation = ExplainCall.explanation
                        ?: throw AssertionError("Assertion failed")
                    // ...
                }
            }
        """.trimIndent()
    )

    add(
        """
            @ExplainCall fun powerAssert(condition: Boolean) {
                if (!condition) {
                    val explanation = <i>ExplainCall.explanation</i>
                        ?: throw AssertionError("Assertion failed")
                    // ...
                }
            }
        """.trimIndent() to """
            @ExplainCall fun powerAssert(condition: Boolean) {
                if (!condition) {
                    val explanation = <i>null</i>
                        ?: throw AssertionError("Assertion failed")
                    // ...
                }
            }
        """.trimIndent()
    )
}
