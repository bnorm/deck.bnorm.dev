package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.OutputComparison
import dev.bnorm.kc26.components.SceneCodeSample
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.mapToValue
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.FutureSection() {
    LocalVariables()
    // TODO arguments?
    // TODO diffs?
}

private fun StoryboardBuilder.LocalVariables() {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(
            codeStyle = CODE_STYLE,
            scope = CodeScope.Function,
        ) { identifier ->
            when (identifier) {
                "powerAssert" -> CODE_STYLE.staticFunctionCall
                "trimIndent" -> CODE_STYLE.extensionFunctionCall
                "length" -> CODE_STYLE.property
                else -> null
            }
        }

        val s by tag("splitter")

        listOf(
            """
                @Test fun test() {
                    powerAssert(${s}"Hello".length${s} == ${s}"World".substring(1, 4).length${s})
                }
            """.toCodeSample(),
            """
                @Test fun test() {
                    ${s}${s}val expected = ${s}"Hello".length${s}
                    ${s}${s}val actual = ${s}"World".substring(1, 4).length${s}
                    powerAssert(${s}expected${s} == ${s}actual${s})
                }
            """.toCodeSample(),
            """
                @Test fun test() {
                    ${s}@PowerAssert ${s}val expected = ${s}"Hello".length${s}
                    ${s}@PowerAssert ${s}val actual = ${s}"World".substring(1, 4).length${s}
                    powerAssert(${s}expected${s} == ${s}actual${s})
                }
            """.toCodeSample(),
        )
    }

    val outputs = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                powerAssert(expected == actual)
                            |        |  |
                            5        ${s}|${s}  3
                                     ${s}false${s}

                Expected :3
                Actual   :5
                <Click to see difference>
            """.toCodeSample(),
            """
                val expected ${s}= "Hello".length${s}
                                       ${s}|${s}
                                       ${s}5${s}
                val actual ${s}= "World".substring(1, 4).length${s}
                                     ${s}|               |${s}
                                     ${s}"orl"           3${s}
                powerAssert(expected == actual)
                            |        |  |
                            5        ${s}|${s}  3
                                     ${s}false${s}

                Expected :3
                Actual   :5
                <Click to see difference>
            """.toCodeSample(),
            """
                powerAssert(expected == actual)
                            |        |  |
                            |        ${s}|${s}  ${s}= "World".substring(1, 4).length${s}
                            |        |            ${s}|               |${s}
                            |        |            ${s}"orl"           3${s}
                            |        ${s}false${s}
                            ${s}= "Hello".length${s}
                                      ${s}|${s}
                                      ${s}5${s}

                Expected :3
                Actual   :5
                <Click to see difference>
            """.toCodeSample(),
        )
    }

    data class SceneState(
        val showOutput: Boolean,
        val showAfter: Boolean,
        val sample: CodeSample,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, sample = samples[0], afterOutput = outputs[1]),
            SceneState(showOutput = false, showAfter = false, sample = samples[1], afterOutput = outputs[1]),
            SceneState(showOutput = true, showAfter = false, sample = samples[1], afterOutput = outputs[1]),
            SceneState(showOutput = true, showAfter = false, sample = samples[2], afterOutput = outputs[1]),
            SceneState(showOutput = true, showAfter = true, sample = samples[2], afterOutput = outputs[1]),
            SceneState(showOutput = true, showAfter = true, sample = samples[2], afterOutput = outputs[2]),
        ),
    ) {
        Column {
            SceneCodeSample(
                content = { MagicText(transition.createChildTransition { it.toValue().sample.string.splitByTags() }) },
                output = {
                    OutputComparison(
                        beforeVersion = { GradientText("2.4") },
                        beforeOutput = { Text(outputs[0].string) },
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
    }
}
