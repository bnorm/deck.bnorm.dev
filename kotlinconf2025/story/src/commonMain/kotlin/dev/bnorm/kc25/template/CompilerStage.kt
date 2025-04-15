package dev.bnorm.kc25.template

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.SharedKodee
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.section

// TODO actually fill in details about stages
fun StoryboardBuilder.CompilerArchitecture() {
    section("Architecture") {
        SectionTitle()
        CompilerStages()
    }
}

enum class CompilerStage {
    Parse,
    Resolve,
    Analyse,
    Transform,
    Generate,
    ;
}

private fun StoryboardBuilder.CompilerStages() {
    StageTimeline(null)
    StageDetail(CompilerStage.Parse)
    StageTimeline(CompilerStage.Parse)
    StageDetail(CompilerStage.Resolve)
    StageTimeline(CompilerStage.Resolve)
    StageDetail(CompilerStage.Analyse)
    StageTimeline(CompilerStage.Analyse)
    StageDetail(CompilerStage.Transform)
    StageTimeline(CompilerStage.Transform)
    StageDetail(CompilerStage.Generate)
    StageTimeline(CompilerStage.Generate)
}

private fun <T> fadeOutSpec(): TweenSpec<T> =
    tween(250, easing = EaseOut)

private fun <T> slideOutSpec(): TweenSpec<T> =
    tween(250, delayMillis = 250, easing = EaseOut)

private val BoxMovementSpec = BoundsTransform { initial, target ->
    if (initial.width < target.width) {
        keyframes {
            durationMillis = 500
            initial at 0 using EaseIn
            Rect(initial.left, target.top, initial.right, target.bottom) at 250 using EaseIn
            target at 500
        }
    } else {
        keyframes {
            durationMillis = 500
            initial at 0 using EaseIn
            Rect(target.left, initial.top, target.right, initial.bottom) at 250 using EaseIn
            target at 500
        }
    }
}

private fun <T> slideInSpec(): TweenSpec<T> =
    tween(250, delayMillis = 0, easing = EaseIn)

private fun <T> fadeInSpec(): TweenSpec<T> =
    tween(250, delayMillis = 250, easing = EaseIn)


private fun StoryboardBuilder.StageDetail(state: CompilerStage) {
    scene {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(32.dp)
                .sharedElement(
                    rememberSharedContentState("box:$state"),
                    zIndexInOverlay = -1f,
                    boundsTransform = BoxMovementSpec,
                )
                .fillMaxSize()
                .border(2.dp, MaterialTheme.colors.secondary, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            frame.AnimatedVisibility(
                visible = { it is Frame.State<*> },
                enter = fadeIn(fadeInSpec()),
                exit = fadeOut(fadeOutSpec()),
            ) {
                Text(state.name, style = MaterialTheme.typography.h3)
            }
        }

        SharedKodee {
            DefaultReactionKodee()
        }
    }
}

// TODO animate to details as part of start & end of scene?
//  - would allow transitioning the text as well
private fun StoryboardBuilder.StageTimeline(currentState: CompilerStage?) {
    scene {
        val currentOrdinal = currentState?.ordinal ?: -1

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .offset(y = (-16).dp)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                for (state in CompilerStage.entries) {
                    val boxVisible = frame.createChildTransition {
                        when (it) {
                            Frame.Start -> currentOrdinal == state.ordinal
                            is Frame.State<*> -> true // All visible.
                            Frame.End -> currentOrdinal + 1 == state.ordinal
                        }
                    }

                    val contentVisible = frame.createChildTransition {
                        when (it) {
                            Frame.Start -> state.ordinal < currentOrdinal
                            else -> state.ordinal <= currentOrdinal
                        }
                    }

                    val borderColor by frame.animateColor(
                        transitionSpec = { tween(500, easing = EaseOut) },
                    ) {
                        val focus = when (it) {
                            Frame.End -> currentOrdinal + 1 == state.ordinal
                            else -> currentOrdinal == state.ordinal
                        }
                        if (focus) MaterialTheme.colors.secondary else MaterialTheme.colors.primary
                    }

                    CompilerStageBox(
                        state,
                        borderColor,
                        boxVisible = boxVisible,
                        contentVisible = contentVisible,
                    )
                }
            }
        }

        SharedKodee {
            // TODO need to slide him in when transition from the title
            DefaultReactionKodee()
        }
    }
}

@Composable
context(sharedVisibilityScope: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun CompilerStageBox(
    state: CompilerStage,
    borderColor: Color,
    boxVisible: Transition<Boolean>,
    contentVisible: Transition<Boolean>,
) {

    Box(Modifier.size(156.dp, 69.dp)) {
        boxVisible.AnimatedVisibility(
            visible = { it },
            enter = slideInVertically(slideInSpec()) { -it },
            exit = slideOutVertically(slideOutSpec()) { -it },
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState("box:$state"),
                        animatedVisibilityScope = sharedVisibilityScope,
                        zIndexInOverlay = -1f,
                        boundsTransform = BoxMovementSpec,
                    )
                    .fillMaxSize()
                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            ) {
                contentVisible.AnimatedVisibility(
                    visible = { it },
                    enter = fadeIn(fadeInSpec()),
                    exit = fadeOut(fadeOutSpec()),
                ) {
                    Text(
                        state.name,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                    )
                }
            }
        }
    }
}
