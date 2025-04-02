package dev.bnorm.kc25.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.Header
import dev.bnorm.kc25.template.KodeeScene
import dev.bnorm.kc25.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.CompilerArchitecture() {
    SectionAndTitle("Compiler Architecture") {
        ArchitectureOverview()
    }
}

private fun StoryboardBuilder.ArchitectureOverview() {
    val titles = listOf(
        "Parsing",
        "Resolution",
        "Conversion",
        "Transformation",
        "Generation",
    )

    class State(val visible: Int, val scale: Float)
    KodeeScene(
        buildList<State> {
            add(State(-1, 0f))
            for (i in titles.indices) {
                add(State(i, 0f))
                add(State(i, 1f))
                add(State(i, 0f))
            }
        }
    ) {
        Header()

        val offset by frame.animateDp(
            transitionSpec = { tween(500, 500, easing = EaseInOut) },
            targetValueByState = { (-960 * it.toState().visible).coerceAtMost(0).dp },
        )
        val scale by frame.animateFloat(
            transitionSpec = { tween(500, easing = EaseInOut) },
            targetValueByState = { 0.4f + 0.6f * it.toState().scale },
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
                .scale(scale)
                .wrapContentWidth(align = Alignment.Start, unbounded = true)
        ) {
            Row(Modifier.offset(x = offset)) {
                for ((index, title) in titles.withIndex()) {
                    Spacer(Modifier.width(32.dp))
                    frame.AnimatedVisibility(
                        visible = { it.toState().visible >= index },
                        enter = fadeIn(tween(500, easing = EaseInOut)),
                        exit = fadeOut(tween(500, easing = EaseInOut)),
                    ) {
                        Surface(
                            modifier = Modifier.size(896.dp, 400.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, MaterialTheme.colors.primary),
                            color = MaterialTheme.colors.surface.copy(alpha = 0.75f),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                ProvideTextStyle(MaterialTheme.typography.h4) {
                                    Text(title)
                                }
                                Spacer(Modifier.height(8.dp))
                                Spacer(
                                    Modifier.height(2.dp).fillMaxWidth().background(MaterialTheme.colors.primary)
                                )

                                // TODO content for each phase
                            }
                        }
                    }
                    Spacer(Modifier.width(32.dp))
                }
            }
        }
    }
}
