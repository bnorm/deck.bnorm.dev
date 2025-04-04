package dev.bnorm.kc25.sections.existing

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.kc25.template.Body
import dev.bnorm.kc25.template.Header
import dev.bnorm.kc25.template.KodeeScene
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code.toCode
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.core.DisplayType
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.section
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.magic.toWords
import dev.bnorm.storyboard.ui.LocalDisplayType
import dev.bnorm.storyboard.ui.StoryEffect
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

private val SAMPLES = buildCodeSamples {
    val namedArguments = SpanStyle(color = Color(0xFF56C1D6))
    val n by tag("named arguments")

    val c1 by tag("composer")
    val c2 by tag("composer calls")
    val c3 by tag("changed")
    val c4 by tag("check skip")
    val c5 by tag("update scope")

    // TODO named parameters aren't highlighted correctly...
    val basic = extractTags(
        """
            @Composable fun MyCounter(${c1}composer: Composer${c1}${c3}, changed: Int${c3}) {${c2}
              composer.startRestartGroup(123)${c2}
              var count by remember(${c1}composer${c1}${c3}, 0${c3}) { mutableStateOf(0) }
              MyButton(${c1}
                ${n}composer = ${n}composer,${c1}${c3}
                ${n}changed = ${n}0,${c3}
                ${n}text = ${n}"Count: ${'$'}count",
                ${n}onPress = ${n}{ count += 1 }
              )${c2}
              composer.endRestartGroup()${c2}
            }
        """.trimIndent()
    )

    val restartable = extractTags(
        """
            @Composable fun MyCounter(composer: Composer, changed: Int) {
              composer.startRestartGroup(123)
              ${c4}if (changed == 0 && composer.getSkipping()) {
                composer.skipToGroupEnd()
              }${c4} else {
                var count by remember(composer, 0) { mutableStateOf(0) }
                MyButton(
                  ${n}composer = ${n}composer,
                  ${n}changed = ${n}0,
                  ${n}text = ${n}"Count: ${'$'}count",
                  ${n}onPress = ${n}{ count += 1 }
                )
              }
              composer.endRestartGroup()${c5}?.let {
                it.updateScope { composer, _ -> MyCounter(composer, changed or 1) }
              }${c5}
            }
        """.trimIndent()
    )

    CodeSample { basic.toCode() }.styled(n, namedArguments).hide(c1, c2, c3)
        .then { reveal(c1).focus(c1) }
        .then { reveal(c2).focus(c2) }
        .then { reveal(c3).focus(c3) }
        .then { CodeSample { restartable.toCode() }.styled(n, namedArguments).hide(c5).focus(c4) }
        .then { reveal(c5).focus(c5) }
        .then { unfocus() }
}

fun StoryboardBuilder.ComposeExample() {
    section("Compose") {
        KodeeScene(stateCount = 1) {
            Header()
            Body {
                // TODO add some bullet points?
            }
        }

        KodeeScene {
            val displayType = LocalDisplayType.current
            Header()
            Body {
                // TODO could I hide some animation controls, to make them pausable and navigable?
                // When rendering the scene for preview, render the finished state and do not animate the sample.
                var sampleIndex by remember {
                    mutableIntStateOf(if (displayType == DisplayType.Story) 0 else SAMPLES.lastIndex)
                }
                val sampleTransition = updateTransition(sampleIndex)

                StoryEffect(Unit) {
                    while (true) {
                        delay(3.seconds)
                        if (sampleIndex == SAMPLES.lastIndex) {
                            delay(2.seconds)
                            sampleIndex = 0
                        } else {
                            sampleIndex += 1
                        }
                    }
                }

                ProvideTextStyle(MaterialTheme.typography.code1) {
                    MagicText(sampleTransition.createChildTransition { SAMPLES[it].get().toWords() })
                }
            }
        }
    }
}
