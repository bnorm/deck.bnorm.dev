package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.MagicCode
import dev.bnorm.storyboard.core.SlideScope
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.enter
import dev.bnorm.storyboard.easel.exit
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit

private val moveDuration = DefaultDurationMillis
private val fadeDuration = moveDuration / 2

fun StoryboardBuilder.FunctionTransformation() {
    slide(
        stateCount = 5,
        enterTransition = enter(end = SlideEnter(Alignment.CenterEnd)),
        exitTransition = exit(end = SlideExit(Alignment.CenterEnd)),
    ) {
        HeaderAndBody {
            state.AnimatedContent(
                transitionSpec = { EnterTransition.None togetherWith ExitTransition.None }
            ) {
                when (it.toState()) {
                    0 -> {
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
        state.createChildTransition { it.toState() - 2 }
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
        state.createChildTransition { it.toState() }
            .MagicCode(SYNTHETIC_FUN_TRANSFORMATIONS)
    }
}

private val SYNTHETIC_FUN_TRANSFORMATIONS = listOf(
    """
        @ExplainCall fun powerAssert(condition: Boolean) {
            if (!condition) {
                val explanation: CallExplanation? = ExplainCall.explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent() to """
        @ExplainCall fun powerAssert(condition: Boolean) {
            if (!condition) {
                val explanation: CallExplanation? = ExplainCall.explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent(),

    """
        <i>@ExplainCall </i>fun powerAssert<i></i>(condition: Boolean<i></i>) {
            if (!condition) {
                val explanation: CallExplanation? = ExplainCall.explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent() to """
        <i></i>fun powerAssert<i>_Explained</i>(condition: Boolean<i>, _explanation: CallExplanation</i>) {
            if (!condition) {
                val explanation: CallExplanation? = ExplainCall.explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent(),

    """
        fun powerAssert_Explained(condition: Boolean, _explanation: CallExplanation) {
            if (!condition) {
                val explanation: CallExplanation? = <i>ExplainCall.explanation</i>
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent() to """
        fun powerAssert_Explained(condition: Boolean, _explanation: CallExplanation) {
            if (!condition) {
                val explanation: CallExplanation? = <i>_explanation</i>
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent(),

    """
        fun powerAssert_Explained(condition: Boolean, _explanation: CallExplanation) {
            if (!condition) {
                val explanation: CallExplanation? = _explanation
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent() to """
        fun powerAssert_Explained(condition: Boolean, _explanation: CallExplanation) {
            if (!condition) {
                val explanation: CallExplanation? = _explanation
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
                    val explanation: CallExplanation? = ExplainCall.explanation
                        ?: throw AssertionError("Assertion failed")
                    // ...
                }
            }
        """.trimIndent() to """
            @ExplainCall fun powerAssert(condition: Boolean) {
                if (!condition) {
                    val explanation: CallExplanation? = ExplainCall.explanation
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
                    val explanation: CallExplanation? = <i>ExplainCall.explanation</i>
                        ?: throw AssertionError("Assertion failed")
                    // ...
                }
            }
        """.trimIndent() to """
            @ExplainCall fun powerAssert(condition: Boolean) {
                if (!condition) {
                    val explanation: CallExplanation? = <i>null</i>
                        ?: throw AssertionError("Assertion failed")
                    // ...
                }
            }
        """.trimIndent()
    )
}
