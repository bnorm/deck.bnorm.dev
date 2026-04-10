package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.MagicCodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.OutputComparison
import dev.bnorm.kc26.components.SceneCodeSample
import dev.bnorm.kc26.components.VersionCompareState
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.toKotlin
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.mapToValue
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.StartSection() {
    Kotlin20Output()
    Kotlin20Gradle()
    // TODO what else do we need to talk about here?
    //  - function signature requirements?
}

private fun StoryboardBuilder.Kotlin20Output() {
    val sample = """
        @Test fun test() {
            val hello = "Hello"
            assert(hello.length == "World".substring(1, 4).length)
        }
    """.trimIndent().toKotlin {
        when (it) {
            "substring" -> extensionFunctionCall
            "length" -> property
            "assert" -> staticFunctionCall
            else -> null
        }
    }

    val output = """
        assert(hello.length == "World".substring(1, 4).length)
               |     |      |          |               |
               |     |      |          |               3
               |     |      |          orl
               |     |      false
               |     5
               Hello
    """.trimIndent()

    carouselScene(
        frames = listOf(
            VersionCompareState.Hidden,
            VersionCompareState.Before,
        ),
    ) {
        Column {
            SceneCodeSample(
                output = {
                    OutputComparison(
                        version = { GradientText("2.0") },
                        output = { Text(output) },
                    )
                },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { it == VersionCompareState.Hidden }
                },
                content = { Text(sample) },
            )
        }
    }
}

private fun StoryboardBuilder.Kotlin20Gradle() {
    val gradle = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(
            codeStyle = CODE_STYLE,
            scope = CodeScope.Function,
        ) { identifier ->
            when (identifier) {
                "class" -> CODE_STYLE.keyword
                "ExperimentalKotlinGradlePluginApi" -> CODE_STYLE.annotation
                "kotlin", "version", "powerAssert" -> CODE_STYLE.extensionFunctionCall // TODO infix function is not detected
                "functions", "includedSourceSets" -> CODE_STYLE.property
                "listOf" -> CODE_STYLE.staticFunctionCall
                "plugins", "linuxX64" -> CODE_STYLE.simple + SpanStyle(color = Color(0xFF800000))  // DSL
                else -> null
            }
        }

        val p by tag("plugin")
        val e by tag("extension")
        val f by tag("functions")
        val t by tag("kotlin-test")
        val s by tag("source-sets")

        val base = """
            plugins {
                kotlin("multiplatform") version "2.0.0"${p}
                kotlin("plugin.power-assert") version "2.0.0"${p}
            }${e}
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {${f}
                functions = listOf(
                    "kotlin.assert",${t}
                    "kotlin.test.assertTrue",
                    "kotlin.test.assertEquals",
                    "kotlin.test.assertNotNull",${t}
                )${f}${s}
                includedSourceSets = listOf("...")${s}
            }${e}
        """.toCodeSample()


        val start = base.hide(tags.toList())
        start
            .then { reveal(p) }
            .then { reveal(e) }
            .then { reveal(f) }
            .then { reveal(t) }
            .then { reveal(s) }
    }

    carouselScene(frames = gradle) {
        SceneCodeSample(content = {
            Box(Modifier.fillMaxSize()) {
                MagicCodeSample(transition.createChildTransition { it.toValue() })
            }
        })
    }
}
