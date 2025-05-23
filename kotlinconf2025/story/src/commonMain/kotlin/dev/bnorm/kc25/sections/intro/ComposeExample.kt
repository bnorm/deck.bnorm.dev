package dev.bnorm.kc25.sections.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.kc25.template.HeaderScaffold
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.section
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import kotlin.time.Duration.Companion.seconds

private val SAMPLES = buildCodeSamples {
    val s by tag("splitter") // TODO support a "point" tag?

    val c1 by tag("composer")
    val c2 by tag("composer calls")
    val c3 by tag("changed")
    val c4 by tag("check skip")
    val c5 by tag("update scope")

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
    """.trimIndent().toCodeSample()

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
    """.trimIndent().toCodeSample()

    basic.hide(c1, c2, c3)
        .then { reveal(c1).focus(c1) }
        .then { reveal(c2).focus(c2) }
        .then { reveal(c3).focus(c3) }
        .then { restartable.hide(c5).focus(c4) }
        .then { reveal(c5).focus(c5) }
        .then { unfocus().attach(5.seconds) }
}

fun StoryboardBuilder.ComposeExample(sink: MutableList<CodeSample>) {
    sink.addAll(SAMPLES)
    section("Compose") {
        scene(
            enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
            exitTransition = SceneExit(alignment = Alignment.CenterEnd),
        ) {
            HeaderScaffold { padding ->
                val index by animateSampleIndex(samples = SAMPLES)

                Box(Modifier.padding(padding)) {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicText(SAMPLES[index].string.splitByTags())
                    }
                }
            }
        }
    }
}
