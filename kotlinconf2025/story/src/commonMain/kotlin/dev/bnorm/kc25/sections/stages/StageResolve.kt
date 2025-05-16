package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code2
import dev.bnorm.kc25.template.code3
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState

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
                    Row(
                        modifier = Modifier.padding(start = 36.dp, top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
    BODY_RESOLVE,
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
    val implt by tag("", ExamplePhase.IMPLICIT_TYPES_BODY_RESOLVE)
    val implb by tag("", ExamplePhase.IMPLICIT_TYPES_BODY_RESOLVE)
    val body by tag("", ExamplePhase.BODY_RESOLVE)

    val sample = extractTags(
        """
            class AsciiRandom : ${sup}Random${sup}() {
              override fun nextBits(bitCount: ${type}Int${type}): ${type}Int${type} {
                return ${body}Default${body}.${body}nextBits${body}(${body}bitCount${body})
              }
            
              fun nextString(charCount: ${type}Int${type})${implt}: String${implt} = ${implb}buildString${implb} {
                ${implb}repeat${implb}(${implb}charCount${implb}) {
                  ${implb}append${implb}(${implb}nextInt${implb}(from = 32, until = 127).${implb}toChar${implb}())
                }
              }
            }
        """.trimIndent()
    ).toCodeSample()


    val start = sample
        .styled(sup, type, implb, body, style = SpanStyle(Color.Red))
        .hide(implt)
        .styled(implt, style = SpanStyle(Color.DarkGray))

    start
        .then { unstyled(sup).attach(ExamplePhase.SUPER_TYPES) }
        .then { unstyled(type).attach(ExamplePhase.TYPES) }
        .then { unstyled(implb).reveal(implt).attach(ExamplePhase.IMPLICIT_TYPES_BODY_RESOLVE) }
        .then { unstyled(body).attach(ExamplePhase.BODY_RESOLVE) }
}

@Composable
private fun Example(
    transition: Transition<Int>,
    modifier: Modifier = Modifier,
) {
    val style = MaterialTheme.typography.code2
    Box(modifier.padding(start = 32.dp)) {
        ProvideTextStyle(style) {
            MagicText(transition.createChildTransition { SAMPLES[it.coerceIn(SAMPLES.indices)].string.splitByTags() })
        }
    }
}
