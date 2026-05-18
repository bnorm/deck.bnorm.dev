package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.material.Text
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.OutputComparison
import dev.bnorm.kc26.components.SceneCodeSample
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.mapToValue
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.FutureSection() {
    LocalVariables()
    // TODO arguments?
    // TODO diffs?
}

private fun StoryboardBuilder.LocalVariables() {
    data class SceneState(
        val showOutput: Boolean,
        val showAfter: Boolean,
        val sample: CodeSample,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, sample = LocalVariableSample.samples[0], afterOutput = LocalVariableSample.outputs[1]),
            SceneState(showOutput = false, showAfter = false, sample = LocalVariableSample.samples[1], afterOutput = LocalVariableSample.outputs[1]),
            SceneState(showOutput = true, showAfter = false, sample = LocalVariableSample.samples[1], afterOutput = LocalVariableSample.outputs[1]),
            SceneState(showOutput = true, showAfter = false, sample = LocalVariableSample.samples[2], afterOutput = LocalVariableSample.outputs[1]),
            SceneState(showOutput = true, showAfter = true, sample = LocalVariableSample.samples[2], afterOutput = LocalVariableSample.outputs[1]),
            SceneState(showOutput = true, showAfter = true, sample = LocalVariableSample.samples[2], afterOutput = LocalVariableSample.outputs[2]),
        ),
    ) {
        Timeline(current = TimelineState.Future)
        SceneCodeSample(
            content = { MagicText(transition.createChildTransition { it.toValue().sample.string.splitByTags() }) },
            output = {
                OutputComparison(
                    beforeVersion = { GradientText("2.4") },
                    beforeOutput = { Text(LocalVariableSample.outputs[0].string) },
                    afterVersion = { GradientText("2.?") },
                    afterOutput = { MagicText(transition.createChildTransition { it.toValue().afterOutput.string.splitByTags() }) },
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

    // TODO argument explanation as well

}
