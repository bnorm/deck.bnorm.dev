package dev.bnorm.kc25.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun StageScaffold(
    currentStage: Transition<CompilerStage?>,
    modifier: Modifier = Modifier,
    kodee: @Composable () -> Unit = { DefaultReactionKodee() },
    body: @Composable BoxScope.(PaddingValues) -> Unit,
) {
    KodeeScaffold(
        header = {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .sharedElement(rememberSharedContentState(key = "StageScaffold.Header"))
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .offset(y = (-16).dp)
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    for (stage in CompilerStage.entries) {
                        // TODO how do we animate color across scenes?
                        //  - will it require a transition scene?
                        val borderColor by currentStage.animateColor(
                            transitionSpec = { tween(500, easing = EaseOut) },
                        ) {
                            when (it) {
                                stage -> MaterialTheme.colors.secondary
                                else -> MaterialTheme.colors.primary
                            }
                        }

                        CompilerStateBox(stage, borderColor)
                    }
                }
            }
        },
        kodee = kodee,
        body = body,
        modifier = modifier,
    )
}

@Composable
private fun CompilerStateBox(
    state: CompilerStage,
    borderColor: Color,
) {
    Box(Modifier.size(156.dp, 69.dp)) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, borderColor, RoundedCornerShape(16.dp))
        ) {
            Text(
                state.name,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )
        }
    }
}
