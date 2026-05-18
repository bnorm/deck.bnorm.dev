package dev.bnorm.kc26.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.ResourceImage
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.animateScroll
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.qr_power_assert_keep
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.OutputComparison
import dev.bnorm.kc26.components.SceneCodeSample
import dev.bnorm.kc26.samples.Array2Sample
import dev.bnorm.kc26.samples.FunctionSample
import dev.bnorm.kc26.samples.MultilineSample
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.mapToValue
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.ExplanationsSection() {
    // TICKETS:
    // https://youtrack.jetbrains.com/issue/KT-75266/PowerAssert-arrayOf-isnt-displayed-on-the-diagram
    // https://youtrack.jetbrains.com/issue/KT-69036/Power-Assert-indent-multiline-values

    CallExplanationSolution()
    ArrayProblem()
    MultilineProblem()

    // TODO split this stuff off into it's own section?
    AnnotationTransformation()
    AnnotationUse()
}

private fun StoryboardBuilder.ArrayProblem() {
    data class SceneState(
        val showOutput: Boolean,
        val showAfter: Boolean,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, afterOutput = Array2Sample.v24Output[1]),
            SceneState(showOutput = true, showAfter = false, afterOutput = Array2Sample.v24Output[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = Array2Sample.v24Output[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = Array2Sample.v24Output[2]),
        ),
    ) {
        Timeline(current = TimelineState.Explanations)
        SceneCodeSample(
            content = { Text(Array2Sample.arraySample) },
            output = {
                OutputComparison(
                    beforeVersion = { GradientText("2.0") },
                    beforeOutput = { Text(Array2Sample.v24Output[0].string) },
                    afterVersion = { GradientText("2.4") },
                    afterOutput = {
                        MagicText(transition.createChildTransition { it.toValue().afterOutput.string.splitByTags() })
                    },
                    showAfter = transition.createChildTransition { frame ->
                        frame.mapToValue(start = false, end = true) { it.showAfter }
                    },
                )
            },
            hideOutput = transition.createChildTransition { frame ->
                frame.mapToValue(start = true, end = true) { !it.showOutput }
            },
        )
    }
}

private fun StoryboardBuilder.MultilineProblem() {
    data class SceneState(
        val showOutput: Boolean,
        val showAfter: Boolean,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, afterOutput = MultilineSample.stringOutput[1]),
            SceneState(showOutput = true, showAfter = false, afterOutput = MultilineSample.stringOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = MultilineSample.stringOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = MultilineSample.stringOutput[2]),
        ),
    ) {
        Timeline(current = TimelineState.Explanations)
        SceneCodeSample(
            content = { Text(MultilineSample.stringSample) },
            output = {
                OutputComparison(
                    beforeVersion = { GradientText("2.0") },
                    beforeOutput = { Text(MultilineSample.stringOutput[0].string) },
                    afterVersion = { GradientText("2.4") },
                    afterOutput = {
                        MagicText(transition.createChildTransition { it.toValue().afterOutput.string.splitByTags() })
                    },
                    showAfter = transition.createChildTransition { frame ->
                        frame.mapToValue(start = false, end = true) { it.showAfter }
                    },
                )
            },
            hideOutput = transition.createChildTransition { frame ->
                frame.mapToValue(start = true, end = true) { !it.showOutput }
            },
        )
    }
}

private fun StoryboardBuilder.CallExplanationSolution() {
    carouselScene(frames = Implementation2Sample.samples) {
        Timeline(current = TimelineState.Explanations)
        SceneCodeSample {
            Box(Modifier.fillMaxSize()) {
                MagicText(transition.createChildTransition { it.toValue().string.splitByTags() })
            }
        }
    }
}

private fun StoryboardBuilder.AnnotationTransformation() {
    @Composable
    fun OriginalFunction(transition: Transition<Int>, modifier: Modifier = Modifier) {
        Box(modifier) {
            val sample = transition.createChildTransition { FunctionSample.originalFun[it] }
            val state = rememberScrollState()
            sample.animateScroll(state)

            ProvideTextStyle(MaterialTheme.typography.code1) {
                // Scroll should come before any padding, so it is not clipped.
                Box(Modifier.verticalScroll(state, enabled = false)) {
                    MagicText(sample.createChildTransition { it.string.splitByTags() })
                }
            }
        }
    }

    @Composable
    fun SyntheticFunction(transition: Transition<Int>, modifier: Modifier = Modifier) {
        Box(modifier) {
            ProvideTextStyle(MaterialTheme.typography.code1) {
                MagicText(transition.createChildTransition {
                    FunctionSample.syntheticFun[it].string.splitByTags()
                })
            }
        }
    }

    data class SceneState(
        val original: Int,
        val synthetic: Int,
        val showSynthetic: Boolean,
    )

    carouselScene(
        start = SceneState(original = 0, synthetic = 0, showSynthetic = false),
        frames = {
            next { copy(showSynthetic = true) }
            next { copy(original = original + 1) }

            before { copy(original = original + 1) }
            while (current.synthetic + 2 < FunctionSample.syntheticFun.size) {
                next { copy(synthetic = synthetic + 1) }
            }
            next { copy(original = original + 1, showSynthetic = false) }
            while (current.original + 1 < FunctionSample.originalFun.size) {
                next { copy(original = original + 1) }
            }
        },
    ) {
        fun <T> spec() = tween<T>(500, easing = EaseInOut)

        Timeline(current = TimelineState.Explanations)
        SceneCodeSample {
            Box(Modifier.fillMaxSize()) {
                val original = transition.createChildTransition { it.toValue().original }
                val synthetic = transition.createChildTransition { it.toValue().synthetic }
                Column(Modifier.fillMaxSize()) {
                    OriginalFunction(transition = original)
                    Spacer(Modifier.size(16.dp))
                    transition.AnimatedVisibility(
                        visible = { it.toValue().showSynthetic },
                        enter = slideInVertically(spec()) { -252 - 32 } + fadeIn(spec()),
                        exit = slideOutVertically(spec()) { -252 - 32 } + fadeOut(spec()),
                    ) {
                        SyntheticFunction(transition = synthetic)
                    }
                }

                ResourceImage(
                    Res.drawable.qr_power_assert_keep,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 24.dp)
                        .height(98.dp)
                )
            }
        }
    }
}

private fun StoryboardBuilder.AnnotationUse() {
    data class SceneState(
        val showOutput: Boolean,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, afterOutput = FunctionSample.arrayOutput[0]),
            SceneState(showOutput = true, afterOutput = FunctionSample.arrayOutput[0]),
            SceneState(showOutput = true, afterOutput = FunctionSample.arrayOutput[1]),
        ),
    ) {
        Timeline(current = TimelineState.Explanations)
        SceneCodeSample(
            content = { Text(FunctionSample.arraySample) },
            output = {
                OutputComparison(
                    beforeVersion = {},
                    beforeOutput = {},
                    afterVersion = { GradientText("2.4") },
                    afterOutput = {
                        MagicText(transition.createChildTransition { it.toValue().afterOutput.string.splitByTags() })
                    },
                    showAfter = updateTransition(true),
                )
            },
            hideOutput = transition.createChildTransition { frame ->
                frame.mapToValue(start = true, end = true) { !it.showOutput }
            },
        )
    }
}
