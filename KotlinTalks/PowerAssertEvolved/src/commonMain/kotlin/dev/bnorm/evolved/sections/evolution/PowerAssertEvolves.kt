package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.toInt

fun StoryboardBuilder.PowerAssertEvolves() {
    scene(stateCount = 7) {
        val transition = frame.createChildTransition { it.toInt() }
        HeaderAndBody {
            Box {
                Column {
                    PowerAssertEvolve(
                        transition = transition,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    )

                    LowerArea(
                        transition = transition,
                    )
                }

                if (currentState >= 6) {
                    ProvideTextStyle(
                        MaterialTheme.typography.h1.copy(
                            color = Color.Red,
                            fontWeight = FontWeight.Black
                        )
                    ) {
                        Text(
                            "Oops!",
                            modifier = Modifier.align(Alignment.Center)
                                .offset(x = (-32).dp, y = (-64).dp)
                                .rotate(-25f)
                                .border(16.dp, Color.Red, RoundedCornerShape(32.dp))
                                .padding(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LowerArea(transition: Transition<Int>) {
    Box(
        Modifier
            .padding(bottom = 32.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Column(
            Modifier
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ProvideTextStyle(MaterialTheme.typography.h5.copy(fontFamily = FontFamily.Monospace)) {
                val first1 by transition.animateTyping("What?", onValue = 1)
                val first2 by transition.animateTyping("POWER-ASSERT is evolving!", onValue = 2)

                val second2 = " Your POWER-ASSERT"
                val second3 = "evolved into EXPLAIN CALL!"
                val second1 by transition.animateTyping("Congratulations!", onValue = 4)
                val second by transition.animateTyping(second2 + second3, onValue = 5)

                if (second1.isEmpty()) {
                    Text(first1)
                    Spacer(Modifier.height(8.dp))
                    Text(first2)
                } else {
                    Text(second1 + second.substring(0, second.length.coerceIn(0, second2.length)))
                    Spacer(Modifier.height(8.dp))
                    Text(second3.substring(0, (second.length - second2.length).coerceIn(0, second3.length)))
                }
            }
        }
    }
}

@Composable
private fun PowerAssertEvolve(
    transition: Transition<Int>,
    modifier: Modifier = Modifier,
) {
    val scale by transition.animateFloat(
        transitionSpec = {
            if (initialState == 2 && targetState == 3) {
                evolveSpec
            } else {
                snap()
            }
        },
    ) { it -> if (it.toInt() >= 3) 1.0f else 0.0f }

    ProvideTextStyle(MaterialTheme.typography.h1.copy(fontFamily = FontFamily.Monospace)) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Text(
                "POWER-ASSERT",
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.scale(1.0f - scale)
            )
            Text(
                "EXPLAIN CALL",
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.scale(scale)
            )
        }
    }
}

@Composable
private fun <T : Comparable<T>> Transition<T>.animateTyping(
    text: String,
    onValue: T,
): State<String> {
    val length by animateInt(
        transitionSpec = {
            if (targetState == onValue && targetState > initialState) {
                tween(text.length * 50, easing = LinearEasing)
            } else {
                snap()
            }
        },
    ) {
        if (it >= onValue) text.length else 0
    }

    return derivedStateOf { text.substring(0, length) }
}

@OptIn(ExperimentalAnimationSpecApi::class)
private val evolveSpec: KeyframesWithSplineSpec<Float> = keyframesWithSpline {
    durationMillis = 3000

    val intervals = buildList {
        repeat(2) { add(30f) }
        repeat(2) { add(25f) }
        repeat(2) { add(15f) }
        repeat(11) { add(10f) }
    }
    val sum = intervals.sum()
    require(intervals.size % 2 == 1)

    0f at 0

    var fraction = 0f
    var flag = false
    for (f in intervals) {
        fraction += f
        flag = !flag
        (if (flag) 1f else 0f) atFraction (fraction / sum)
    }
    require(flag)
}
