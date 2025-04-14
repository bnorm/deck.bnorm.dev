package dev.bnorm.kc25.sections.existing

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.KodeeScaffold
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.SceneMode
import dev.bnorm.storyboard.LocalSceneMode
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.StoryEffect
import dev.bnorm.storyboard.easel.template.section
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.highlight.CodeStyle
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.magic.toWords
import kotlinx.coroutines.delay
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

    val identifierType: (CodeStyle, String) -> SpanStyle? = { highlighting, identifier ->
        when (identifier) {
            "assert" -> highlighting.staticFunctionCall
            "trimIndent" -> highlighting.extensionFunctionCall
            "length" -> highlighting.property
            else -> null
        }
    }

    // TODO formatting bug with `$` in a string
    samples.map { it.toCodeSample(INTELLIJ_DARK_CODE_STYLE, scope = CodeScope.Function, identifierType) }
        .then { finalExample.toCodeSample(INTELLIJ_DARK_CODE_STYLE, scope = CodeScope.Function, identifierType) }
        .then { collapse(m) }
}

fun StoryboardBuilder.PowerAssertExample() {
    section("Power-Assert") {
        scene(
            stateCount = 1,
            enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
            exitTransition = SceneExit(alignment = Alignment.CenterEnd),
        ) {
            KodeeScaffold { padding ->
                Column(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // TODO add some bullet points?
                }
            }
        }

        scene(
            enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
            exitTransition = SceneExit(alignment = Alignment.CenterEnd),
        ) {
            // TODO could I hide some animation controls, to make them pausable and navigable?
            // When rendering the scene for preview, render the finished state and do not animate the sample.
            val sceneMode = LocalSceneMode.current
            var sampleIndex by remember {
                mutableIntStateOf(if (sceneMode == SceneMode.Story) 0 else SAMPLES.lastIndex)
            }
            val sampleTransition = updateTransition(sampleIndex)

            StoryEffect(Unit) {
                while (true) {
                    delay(2.seconds)
                    if (sampleIndex == SAMPLES.lastIndex) {
                        delay(3.seconds)
                        sampleIndex = 0
                    } else if (sampleIndex == SAMPLES.lastIndex - 1 || sampleIndex == SAMPLES.lastIndex - 2) {
                        delay(1.seconds)
                        sampleIndex += 1
                    } else {
                        sampleIndex += 1
                    }
                }
            }

            KodeeScaffold { padding ->
                Box(Modifier.padding(padding)) {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicText(sampleTransition.createChildTransition { SAMPLES[it].string.toWords() })
                    }
                }
            }
        }
    }
}
