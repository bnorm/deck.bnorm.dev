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
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

private val SAMPLES = buildCodeSamples {
    val s by tag("splitter") // TODO support a "point" tag?

    val c1 by tag("composer")
    val c2 by tag("composer calls")
    val c3 by tag("changed")
    val c4 by tag("check skip")
    val c5 by tag("update scope")

    // TODO named parameters aren't highlighted correctly...
    val basic = """
        @Composable fun MyCounter(${c1}composer: Composer${c1}${c3}, changed: Int${c3}) {${c2}
          composer.startRestartGroup(123)${c2}
          var count by remember(${c1}composer${c1}${c3}, 0${c3}) { mutableStateOf(0) }
          MyButton(
            text = "Count: ${'$'}count",
            onPress = { count += 1 },${c1}
            composer = composer,${c1}${c3}
            changed = 0,${c3}
          )${c2}
          composer.endRestartGroup()${c2}
        }
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    val restartable = """
        @Composable fun MyCounter($s${s}composer: Composer$s${s}, changed: Int$s${s}) {
          composer.startRestartGroup(123)
          ${c4}if (changed == 0 && composer.getSkipping()) {
            composer.skipToGroupEnd()
          }${c4} else {
            var count by remember($s${s}composer$s${s}, 0$s${s}) { mutableStateOf(0) }
            MyButton(
              text = "Count: ${'$'}count",
              onPress = { count += 1 },
              composer = composer,
              changed = 0,
            )
          }
          composer.endRestartGroup()${c5}?.let {
            it.updateScope { composer, _ -> MyCounter(composer, changed or 1) }
          }${c5}
        }
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    basic.hide(c1, c2, c3)
        .then { reveal(c1).focus(c1) }
        .then { reveal(c2).focus(c2) }
        .then { reveal(c3).focus(c3) }
        .then { restartable.hide(c5).focus(c4) }
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
            KodeeScaffold { padding ->
                // TODO could I hide some animation controls, to make them pausable and navigable?
                // When rendering the scene for preview, render the finished state and do not animate the sample.
                val sceneMode = LocalSceneMode.current
                var sampleIndex by remember {
                    mutableIntStateOf(if (sceneMode == SceneMode.Story) 0 else SAMPLES.lastIndex)
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

                Box(Modifier.padding(padding)) {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicText(sampleTransition.createChildTransition {
                            SAMPLES[it].string.splitByTags()
                        })
                    }
                }
            }
        }
    }
}
