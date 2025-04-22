package dev.bnorm.kc25.sections.stages

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.SharedKodee
import dev.bnorm.kc25.template.DefaultReactionKodee
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

enum class CompilerStage {
    Parse,
    Resolve,
    Analyze,
    Transform,
    Generate,
    ;
}

private fun <T> fadeOutSpec(): TweenSpec<T> =
    tween(250, easing = EaseOut)

//private fun <T> slideOutSpec(): TweenSpec<T> =
//    tween(250, delayMillis = 250, easing = EaseOut)

val BoxMovementSpec: BoundsTransform = BoundsTransform { _, _ -> tween(500, easing = EaseInOut) }
val TextMovementSpec: BoundsTransform = BoxMovementSpec

//private val BoxMovementSpec = BoundsTransform { initial, target ->
//    if (initial.width < target.width) {
//        keyframes {
//            durationMillis = 500
//            initial at 0 using EaseIn
//            Rect(initial.left, target.top, initial.right, target.bottom) at 250 using EaseIn
//            target at 500
//        }
//    } else {
//        keyframes {
//            durationMillis = 500
//            initial at 0 using EaseIn
//            Rect(target.left, initial.top, target.right, initial.bottom) at 250 using EaseIn
//            target at 500
//        }
//    }
//}

//private fun <T> slideInSpec(): TweenSpec<T> =
//    tween(250, delayMillis = 0, easing = EaseIn)

private fun <T> fadeInSpec(): TweenSpec<T> =
    tween(250, delayMillis = 0, easing = EaseIn)

fun StoryboardBuilder.StageDetail(
    stateCount: Int,
    stage: CompilerStage,
    content: SceneContent<Int>,
) {
    scene(stateCount) {
        // TODO display the other stages off the screen
        //  - they are pushed out rather than fade?

        Box(
            modifier = Modifier
                .padding(32.dp)
                .sharedElement(
                    rememberSharedContentState("box:$stage"),
                    boundsTransform = BoxMovementSpec,
                    zIndexInOverlay = -1f,
                )
                .fillMaxSize()
                .border(2.dp, MaterialTheme.colors.secondary, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    stage.name,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState("text:$stage"),
                            boundsTransform = TextMovementSpec,
                            zIndexInOverlay = -1f,
                        )
                )

                frame.AnimatedVisibility(
                    visible = { it is Frame.State<*> },
                    enter = fadeIn(fadeInSpec()),
                    exit = fadeOut(fadeOutSpec()),
                ) {
                    // TODO horizontal line like with the header scaffold?
                    Box(Modifier.padding(16.dp)) {
                        Render(content)
                    }
                }
            }
        }

        SharedKodee {
            DefaultReactionKodee()
        }
    }
}

fun StoryboardBuilder.StageTimeline(startState: CompilerStage?, endState: CompilerStage?) {
    val startOrdinal = startState?.ordinal ?: -1
    val endOrdinal = endState?.ordinal ?: CompilerStage.entries.size

    scene(
        states = listOf(startOrdinal, endOrdinal),
        enterTransition = if (startState == null || endState == null) SceneEnter(alignment = Alignment.CenterEnd) else DefaultEnterTransition,
        exitTransition = if (startState == null || endState == null) SceneExit(alignment = Alignment.CenterEnd) else DefaultExitTransition,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                for (state in CompilerStage.entries) {
                    val detailStage = frame.createChildTransition {
                        when (it) {
                            Frame.Start -> startState?.ordinal
                            is Frame.State<*> -> null // All visible.
                            Frame.End -> endState?.ordinal
                        }
                    }

                    val contentVisible = frame.createChildTransition { state.ordinal <= it.toState() }

                    val borderColor by frame.animateColor(
                        transitionSpec = { tween(500, easing = EaseOut) },
                    ) {
                        val focus = state.ordinal == it.toState()
                        if (focus) MaterialTheme.colors.secondary else MaterialTheme.colors.primary
                    }

                    CompilerStageBox(
                        state,
                        borderColor,
                        detailStage = detailStage,
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
    stage: CompilerStage,
    borderColor: Color,
    detailStage: Transition<Int?>,
    contentVisible: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {
    Box(modifier.width(156.dp)) {
        detailStage.AnimatedVisibility(
            visible = { it == null || it == stage.ordinal },
            enter = fadeIn(fadeInSpec()),
            exit = fadeOut(fadeOutSpec()),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState("box:$stage"),
                        animatedVisibilityScope = sharedVisibilityScope,
                        boundsTransform = BoxMovementSpec,
                        zIndexInOverlay = -1f,
                    )
                    .fillMaxWidth()
                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    "", style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                contentVisible.AnimatedVisibility(
                    visible = { it },
                    enter = fadeIn(fadeInSpec()),
                    exit = fadeOut(fadeOutSpec()),
                ) {
                    Text(
                        stage.name,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .sharedBounds(
                                rememberSharedContentState("text:$stage"),
                                animatedVisibilityScope = sharedVisibilityScope,
                                boundsTransform = TextMovementSpec,
                                zIndexInOverlay = -1f,
                            )
                    )
                }
            }
        }
    }
}
