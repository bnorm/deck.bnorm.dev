package dev.bnorm.kc26.sections.new

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Modifier
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.MagicCodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.SectionSceneScaffold
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.code1
import dev.bnorm.kc26.template.toKotlin
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.PowerAssertAnnotationIntro() {
    carouselScene(PowerAssert) {
        SectionSceneScaffold { padding ->
            ProvideTextStyle(MaterialTheme.typography.code1) {
                MagicCodeSample(transition.createChildTransition { it.toValue() }, modifier = Modifier.padding(padding))
            }
        }
    }
}

private val PowerAssert = buildCodeSamples {
    val target by tag("target")
    val explanation by tag("explanation")

    val base = """
        ${target}@Target(AnnotationTarget.FUNCTION)
        ${target}annotation class PowerAssert$explanation {
            companion object {
                val explanation: CallExplanation? // Implemented as compiler-plugin intrinsic.
            }
        }$explanation
    """.trimIndent()

    fun String.toCodeSample(): CodeSample {
        return CodeSample(lazy {
            extractTags(this).toKotlin(
                scope = CodeScope.Function,
                identifierStyle = { identifier ->
//                    when (identifier) {
//                        "sortedBy", "flagMap" -> CODE_STYLE.extensionFunctionCall
//                        "arguments", "startOffset", "expressions" -> CODE_STYLE.property
//                        else -> null
//                    }
                    null
                }
            )
        })
    }

    base.toCodeSample().hide(tags.toList())
        .then { reveal(target) }
        .then { reveal(explanation) }
}