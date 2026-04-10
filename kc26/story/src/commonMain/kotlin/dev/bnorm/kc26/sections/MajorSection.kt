package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.OutputComparison
import dev.bnorm.kc26.components.SceneCodeSample
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.toKotlin
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.mapToValue
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.MajorSection() {
    // TICKETS:
    // https://youtrack.jetbrains.com/issue/KT-75266/PowerAssert-arrayOf-isnt-displayed-on-the-diagram
    // https://youtrack.jetbrains.com/issue/KT-69036/Power-Assert-indent-multiline-values

    ArrayProblem()
    MultilineProblem()
    CallExplanationSolution()
    // TODO talk about the new API that makes this all possible
    // TODO talk about the Gradle improvements a little?
}

private fun StoryboardBuilder.ArrayProblem() {
    val arraySample = """
        @Test fun test() {
            assert(arrayOf(true, false)[1])
        }
    """.trimIndent().toKotlin {
        when (it) {
            "assert", "arrayOf" -> staticFunctionCall
            else -> null
        }
    }

    val arrayOutput = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                assert(arrayOf(true, false)[1])
                       |                   |
                       |                   false
                       [Ljava.lang.Boolean;@47caedad
            """.toCodeSample(),
            """
                assert(arrayOf(true, false)[1])
                       |                   |
                       |                   ${s}false${s}
                       ${s}[true, false]${s}
            """.toCodeSample(),
            """
                assert(arrayOf(true, false)[1])
                       |                   |
                       ${s}[true, false]${s}       ${s}false${s}
            """.toCodeSample(),
        )
    }

    data class SceneState(
        val showOutput: Boolean,
        val showAfter: Boolean,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, afterOutput = arrayOutput[1]),
            SceneState(showOutput = true, showAfter = false, afterOutput = arrayOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = arrayOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = arrayOutput[2]),
        ),
    ) {
        Column {
            SceneCodeSample(
                content = { Text(arraySample) },
                output = {
                    OutputComparison(
                        beforeVersion = { GradientText("2.0") },
                        beforeOutput = { Text(arrayOutput[0].string) },
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
}

private fun StoryboardBuilder.MultilineProblem() {
    val stringSample = """
        @Test fun test() {
            val a = "another string"
            val s = "this is a multiline\nstring. this is the second line"
            assert(a == s)
        }
    """.trimIndent().toKotlin {
        when (it) {
            "assert" -> staticFunctionCall
            else -> null
        }
    }

    val stringOutput = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                assert(a == s)
                       | |  |
                       | |  this is a multiline
                string. this is the second line
                       | false
                       another string
            """.toCodeSample(),
            """
                assert(a == s)
                       | |  |
                       | |  this is a multiline
                       | |  string. this is the second line
                       | false
                       ${s}another string${s}
            """.toCodeSample(),
            """
                assert(a == s)
                       | |  |
                       | |  ""${'"'}
                       | |  this is a multiline
                       | |  string. this is the second line
                       | |  ""${'"'}
                       | false
                       "${s}another string${s}"
            """.toCodeSample(),
        )
    }

    data class SceneState(
        val showOutput: Boolean,
        val showAfter: Boolean,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, afterOutput = stringOutput[1]),
            SceneState(showOutput = true, showAfter = false, afterOutput = stringOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = stringOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = stringOutput[2]),
        ),
    ) {
        Column {
            SceneCodeSample(
                content = { Text(stringSample) },
                output = {
                    OutputComparison(
                        beforeVersion = { GradientText("2.0") },
                        beforeOutput = { Text(stringOutput[0].string) },
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
}

private fun StoryboardBuilder.CallExplanationSolution() {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(CODE_STYLE) { identifier ->
            when (identifier) {
                "assert" -> CODE_STYLE.staticFunctionCall
                "trimIndent", "toDefaultMessage" -> CODE_STYLE.extensionFunctionCall
                "length" -> CODE_STYLE.property
                else -> null
            }
        }

        val e by tag("explanation")
        val m by tag("default message")

        val start = """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6) {$e
                        "${'"'}"
                            assert(str.length >= 1 && str[0] == 'x')
                                   |   |      |       |  |   |
                                   |   |      |       |  |   ${'$'}tmp6
                                   |   |      |       |  ${'$'}tmp5
                                   |   |      |       ${'$'}tmp4
                                   |   |      ${'$'}tmp3
                                   |   ${'$'}tmp2
                                   ${'$'}tmp1
                        "${'"'}".trimIndent()
                    $e}
                }
                else -> assert(false) {$e
                    "${'"'}"
                        assert(str.length >= 1 && str[0] == 'x')
                               |   |      |
                               |   |      ${'$'}tmp3
                               |   ${'$'}tmp2
                               ${'$'}tmp1
                    "${'"'}".trimIndent()
                $e}
            }
        """.toCodeSample()

        val end = """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6) {$e
                        CallExplanation(/* ... */)${m}.toDefaultMessage()${m}
                    $e}
                }
                else -> assert(false) {$e
                    CallExplanation(/* ... */)${m}.toDefaultMessage()${m}
                $e}
            }
        """.toCodeSample()

        start.collapse(e)
            .then { reveal(e) }
            .then { end }
    }

    carouselScene(frames = samples) {
        SceneCodeSample {
            Box(Modifier.fillMaxSize()) {
                MagicText(transition.createChildTransition { it.toValue().string.splitByTags() })
            }
        }
    }
}