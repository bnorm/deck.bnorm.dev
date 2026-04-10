package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.SampleComparison
import dev.bnorm.kc26.components.VersionCompareState
import dev.bnorm.kc26.sections.it.Sample
import dev.bnorm.kc26.template.SectionHeader
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.mapToValue

fun StoryboardBuilder.StartSection() {
    carouselScene(
        frames = listOf(
            VersionCompareState.Hidden,
            VersionCompareState.Before,
        ),
    ) {
        Column {
            SampleComparison(
                sample = { Text(Sample) },
                beforeVersion = { GradientText("2.0") },
                beforeOutput = {
                    Text(
                        """
                            java.lang.AssertionError: Assertion failed
                            assert(hello.length == "World".substring(1, 4).length)
                                   |     |      |          |               |
                                   |     |      |          |               3
                                   |     |      |          orl
                                   |     |      false
                                   |     5
                                   Hello
                        """.trimIndent()
                    )
                },
                afterVersion = { },
                afterOutput = { },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { it == VersionCompareState.Hidden }
                },
                showAfter = transition.createChildTransition { false },
            )
        }
    }

    // TODO need to talk about the Gradle configuration a little
}
