package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.SampleComparison
import dev.bnorm.kc26.components.VersionCompareState
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.toKotlin
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.mapToValue

fun StoryboardBuilder.MinorSection() {
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

    Kotlin21Improvements()
    Kotlin22Improvements()
    // TODO slide showing
}

private fun StoryboardBuilder.Kotlin21Improvements() {
    carouselScene(
        frames = listOf(
            VersionCompareState.Hidden,
            VersionCompareState.Before,
            VersionCompareState.After,
        ),
    ) {
        Column {
            SampleComparison(
                sample = {
                    Text(
                        """
                            @Test fun test() {
                                assert(listOf("Hello", "World")[1] == "Hello")
                            }
                        """.trimIndent().toKotlin {
                            when (it) {
                                "assert" -> staticFunctionCall
                                else -> null
                            }
                        }
                    )
                },
                beforeVersion = { GradientText("2.0") },
                beforeOutput = {
                    Text(
                        """
                            java.lang.AssertionError: Assertion failed
                            assert(listOf("Hello", "World")[1] == "Hello")
                                   |                           |
                                   |                           false
                                   World
                                   [Hello, World]
                        """.trimIndent()
                    )
                },
                afterVersion = { GradientText("2.1") },
                afterOutput = {
                    Text(
                        """
                            java.lang.AssertionError: Assertion failed
                            assert(listOf("Hello", "World")[1] == "Hello")
                                   |                       |   |
                                   |                       |   false
                                   |                       World
                                   [Hello, World]
                        """.trimIndent()
                    )
                },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { it == VersionCompareState.Hidden }
                },
                showAfter = transition.createChildTransition { frame ->
                    frame.mapToValue(start = false, end = true) { it == VersionCompareState.After }
                },
            )
        }
    }
}

private fun StoryboardBuilder.Kotlin22Improvements() {
    carouselScene(
        frames = listOf(
            VersionCompareState.Hidden,
            VersionCompareState.Before,
            VersionCompareState.After,
        ),
    ) {
        Column {
            SampleComparison(
                sample = {
                    Text(
                        """
                            @Test fun test() {
                                val a : Boolean? = false
                                assert(a!!)
                            }
                        """.trimIndent().toKotlin {
                            when (it) {
                                "assert", "listOf" -> staticFunctionCall
                                else -> null
                            }
                        }
                    )
                },
                beforeVersion = { GradientText("2.1") },
                beforeOutput = {
                    Text(
                        """
                            java.lang.AssertionError: Assertion failed
                            assert(a!!)
                                   ||
                                   |false
                                   false
                        """.trimIndent()
                    )
                },
                afterVersion = { GradientText("2.2") },
                afterOutput = {
                    Text(
                        """
                            java.lang.AssertionError: Assertion failed
                            assert(a!!)
                                   |
                                   |
                                   false
                        """.trimIndent()
                    )
                },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { it == VersionCompareState.Hidden }
                },
                showAfter = transition.createChildTransition { frame ->
                    frame.mapToValue(start = false, end = true) { it == VersionCompareState.After }
                },
            )
        }
    }
}
