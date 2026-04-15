package dev.bnorm.kc26.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.ResourceImage
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.qr_power_assert
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

fun StoryboardBuilder.StartSection() {
    Kotlin20Gradle(initial = true)
    Kotlin20Output()
    Kotlin20Gradle(initial = false)
}

private fun StoryboardBuilder.Kotlin20Output() {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(
            codeStyle = CODE_STYLE,
        ) { identifier ->
            when (identifier) {
                "assert", "assertTrue", "assertEquals" -> CODE_STYLE.staticFunctionCall
                "substring" -> CODE_STYLE.extensionFunctionCall
                "length" -> CODE_STYLE.property
                else -> null
            }
        }

        val s by tag("splitter")
        val base = """
            @Test fun test() {
                val hello = "Hello"
                assert${s}${s}(hello.length${s} == ${s}"World".substring(1, 4).length)
            }
        """.trimIndent()

        listOf(
            base.toCodeSample(),
            base.replace("assert", "assert${s}True${s}").toCodeSample(),
            base.replace("assert", "assert${s}Equals${s}").replace(" == ", ", ").toCodeSample(),
        )
    }

    val outputs = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                java.lang.AssertionError:${s} Assertion failed${s}
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}${s}(hello.length${s} == ${s}"World".substring(1, 4).length)
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}${s}(hello.length${s} == ${s}"World".substring(1, 4).length)
                       |     |      ${s}|    ${s}      |               |
                       |     |      ${s}|    ${s}      |               3
                       |     |      ${s}|    ${s}      orl
                       |     |      ${s}false${s}
                       |     5
                       Hello
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}True${s}(hello.length${s} == ${s}"World".substring(1, 4).length)
                           |     |      ${s}|    ${s}      |               |
                           |     |      ${s}|    ${s}      |               3
                           |     |      ${s}|    ${s}      orl
                           |     |      ${s}false${s}
                           |     5
                           Hello
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}Equals${s}(hello.length${s}, ${s}"World".substring(1, 4).length)
                             |     |      ${s}   ${s}      |               |
                             |     |      ${s}   ${s}      |               3
                             |     |      ${s}   ${s}      orl
                             |     |      ${s}   ${s}
                             |     5
                             Hello
        
                Expected :5
                Actual   :3
                <Click to see difference>
            """.toCodeSample(),
        )
    }

    carouselScene(frameCount = 6) {
        SceneCodeSample(
            output = {
                OutputComparison(
                    version = { GradientText("2.0") },
                    output = {
                        MagicText(transition.createChildTransition {
                            outputs[(it.toValue() - 1).coerceAtLeast(0)].string.splitByTags()
                        })
                    },
                )
            },
            hideOutput = transition.createChildTransition { frame ->
                frame.mapToValue(start = true, end = true) { it < 1 }
            },
            content = {
                MagicText(transition.createChildTransition { samples[(it.toValue() - 3).coerceAtLeast(0)].string.splitByTags() })
            },
        )
    }
}

private fun StoryboardBuilder.Kotlin20Gradle(initial: Boolean) {
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

    val frames = when (initial) {
        true -> gradle.subList(0, 2)
        false -> gradle.subList(1, gradle.size)
    }
    carouselScene(
        frames = frames
    ) {
        SceneCodeSample(content = {
            Box(Modifier.fillMaxSize()) {
                MagicText(transition.createChildTransition { it.toValue().string.splitByTags() })

                if (!initial) {
                    transition.AnimatedVisibility(
                        visible = { it.toValue() !== frames.first() },
                        modifier = Modifier.align(Alignment.BottomEnd),
                        // Match the MagicText animation.
                        enter = fadeIn(tween(300, delayMillis = 600, easing = EaseInCubic)),
                        exit = fadeOut(tween(300, easing = EaseOutCubic)),
                    ) {
                        ResourceImage(
                            Res.drawable.qr_power_assert,
                            modifier = Modifier.height(120.dp)
                        )
                    }
                }
            }
        })
    }
}
