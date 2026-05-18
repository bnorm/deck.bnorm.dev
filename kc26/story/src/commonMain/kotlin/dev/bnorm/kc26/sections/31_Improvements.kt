package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.material.Text
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.OutputComparison
import dev.bnorm.kc26.components.SceneCodeSample
import dev.bnorm.kc26.components.VersionCompareState
import dev.bnorm.kc26.samples.ArraySample
import dev.bnorm.kc26.samples.ImplementationSample
import dev.bnorm.kc26.samples.WhenSample
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.mapToValue
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.ImprovementsSection() {
    // TICKETS:
    //
    // Kotlin 2.1
    // https://youtrack.jetbrains.com/issue/KT-69646/PowerAssert-result-of-array-access-operator-is-unaligned
    // https://youtrack.jetbrains.com/issue/KT-69038/Power-Assert-does-not-display-const-vals
    // https://youtrack.jetbrains.com/issue/KT-73870/PowerAssert-Object-should-not-be-displayed
    // https://youtrack.jetbrains.com/issue/KT-73871/PowerAssert-Comparison-via-operator-overload-results-in-confusing-diagram
    //
    // Kotlin 2.2
    // https://youtrack.jetbrains.com/issue/KT-75663/PowerAssert-contains-result-for-strings-is-displayed-under-the-first-parameter-instead-of-in
    // https://youtrack.jetbrains.com/issue/KT-75264/PowerAssert-the-diagram-for-try-catch-with-boolean-expressions-isnt-clear
    // https://youtrack.jetbrains.com/issue/KT-75263/PowerAssert-no-additional-info-is-displayed-for-when-with-subject
    // https://youtrack.jetbrains.com/issue/KT-75876/PowerAssert-dont-display-results-for-assertion-operator
    // https://youtrack.jetbrains.com/issue/KT-75265/PowerAssert-the-result-of-invoke-is-displayed-at-the-same-level-as-value-that-can-be-confusing
    //
    // Kotlin 2.4
    // https://youtrack.jetbrains.com/issue/KT-75873/PowerAssert-display-callable-reference-value-under

    WhenExpressionImprovements()
    DiagramImprovementsAndProblems()
    // TODO add multi-line string problem

    // TODO move all implementation details to explanations section instead
    Kotlin23Implementation()
}

private fun StoryboardBuilder.WhenExpressionImprovements() {
    carouselScene(
        frames = listOf(
            VersionCompareState.Hidden,
            VersionCompareState.Before,
            VersionCompareState.After,
        ),
    ) {
        Timeline(current = TimelineState.Improvements)
        SceneCodeSample(
            content = { Text(WhenSample.sample) },
            output = {
                OutputComparison(
                    beforeVersion = { GradientText("2.0") },
                    beforeOutput = { Text(WhenSample.v20Output) },
                    afterVersion = { GradientText("2.3") },
                    afterOutput = { Text(WhenSample.v23Output) },
                    showAfter = transition.createChildTransition { frame ->
                        frame.mapToValue(start = false, end = true) { it == VersionCompareState.After }
                    },
                )
            },
            hideOutput = transition.createChildTransition { frame ->
                frame.mapToValue(start = true, end = true) { it == VersionCompareState.Hidden }
            },
        )
    }
}

private fun StoryboardBuilder.DiagramImprovementsAndProblems() {
    carouselScene(
        frames = listOf(
            VersionCompareState.Hidden,
            VersionCompareState.Before,
            VersionCompareState.After,
        ),
    ) {
        Timeline(current = TimelineState.Improvements)
        SceneCodeSample(
            content = { Text(ArraySample.sample) },
            output = {
                OutputComparison(
                    beforeVersion = { GradientText("2.0") },
                    beforeOutput = { Text(ArraySample.v20Output) },
                    afterVersion = { GradientText("2.3") },
                    afterOutput = { Text(ArraySample.v23Output) },
                    showAfter = transition.createChildTransition { frame ->
                        frame.mapToValue(start = false, end = true) { it == VersionCompareState.After }
                    },
                )
            },
            hideOutput = transition.createChildTransition { frame ->
                frame.mapToValue(start = true, end = true) { it == VersionCompareState.Hidden }
            },
        )
    }
}

private fun StoryboardBuilder.Kotlin23Implementation() {
    carouselScene(frames = ImplementationSample.samples) {
        Timeline(current = TimelineState.Improvements)
        SceneCodeSample {
            MagicText(transition.createChildTransition { it.toValue().string })
        }
    }
}
