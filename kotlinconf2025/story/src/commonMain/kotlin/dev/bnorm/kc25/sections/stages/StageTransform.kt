package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.magic.toWords
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.StageTransform() {
    val items = listOf(
        AnnotatedString("$BULLET_1 FIR is converted to IR."),
        buildAnnotatedString {
            append("$BULLET_1 IR is a tree-based representation of the ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                append("semantics")
            }
            append(" of Kotlin code.")
        },
        AnnotatedString("$BULLET_1 It is used to convert high-level concepts into low-level operations."),
        AnnotatedString("$BULLET_1 Performed in phases that each reduce or optimize."),
    )

    StageDetail(stateCount = items.size + 1 + SAMPLES.size, stage = CompilerStage.Transform) {
        val sampleTransition = transition.createChildTransition { it.toState() - 1 - items.size }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            RevealEach(transition.createChildTransition { it.toState() - 1 }) {
                for (value in items) {
                    item { Text(value) }
                }
                item {
                    Sample(sampleTransition, modifier = Modifier.padding(start = 36.dp))
                }
            }
        }
    }
}

private val SAMPLES = buildCodeSamples {
    val samples = listOf(
        """
            for ((i, str) in list.withIndex()) {
              if (i in 0..10) {
                println(str)
              }
            }
        """.trimIndent(),
        """
            var tmp = 0
            for (str in list) {
              val i = tmp++
              if (i in 0..10) {
                println(str)
              }
            }
        """.trimIndent(),
        """
            var tmp = 0
            val iter = list.iterator()
            while (iter.hasNext()) {
              val str = iter.next()
              val i = tmp++
              if (i in 0..10) {
                println(str)
              }
            }
        """.trimIndent(),
        """
            var tmp = 0
            val iter = list.iterator()
            while (iter.hasNext()) {
              val str = iter.next()
              val i = tmp++
              if (i >= 0 && i <= 10) {
                println(str)
              }
            }
        """.trimIndent(),
    )

    samples.map { it.toCodeSample(scope = CodeScope.Function) }
}

@Composable
fun Sample(
    transition: Transition<Int>,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        ProvideTextStyle(MaterialTheme.typography.code1) {
            MagicText(transition.createChildTransition { SAMPLES[it.coerceIn(SAMPLES.indices)].string.toWords() })
        }
    }
}
