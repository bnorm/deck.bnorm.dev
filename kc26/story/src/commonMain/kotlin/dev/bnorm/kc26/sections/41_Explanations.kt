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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.ResourceImage
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.animateScroll
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.qr_power_assert_keep
import dev.bnorm.kc26.components.*
import dev.bnorm.kc26.samples.ArraySample
import dev.bnorm.kc26.samples.ImplementationSample
import dev.bnorm.kc26.samples.MultilineSample
import dev.bnorm.kc26.samples.PowerAssertSample
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
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

    PowerAssertSolutionSummary()
}

private fun StoryboardBuilder.ArrayProblem() {
    data class SceneState(
        val showOutput: Boolean,
        val showAfter: Boolean,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, afterOutput = ArraySample.v24Output[1]),
            SceneState(showOutput = true, showAfter = false, afterOutput = ArraySample.v24Output[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = ArraySample.v24Output[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = ArraySample.v24Output[2]),
        ),
    ) {
        Timeline(current = TimelineState.Explanations)
        SceneCodeSample(
            content = { Text(ArraySample.sample) },
            output = {
                OutputComparison(
                    beforeVersion = { GradientText("2.0") },
                    beforeOutput = { Text(ArraySample.v24Output[0].string) },
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
            SceneState(showOutput = false, showAfter = false, afterOutput = MultilineSample.v24Output[1]),
            SceneState(showOutput = true, showAfter = false, afterOutput = MultilineSample.v24Output[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = MultilineSample.v24Output[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = MultilineSample.v24Output[2]),
        ),
    ) {
        Timeline(current = TimelineState.Explanations)
        SceneCodeSample(
            content = { Text(MultilineSample.stringSample) },
            output = {
                OutputComparison(
                    beforeVersion = { GradientText("2.0") },
                    beforeOutput = { Text(MultilineSample.v24Output[0].string) },
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
    val samples = ImplementationSample.samples
    carouselScene(frames = samples.subList(samples.size - 2, samples.size)) {
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
            val sample = transition.createChildTransition { PowerAssertSample.originalFun[it] }
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
                    PowerAssertSample.syntheticFun[it].string.splitByTags()
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
            while (current.synthetic + 2 < PowerAssertSample.syntheticFun.size) {
                next { copy(synthetic = synthetic + 1) }
            }
            next { copy(original = original + 1, showSynthetic = false) }
            while (current.original + 1 < PowerAssertSample.originalFun.size) {
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
        val sample: CodeSample,
        val showOutput: Boolean,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(
                sample = PowerAssertSample.sample[0],
                showOutput = false,
                afterOutput = PowerAssertSample.v24Output[0]
            ),
            SceneState(
                sample = PowerAssertSample.sample[0],
                showOutput = true,
                afterOutput = PowerAssertSample.v24Output[0]
            ),
            SceneState(
                sample = PowerAssertSample.sample[1],
                showOutput = true,
                afterOutput = PowerAssertSample.v24Output[0]
            ),
            SceneState(
                sample = PowerAssertSample.sample[1],
                showOutput = true,
                afterOutput = PowerAssertSample.v24Output[1]
            ),
        ),
    ) {
        Timeline(current = TimelineState.Explanations)
        SceneCodeSample(
            content = {
                MagicText(transition.createChildTransition { it.toValue().sample.string.splitByTags() })
            },
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

val codeSpan = SpanStyle(fontFamily = FontFamily.Monospace)
private fun StoryboardBuilder.PowerAssertSolutionSummary() {
    carouselScene(frameCount = 4) {
        Timeline(current = TimelineState.Improvements)
        Summary {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(buildAnnotatedString {
                    withStyle(codeSpan) { append("CallExplanation") }
                    append(" and ")
                    withStyle(codeSpan) { append("@PowerAssert") }
                    append(" solve all of our problems:")
                })
                RevealEach(transition.createChildTransition { it.toValue() - 1 }) {
                    item {
                        Row(modifier = Modifier.padding(start = 16.dp)) {
                            Text(BULLET_1)
                            Text(buildAnnotatedString {
                                withStyle(codeSpan) { append("@PowerAssert") }
                                append(" functions are automatically discovered by the compiler plugin at the call-site.")
                            })
                        }
                    }
                    item {
                        Row(modifier = Modifier.padding(start = 16.dp)) {
                            Text(BULLET_1)
                            Text(buildAnnotatedString {
                                withStyle(codeSpan) { append("@PowerAssert") }
                                append(" functions declarations are transformed by the compiler plugin, generating the needed signature to consume ")
                                withStyle(codeSpan) { append("CallExplanation") }
                                append(".")
                            })
                        }
                    }
                    item {
                        Row(modifier = Modifier.padding(start = 16.dp)) {
                            Text(BULLET_1)
                            Text(buildAnnotatedString {
                                append("Static diagram generation is replaced by runtime generation via the information provided by a ")
                                withStyle(codeSpan) { append("CallExplanation") }
                                append(", and in-depth information is available for advanced tooling.")
                            })
                        }
                    }
                }
            }
        }
    }
}
