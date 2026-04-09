package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.SampleComparison
import dev.bnorm.kc26.template.SceneTitle
import dev.bnorm.kc26.template.SectionHeader
import dev.bnorm.kc26.template.toKotlin
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.layout.template.enter
import dev.bnorm.storyboard.layout.template.exit
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

    scene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, afterOutput = arrayOutput[1]),
            SceneState(showOutput = true, showAfter = false, afterOutput = arrayOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = arrayOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = arrayOutput[2]),
        ),
        enterTransition = enter(
            start = { fadeIn(tween(durationMillis = 750)) },
            end = SceneEnter(alignment = Alignment.CenterEnd),
        ),
        exitTransition = exit(
            start = { fadeOut(tween(durationMillis = 750)) },
            end = SceneExit(alignment = Alignment.CenterEnd),
        ),
    ) {
        Column {
            SectionHeader(Modifier.sharedElement(rememberSharedContentState(key = SceneTitle))) {
                GradientText("Kotlin 2.4")
            }

            SampleComparison(
                sample = { Text(arraySample) },
                beforeVersion = { GradientText("2.0") },
                beforeOutput = { Text(arrayOutput[0].string) },
                afterVersion = { GradientText("2.4") },
                afterOutput = {
                    MagicText(transition.createChildTransition { it.toValue().afterOutput.string.splitByTags() })
                },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { !it.showOutput }
                },
                showAfter = transition.createChildTransition { frame ->
                    frame.mapToValue(start = false, end = true) { it.showAfter }
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

    scene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, afterOutput = stringOutput[1]),
            SceneState(showOutput = true, showAfter = false, afterOutput = stringOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = stringOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = stringOutput[2]),
        ),
        enterTransition = enter(
            end = { fadeIn(tween(durationMillis = 750)) },
            start = SceneEnter(alignment = Alignment.CenterEnd),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.CenterEnd),
            end = { fadeOut(tween(durationMillis = 750)) },
        ),
    ) {
        Column {
            SectionHeader(Modifier.sharedElement(rememberSharedContentState(key = SceneTitle))) {
                GradientText("Kotlin 2.4")
            }

            SampleComparison(
                sample = { Text(stringSample) },
                beforeVersion = { GradientText("2.0") },
                beforeOutput = { Text(stringOutput[0].string) },
                afterVersion = { GradientText("2.4") },
                afterOutput = {
                    MagicText(transition.createChildTransition { it.toValue().afterOutput.string.splitByTags() })
                },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { !it.showOutput }
                },
                showAfter = transition.createChildTransition { frame ->
                    frame.mapToValue(start = false, end = true) { it.showAfter }
                },
            )
        }
    }
}