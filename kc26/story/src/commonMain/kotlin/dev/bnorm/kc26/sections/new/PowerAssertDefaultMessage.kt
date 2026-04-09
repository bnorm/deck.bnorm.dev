package dev.bnorm.kc26.sections.changing

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.kc26.template.SectionSceneScaffold
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.PowerAssertDefaultMessage() {
    carouselScene(PowerAssertSamples) {
        SectionSceneScaffold { padding ->
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(padding)) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    MagicText(transition.createChildTransition { it.toValue().string.splitByTags() })
                }
            }
        }
    }
}

private val PowerAssertSamples = buildCodeSamples {
    fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(CODE_STYLE) { identifier ->
        when (identifier) {
            "assert", "powerAssert" -> CODE_STYLE.staticFunctionCall
            "trimIndent", "toDefaultMessage" -> CODE_STYLE.extensionFunctionCall
            else -> null
        }
    }

    val e by tag("explanation")
    val f by tag("function name")
    val m by tag("default message")

    val previous = """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        when {
            tmp3 -> {
                val tmp4 = str
                val tmp5 = tmp4[0]
                val tmp6 = tmp5 == 'x'
                ${f}assert${f}(tmp6) {$e
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
            else -> ${f}assert${f}(false) {$e
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

    val start = """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        when {
            tmp3 -> {
                val tmp4 = str
                val tmp5 = tmp4[0]
                val tmp6 = tmp5 == 'x'
                ${f}assert${f}(tmp6) {$e
                    CallExplanation(/* ... */)${m}.toDefaultMessage()${m}
                $e}
            }
            else -> ${f}assert${f}(false) {$e
                CallExplanation(/* ... */)${m}.toDefaultMessage()${m}
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
                ${f}powerAssert${f}(tmp6) {$e
                    CallExplanation(/* ... */)${m}.toDefaultMessage()${m}
                $e}
            }
            else -> ${f}powerAssert${f}(false) {$e
                CallExplanation(/* ... */)${m}.toDefaultMessage()${m}
            $e}
        }
    """.toCodeSample()

    previous.collapse(e)
        .then { reveal(e) }
        .then { start }
}