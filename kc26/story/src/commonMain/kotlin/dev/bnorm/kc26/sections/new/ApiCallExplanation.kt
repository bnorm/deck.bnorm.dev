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

fun StoryboardBuilder.CallExplanationIntro() {
    carouselScene(CallExplanation.subList(0, CallExplanation.size - 1)) {
        SectionSceneScaffold { padding ->
            ProvideTextStyle(MaterialTheme.typography.code1) {
                MagicCodeSample(transition.createChildTransition { it.toValue() }, modifier = Modifier.padding(padding))
            }
        }
    }
}

fun StoryboardBuilder.CallExplanationTransition() {
    carouselScene(CallExplanation.subList(CallExplanation.size - 2, CallExplanation.size)) {
        SectionSceneScaffold { padding ->
            ProvideTextStyle(MaterialTheme.typography.code1) {
                MagicCodeSample(transition.createChildTransition { it.toValue() }, modifier = Modifier.padding(padding))
            }
        }
    }
}

private val CallExplanation = buildCodeSamples {
    val src by tag("source")
    val arg by tag("arguments")
    val argCls by tag("Argument")
    val seoff by tag("start and end offset")
    val kind by tag("kind")
    val kndCls by tag("Kind")
    val exps by tag("expressions")
    val allExps by tag("all argument expressions")
    val extend by tag("Explanation override")

    val base = """
        class CallExplanation($src
            ${extend}override ${extend}val offset: Int,
            ${extend}override ${extend}val source: String,
        $src$arg    val arguments: List<Argument?>,
        $arg)$argCls$extend : Explanation()$extend {$allExps
            ${extend}override ${extend}val expressions: List<Expression>
                get() = arguments.sortedBy { it?.startOffset }
                    .flatMap { it?.expressions.orEmpty() }
        $allExps
            class Argument($seoff
                val startOffset: Int,
                val endOffset: Int,
            $seoff$kind    val kind: Kind,
            $kind$exps    val expressions: List<Expression>,
            $exps)$kndCls {
                enum class Kind {
                    DISPATCH,
                    CONTEXT,
                    EXTENSION,
                    VALUE,
                }
            }$kndCls
        }$argCls
    """.trimIndent()

    fun String.toCodeSample(): CodeSample {
        return CodeSample(lazy {
            extractTags(this).toKotlin(
                scope = CodeScope.Function,
                identifierStyle = { identifier ->
                    when (identifier) {
                        "filterNotNull", "sortedBy", "flatMap" -> extensionFunctionCall
                        "arguments", "startOffset", "expressions" -> property
                        else -> null
                    }
                }
            )
        })
    }

    base.toCodeSample().hide(tags.toList())
        .then { reveal(src) }
        .then { reveal(arg) }
        .then { reveal(argCls).scroll(argCls) }
        .then { reveal(seoff) }
        .then { reveal(kind) }
        .then { reveal(kndCls) }
        .then { reveal(exps) }
        .then { scroll(null) }
        .then { reveal(allExps) }
        .then { reveal(extend) }
}
