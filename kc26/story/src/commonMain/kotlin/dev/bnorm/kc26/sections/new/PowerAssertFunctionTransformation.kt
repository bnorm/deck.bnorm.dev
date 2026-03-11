package dev.bnorm.kc26.sections.new

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.*
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.PowerAssertFunctionTransformation() {
    carouselScene(ORIGINAL_FUN_SAMPLES.size + SYNTHETIC_FUN_SAMPLES.size) {
        SectionSceneScaffold { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    transition.createChildTransition { it.toValue() == 0 }.AnimatedContent(
                        transitionSpec = {
                            fun <T> spec() = tween<T>(500, easing = EaseInOut)
                            (slideInVertically(spec()) + fadeIn(spec())) togetherWith
                                    (slideOutVertically(spec()) + fadeOut(spec()))
                        }
                    ) {
                        when (it) {
                            true -> Box(Modifier.fillMaxSize()) {
                                OriginalFunction()
                            }

                            false -> Column(Modifier.fillMaxSize()) {
                                OriginalFunction(Modifier.weight(8f))
                                Spacer(Modifier.height(16.dp))
                                SyntheticFunction(Modifier.weight(12f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun SceneScope<Int>.OriginalFunction(modifier: Modifier = Modifier.Companion) {
    Box(modifier.sharedElement(rememberSharedContentState("original-fun"))) {
        MagicText(transition.createChildTransition {
            ORIGINAL_FUN_SAMPLES[(it.toValue() - 1).coerceIn(ORIGINAL_FUN_SAMPLES.indices)].string.splitByTags()
        })
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun SceneScope<Int>.SyntheticFunction(modifier: Modifier = Modifier.Companion) {
    Box(modifier.sharedElement(rememberSharedContentState("synthetic-fun"))) {
        MagicText(transition.createChildTransition {
            SYNTHETIC_FUN_SAMPLES[(it.toValue() - 2).coerceAtLeast(0)].string.splitByTags()
        })
    }
}

private val ORIGINAL_FUN_SAMPLES = buildCodeSamples {
    fun String.toCodeSample(): CodeSample {
        return CodeSample(lazy {
            extractTags(this).toKotlin {
                when (it) {
                    "explanation" -> CODE_STYLE.staticProperty
                    "PowerAssert" -> CODE_STYLE.annotation
                    else -> null
                }
            }
        })
    }

    val exp by tag("explanation call")

    listOf(
        """
            @PowerAssert fun powerAssert(condition: Boolean) {
                if (!condition) {
                    val explanation: CallExplanation? = ${exp}PowerAssert.explanation${exp}
                        ?: throw AssertionError("Assertion failed")
                    // ...
                }
            }
        """.trimIndent().toCodeSample(),
        """
            @PowerAssert fun powerAssert(condition: Boolean) {
                if (!condition) {
                    val explanation: CallExplanation? = ${exp}null${exp}
                        ?: throw AssertionError("Assertion failed")
                    // ...
                }
            }
        """.trimIndent().toCodeSample(),
    )
}

private val SYNTHETIC_FUN_SAMPLES = buildCodeSamples {
    fun String.toCodeSample(): CodeSample {
        return CodeSample(lazy {
            extractTags(this).toKotlin(identifierStyle = {
                when (it) {
                    "explanation" -> CODE_STYLE.staticProperty
                    "PowerAssert" -> CODE_STYLE.annotation
                    else -> null
                }
            })
        })
    }

    val exp by tag("explanation call")
    val invoke by tag("explanation call")
    val name by tag("explanation call")
    val ann by tag("explanation call")
    val format by tag("explanation call")
    val param by tag("explanation call")
    val hide by tag("explanation call")

    val base = """
        ${ann}@PowerAssert ${ann}fun ${name}`${name}powerAssert${name}${'$'}powerassert`${name}($format
            ${format}condition: Boolean${param},
            `${'$'}explanation`: () -> CallExplanation${param}${format}
        ${format}) {
            if (!condition) {
                val explanation: CallExplanation? = ${exp}PowerAssert.explanation${exp}${hide} ?: ${hide}${invoke}`${'$'}explanation`.invoke()${invoke}
                    ?: throw AssertionError("Assertion failed")
                // ...
            }
        }
    """.trimIndent().toCodeSample()

    base.hide(name, format, param, hide, invoke)
        .then { reveal(name) }
        .then { hide(ann) }
        .then { reveal(format, param) }
        .then { hide(exp).reveal(invoke) }
}