package dev.bnorm.kc25.sections.existing

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.HeaderAndBody
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.map
import dev.bnorm.storyboard.core.toState
import dev.bnorm.storyboard.easel.section
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.magic.toWords
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
            HeaderAndBody {
                // TODO could I hide some animation controls, to make them pausable and navigable?
                // When transitioning into the scene, make sure it starts from the beginning.
                // But when rendering the scene state directly, render in the finished state.
                // Also, when rendering directly, do not animate the sample.
                val frozen = remember { frame.currentState.map { true }.toState(start = false, end = false) }
                var sampleIndex by remember { mutableIntStateOf(if (frozen) SAMPLES.lastIndex else 0) }
                val sampleTransition = updateTransition(sampleIndex)

                LaunchedEffect(Unit) {
                    while (!frozen) {
                        delay(3.seconds)
                        if (sampleIndex == SAMPLES.lastIndex) {
                            delay(2.seconds)
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
