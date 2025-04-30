package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement

fun StoryboardBuilder.StageTimelineTransition() {
    scene(stateCount = 0) {
        SharedTransitionLayout {
            transition.AnimatedContent(
                transitionSpec = { EnterTransition.None togetherWith ExitTransition.None }
            ) {
                when (it) {
                    // Impossible since 'stateCount' is 0.
                    is Frame.State<*> -> {}

                    Frame.Start -> {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 32.dp)
                        ) {
                            for (state in CompilerStage.entries) {
                                CompilerStageBoxCenter(state)
                            }
                        }
                    }

                    Frame.End -> {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .offset(y = (-16).dp)
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            for (stage in CompilerStage.entries) {
                                CompilerStageBoxTop(stage)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun CompilerStageBoxCenter(
    stage: CompilerStage,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .sharedElement(
                rememberSharedContentState("box:$stage"),
                boundsTransform = BoxMovementSpec,
            )
            .width(156.dp)
            .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            stage.name,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .sharedElement(
                    rememberSharedContentState("text:$stage"),
                    boundsTransform = TextMovementSpec,
                )
        )
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun CompilerStageBoxTop(
    stage: CompilerStage,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .sharedElement(
                rememberSharedContentState("box:$stage"),
                boundsTransform = BoxMovementSpec,
            )
            .width(156.dp)
            .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            stage.name,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(top = 8.dp)
                .sharedElement(
                    rememberSharedContentState("text:$stage"),
                    boundsTransform = TextMovementSpec,
                )
        )
    }
}
