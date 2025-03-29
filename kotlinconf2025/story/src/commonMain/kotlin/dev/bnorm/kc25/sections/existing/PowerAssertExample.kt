package dev.bnorm.kc25.sections.existing

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.HeaderAndBody
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code.toCode
import dev.bnorm.storyboard.core.DisplayType
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.section
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.highlight.Highlighting
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.magic.toWords
import dev.bnorm.storyboard.ui.LocalDisplayType
import dev.bnorm.storyboard.ui.StoryEffect
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
    val finalExample = extractTags(
        """
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
        """.trimIndent(),
    )

    val identifierType: (Highlighting, String) -> SpanStyle? = { highlighting, identifier ->
        when (identifier) {
            "assert" -> highlighting.staticFunctionCall
            "trimIndent" -> highlighting.extensionFunctionCall
            "length" -> highlighting.property
            else -> null
        }
    }

    // TODO formatting bug with `$` in a string
    samples.map { CodeSample { it.toCode(identifierType) } }
        .then { CodeSample { finalExample.toCode(identifierType) } }
        .then { collapse(m) }
}

fun StoryboardBuilder.PowerAssertExample() {
    section("Power-Assert") {
        scene(
            stateCount = 1,
            enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
            exitTransition = SceneExit(alignment = Alignment.CenterEnd),
        ) {
            HeaderAndBody {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(horizontal = 64.dp, vertical = 32.dp),
                ) {
                    ProvideTextStyle(MaterialTheme.typography.h5) {
                        // TODO add some bullet points?
                    }
                }
            }
        }

        scene(
            enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
            exitTransition = SceneExit(alignment = Alignment.CenterEnd),
        ) {
            val displayType = LocalDisplayType.current
            HeaderAndBody {
                // TODO could I hide some animation controls, to make them pausable and navigable?
                // When rendering the scene for preview, render the finished state and do not animate the sample.
                var sampleIndex by remember {
                    mutableIntStateOf(if (displayType == DisplayType.Story) 0 else SAMPLES.lastIndex)
                }
                val sampleTransition = updateTransition(sampleIndex)

                StoryEffect(Unit) {
                    while (true) {
                        delay(2.seconds)
                        if (sampleIndex == SAMPLES.lastIndex) {
                            delay(3.seconds)
                            sampleIndex = 0
                        } else {
                            sampleIndex += 1
                        }
                    }
                }

                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 64.dp)
                        .padding(top = 32.dp)
                        .wrapContentHeight(align = Alignment.Top, unbounded = true)
                ) {
                    MagicText(sampleTransition.createChildTransition { SAMPLES[it].get().toWords() })
                }
            }
        }
    }
}
