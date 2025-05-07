package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import dev.bnorm.kc25.template.code2
import dev.bnorm.kc25.template.code3
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState
import kotlin.time.Duration.Companion.seconds

fun StoryboardBuilder.StageResolve() {
    val items = listOf(
        "$BULLET_1 FIR is resolved in a sequence of phases.",
        "$BULLET_1 Each phase resolves a different part of the FIR structure.",
        "$BULLET_1 Order is extremely important, as phases build on each other.",
    )

    StageDetail(stateCount = items.size + 2 + ExamplePhase.entries.size, stage = CompilerStage.Resolve) {
        val phases = transition.createChildTransition { it.toState() - items.size - 1 }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            RevealEach(transition.createChildTransition { it.toState() - 1 }) {
                for (value in items) {
                    item { Text(value) }
                }
                item {
                    Row(modifier = Modifier.padding(start = 36.dp, top = 12.dp)) {
                        Phases(phases)
                        Example(phases)
                    }
                }
            }
        }
    }
}

private enum class ExamplePhase {
    SUPER_TYPES,
    TYPES,
    IMPLICIT_TYPES_BODY_RESOLVE,
    // TODO more phases?
}

@Composable
private fun Phases(
    transition: Transition<Int>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .width(IntrinsicSize.Max)
            .fillMaxHeight()
    ) {
        ProvideTextStyle(MaterialTheme.typography.code3) {
            for ((index, phase) in ExamplePhase.entries.withIndex()) {
                val border by transition.animateColor(transitionSpec = { tween(500) }) {
                    if (it == index + 1) MaterialTheme.colors.secondary
                    else MaterialTheme.colors.primary
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .border(2.dp, border, RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text(phase.name)
                }
            }
        }
    }
}

private val SAMPLES = buildCodeSamples {
    val sup by tag("", ExamplePhase.SUPER_TYPES)
    val type by tag("", ExamplePhase.TYPES)
    val impl by tag("", ExamplePhase.IMPLICIT_TYPES_BODY_RESOLVE)

    val base = extractTags(
        """
            class AsciiRandom : ${sup}Random${sup}() {
              override fun nextBits(bitCount: ${type}Int${type}): ${type}Int${type} {
                return Default.nextBits(bitCount)
              }
            
              fun nextString(charCount: ${type}Int${type})${impl}: String${impl} = buildString {
                repeat(charCount) {
                  append(nextInt(from = 32, until = 127).toChar())
                }
              }
            }
        """.trimIndent()
    )

    val samples = base.toCodeSample().hide(impl) // all red
        .then { attach(ExamplePhase.SUPER_TYPES) } // sup green
        .then { attach(ExamplePhase.TYPES) } // types green
        .then { reveal(impl).attach(ExamplePhase.IMPLICIT_TYPES_BODY_RESOLVE) } // impl green

    samples to tags
}

private val samples = SAMPLES.first
private val tags = SAMPLES.second

@Composable
private fun Example(
    transition: Transition<Int> = updateTransition(-1),
    modifier: Modifier = Modifier,
) {
    val style = MaterialTheme.typography.code2
    val measurer = rememberTextMeasurer()
    val animator = rememberSquigglyUnderlineAnimator(2.seconds)
    // TODO are squiggly lines the best way to indicate unresolved code?
    //  - would this be better visually if the code itself was red?
    val painter = remember(animator) {
        SquigglyUnderlineSpanPainter(
            width = 1.sp,
            wavelength = 6.sp,
            bottomOffset = 0.sp,
            animator = animator
        )
    }

    Box(modifier.padding(start = 32.dp, top = 16.dp)) {
        transition.createChildTransition { it.coerceIn(samples.indices) }
            .AnimatedContent(
                transitionSpec = {
                    fadeIn(tween(300, easing = LinearEasing)) togetherWith
                            fadeOut(tween(300, delayMillis = 300, easing = LinearEasing))
                }
            ) { index ->
                val sample = samples[index].string
                val ranges = ExamplePhase.entries.subList(fromIndex = index, toIndex = ExamplePhase.entries.size)
                    .flatMap { phase ->
                        val tag = tags.single { it.data == phase }
                        sample.getStringAnnotations(tag.annotationStringTag, 0, sample.length)
                            .filter { phase != ExamplePhase.IMPLICIT_TYPES_BODY_RESOLVE && it.item == tag.id }
                            .map { it.start..<it.end }
                    }
                    .associateWith { Color.Red }

                val result = remember(sample) { measurer.measure(sample, style = style) }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            with(painter.drawInstructionsFor(result, ranges)) { draw() }
                        },
                )
            }

        ProvideTextStyle(style) {
            MagicText(transition.createChildTransition { samples[it.coerceIn(samples.indices)].string.splitByTags() })
        }
    }
}
