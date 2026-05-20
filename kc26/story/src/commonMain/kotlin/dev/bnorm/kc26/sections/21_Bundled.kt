package dev.bnorm.kc26.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.ResourceImage
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.qr_power_assert
import dev.bnorm.kc26.components.*
import dev.bnorm.kc26.samples.BasicSample
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.mapToValue
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.BundledSection() {
    PowerAssertIntro()
    Kotlin20Output()
    Kotlin20Gradle()
}

private fun StoryboardBuilder.PowerAssertIntro() {
    carouselScene(frameCount = 4) {
        Timeline(current = TimelineState.Bundled)
        Summary {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Power-Assert is a compiler plugin with a few key features:")
                RevealEach(transition.createChildTransition { it.toValue() - 1 }) {
                    item {
                        Row(modifier = Modifier.padding(start = 16.dp)) {
                            Text(BULLET_1)
                            Text(buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Enhanced error messages: ")
                                }
                                append("The plugin captures and displays the values of variables and sub-expressions within the assertion to clearly identify the cause of failure.")
                            })
                        }
                    }
                    item {
                        Row(modifier = Modifier.padding(start = 16.dp)) {
                            Text(BULLET_1)
                            Text(buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Simplified testing: ")
                                }
                                append("Automatically generates informative failure messages, reducing the need for complex assertion libraries.")
                            })
                        }
                    }
                    item {
                        Row(modifier = Modifier.padding(start = 16.dp)) {
                            Text(BULLET_1)
                            Text(buildAnnotatedString {
                                val codeSpan = SpanStyle(fontFamily = FontFamily.Monospace)

                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Support for multiple functions: ")
                                }
                                append("By default, it transforms ")
                                withStyle(codeSpan) { append("assert()") }
                                append(" function calls but can also transform other functions, such as ")
                                withStyle(codeSpan) { append("require()") }
                                append(", ")
                                withStyle(codeSpan) { append("check()") }
                                append(", and ")
                                withStyle(codeSpan) { append("assertTrue()") }
                                append(".")
                            }, )
                        }
                    }
                }
            }
        }
    }
}

private fun StoryboardBuilder.Kotlin20Output() {
    carouselScene(frameCount = BasicSample.outputs.size + 1) {
        Timeline(current = TimelineState.Bundled)
        SceneCodeSample(
            output = {
                OutputComparison(
                    version = { GradientText("2.0") },
                    output = {
                        MagicText(transition.createChildTransition {
                            BasicSample.outputs[(it.toValue() - 1).coerceAtLeast(0)].string.splitByTags()
                        })
                    },
                )
            },
            hideOutput = transition.createChildTransition { frame ->
                frame.mapToValue(start = true, end = true) { it < 1 }
            },
            content = {
                MagicText(transition.createChildTransition { BasicSample.samples[(it.toValue() - 3).coerceAtLeast(0)].string.splitByTags() })
            },
        )
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
        Timeline(current = TimelineState.Bundled)
        SceneCodeSample {
            Box(Modifier.fillMaxSize()) {
                MagicText(transition.createChildTransition { it.toValue().string.splitByTags() })

                transition.AnimatedVisibility(
                    visible = { it.toValue() !== frames.first() },
                    modifier = Modifier.align(Alignment.BottomEnd),
                    // Match the MagicText animation.
                    enter = fadeIn(tween(300, delayMillis = 600, easing = EaseInCubic)),
                    exit = fadeOut(tween(300, easing = EaseOutCubic)),
                ) {
                    ResourceImage(
                        Res.drawable.qr_power_assert,
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .height(98.dp)
                    )
                }
            }
        }
    }
}
