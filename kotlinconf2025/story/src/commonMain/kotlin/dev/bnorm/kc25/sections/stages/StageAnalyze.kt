package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.spans.SquigglyUnderlineSpanPainter
import dev.bnorm.deck.shared.spans.rememberSquigglyUnderlineAnimator
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.toState
import kotlin.time.Duration.Companion.seconds

fun StoryboardBuilder.StageAnalyze() {
    val items = listOf(
        "$BULLET_1 Once resolved, FIR is analyzed to determine correctness.",
        "$BULLET_1 This allows checking for problems which do not impact resolution.",
        "$BULLET_1 Most warnings are reported during this stage.",
    )
    StageDetail(stateCount = items.size + 1 + 6, stage = CompilerStage.Analyze) {
        val errorTransition = transition.createChildTransition { it.toState() - 1 - items.size }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            RevealEach(transition.createChildTransition { it.toState() - 1 }) {
                for (value in items) {
                    item { Text(value) }
                }
                item { ErrorSample(errorTransition) }
            }
        }
    }
}

private enum class TagType {
    Error,
    Warning,
    Comment,
}

private val SAMPLES = buildCodeSamples {
    val d1 by tag("diagnostic DIVISION_BY_ZERO", TagType.Warning)
    val d2 by tag("diagnostic RETURN_TYPE_MISMATCH", TagType.Error)
    val d3 by tag("diagnostic THROWABLE_TYPE_MISMATCH", TagType.Error)
    val d4 by tag("diagnostic UNNECESSARY_NOT_NULL_ASSERTION", TagType.Warning)
    val d5 by tag("diagnostic NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY", TagType.Error)
    val c1 by tag("comment DIVISION_BY_ZERO", TagType.Comment)
    val c2 by tag("comment RETURN_TYPE_MISMATCH", TagType.Comment)
    val c3 by tag("comment THROWABLE_TYPE_MISMATCH", TagType.Comment)
    val c4 by tag("comment UNNECESSARY_NOT_NULL_ASSERTION", TagType.Comment)
    val c5 by tag("comment NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY", TagType.Comment)

    val base = extractTags(
        """
            fun foo(): Int {
                try {
                    println(${d1}1 / 0${d1})$c1    // w: DIVISION_BY_ZERO$c1
                    return ${d2}""${d2}$c2         // e: RETURN_TYPE_MISMATCH$c2
                } catch (${d3}s: String${d3}) {$c3 // e: THROWABLE_TYPE_MISMATCH$c3
                    println(s${d4}!!${d4})$c4      // w: UNNECESSARY_NOT_NULL_ASSERTION$c4
                }
            ${d5}}${d5}$c5                         // e: NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY$c5
        """.trimIndent()
    )

    val samples = base.toCodeSample().hide(TagType.Comment)
        .then { reveal(c1) }
        .then { reveal(c2) }
        .then { reveal(c3) }
        .then { reveal(c4) }
        .then { reveal(c5) }

    val ranges = tags.filter { it.data == TagType.Error || it.data == TagType.Warning }.map { tag ->
        val ann = base.getStringAnnotations(tag.annotationStringTag, 0, base.length).single { it.item == tag.id }
        val range = ann.start..<ann.end
        val color = when (tag.data) {
            TagType.Error -> Color.Red
            TagType.Warning -> Color.Yellow
            else -> Color.Black
        }
        range to color
    }

    samples to ranges
}

private val samples = SAMPLES.first
private val ranges = SAMPLES.second

@Composable
private fun ErrorSample(transition: Transition<Int>) {
    val style = MaterialTheme.typography.code1
    val measurer = rememberTextMeasurer()
    val animator = rememberSquigglyUnderlineAnimator(2.seconds)
    val painter = remember(animator) {
        SquigglyUnderlineSpanPainter(
            width = 1.sp,
            wavelength = 6.sp,
            bottomOffset = 0.sp,
            animator = animator
        )
    }

    transition.createChildTransition { it.coerceIn(0..ranges.size) }.AnimatedContent(
        transitionSpec = {
            fadeIn(tween(300, easing = LinearEasing)) togetherWith
                    fadeOut(tween(300, delayMillis = 300, easing = LinearEasing))
        }
    ) { index ->
        val ranges = ranges.subList(fromIndex = 0, toIndex = index).toMap()
        val sample = samples[index.coerceAtMost(samples.lastIndex)].string
        val result = remember(sample) { measurer.measure(sample, style = style) }
        Text(
            text = sample,
            style = style,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 36.dp, top = 16.dp)
                .drawBehind {
                    with(painter.drawInstructionsFor(result, ranges)) { draw() }
                },
        )
    }
}
