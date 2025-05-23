package dev.bnorm.kc25.sections.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.deck.shared.INTELLIJ_DARK
import dev.bnorm.kc25.template.HeaderScaffold
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.section
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.magic.toWords
import kotlin.time.Duration.Companion.seconds

private val SAMPLES = buildCodeSamples {
    val samples = listOf(
        """
            assert(str.length >= 1 && str[0] == 'x')
        """.trimIndent(),
        """
            val tmp1 = str
            assert(tmp1.length >= 1 && str[0] == 'x')
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            assert(tmp2 >= 1 && str[0] == 'x')
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            assert(tmp3 && str[0] == 'x')
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            assert(when {
                tmp3 -> str[0] == 'x'
                else -> false
            })
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> assert(str[0] == 'x')
                else -> assert(false)
            }
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    assert(tmp4[0] == 'x')
                }
                else -> assert(false)
            }
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    assert(tmp5 == 'x')
                }
                else -> assert(false)
            }
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6)
                }
                else -> assert(false)
            }
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6)
                }
                else -> assert(false) {
                    "${'"'}"
                        assert(str.length >= 1 && str[0] == 'x')
                               |   |      |
                               |   |      ${'$'}tmp3
                               |   ${'$'}tmp2
                               ${'$'}tmp1
                    "${'"'}".trimIndent()
                }
            }
        """.trimIndent(),
    )

    val m by tag("message")
    val finalExample = """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        when {
            tmp3 -> {
                val tmp4 = str
                val tmp5 = tmp4[0]
                val tmp6 = tmp5 == 'x'
                assert(tmp6) {$m
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
                $m}
            }
            else -> assert(false) {$m
                "${'"'}"
                    assert(str.length >= 1 && str[0] == 'x')
                           |   |      |
                           |   |      ${'$'}tmp3
                           |   ${'$'}tmp2
                           ${'$'}tmp1
                "${'"'}".trimIndent()
            $m}
        }
    """.trimIndent()

    fun String.toCodeSample(): CodeSample {
        return toCodeSample(
            scope = CodeScope.Function,
            identifierType = { _, identifier ->
                when (identifier) {
                    "assert" -> INTELLIJ_DARK.staticFunctionCall
                    "trimIndent" -> INTELLIJ_DARK.extensionFunctionCall
                    "length" -> INTELLIJ_DARK.property
                    else -> null
                }
            })
    }

    samples.map { it.toCodeSample() }
        .then { finalExample.toCodeSample().attach(5.seconds) }
        .then { collapse(m).attach(3.seconds) }
}

fun StoryboardBuilder.PowerAssertExample(sink: MutableList<CodeSample>) {
    sink.addAll(SAMPLES)
    section("Power-Assert") {
        scene(
            enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
            exitTransition = SceneExit(alignment = Alignment.CenterEnd),
        ) {
            val index by animateSampleIndex(samples = SAMPLES, defaultDelay = 2.seconds)

            HeaderScaffold { padding ->
                Box(Modifier.padding(padding)) {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicText(SAMPLES[index].string.toWords())
                    }
                }
            }
        }
    }
}
