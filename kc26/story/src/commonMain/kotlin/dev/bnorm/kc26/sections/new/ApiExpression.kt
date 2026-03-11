package dev.bnorm.kc26.sections.new

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Modifier
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.MagicCodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.*
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.ExpressionIntro() {
    carouselScene(Expression.subList(0, Expression.size - 1)) {
        SectionSceneScaffold { padding ->
            ProvideTextStyle(MaterialTheme.typography.code1) {
                MagicCodeSample(transition.createChildTransition { it.toValue() }, modifier = Modifier.padding(padding))
            }
        }
    }
}

private val Expression = buildCodeSamples {
    val offsets by tag("offsets")
    val value by tag("value")
    val copy by tag("copy")

    val base = """
        abstract class Expression($offsets
            val startOffset: Int,
            val endOffset: Int,
            val displayOffset: Int,
        $offsets$value    val value: Any?,
        $value)$copy {
            abstract fun copy(deltaOffset: Int): Expression
        }$copy
    """.trimIndent()

    fun String.toCodeSample(): CodeSample {
        return CodeSample(lazy {
            extractTags(this).toKotlin(
                scope = CodeScope.Function,
                identifierStyle = { identifier ->
                    when (identifier) {
                        "sortedBy", "flagMap" -> CODE_STYLE.extensionFunctionCall
                        "arguments", "startOffset", "expressions" -> CODE_STYLE.property
                        else -> null
                    }
                }
            )
        })
    }

    base.toCodeSample().hide(tags.toList())
        .then { reveal(offsets) }
        .then { reveal(value) }
        .then { reveal(copy) }
}
