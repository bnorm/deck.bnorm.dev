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
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit

enum class CompilerStage {
    Parse,
    Resolve,
    Analyze,
    Transform,
    Generate,
    ;
}

data class CompilerStageState(
    val visible: Boolean = true,
    val content: Boolean = false,
    val focused: Boolean = false,
)

private fun <T> fadeOutSpec(): TweenSpec<T> =
    tween(250, easing = EaseOut)

val BoxMovementSpec: BoundsTransform = BoundsTransform { _, _ -> tween(500, easing = EaseInOut) }
val TextMovementSpec: BoundsTransform = BoxMovementSpec

private fun <T> fadeInSpec(): TweenSpec<T> =
    tween(250, delayMillis = 0, easing = EaseIn)

fun StoryboardBuilder.StageDetail(
    stateCount: Int,
    stage: CompilerStage,
    content: SceneContent<Int>,
) {
    scene(stateCount) {
        Box(
            modifier = Modifier
                .padding(32.dp)
                .sharedElement(
                    rememberSharedContentState("box:$stage"),
                    boundsTransform = BoxMovementSpec,
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
                        )
                )

                transition.AnimatedVisibility(
                    visible = { it is Frame.State<*> },
                    enter = fadeIn(fadeInSpec()),
                    exit = fadeOut(fadeOutSpec()),
                ) {
                    Box(Modifier.padding(16.dp)) {
                        Render(content)
                    }
                }
            }
        }
    }
}

fun StoryboardBuilder.StageTimeline(
    states: List<Map<CompilerStage, CompilerStageState>>,
    start: Map<CompilerStage, CompilerStageState>? = null,
    end: Map<CompilerStage, CompilerStageState>? = null,
    slideStart: Boolean = false,
    slideEnd: Boolean = false,
) {
    scene(
        states = states,
        enterTransition = enter(
            start = if (slideStart) SceneEnter(alignment = Alignment.CenterEnd) else DefaultEnterTransition,
            end = if (slideEnd) SceneEnter(alignment = Alignment.CenterEnd) else DefaultEnterTransition,
        ),
        exitTransition = exit(
            start = if (slideStart) SceneExit(alignment = Alignment.CenterEnd) else DefaultExitTransition,
            end = if (slideEnd) SceneExit(alignment = Alignment.CenterEnd) else DefaultExitTransition,
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            for (stage in CompilerStage.entries) {
                val boxVisible = transition.createChildTransition {
                    it.toState(start, end)?.get(stage)?.visible ?: false
                }

                val contentVisible = transition.createChildTransition {
                    it.toState(start, end)?.get(stage)?.content ?: false
                }

                val borderColor by transition.animateColor(
                    transitionSpec = { tween(500, easing = EaseOut) },
                ) {
                    val focus = it.toState(start, end)?.get(stage)?.focused ?: false
                    if (focus) MaterialTheme.colors.secondary else MaterialTheme.colors.primary
                }

                CompilerStageBox(
                    stage,
                    borderColor,
                    boxVisible = boxVisible,
                    contentVisible = contentVisible,
                )
            }
        }
    }
}

@Composable
context(sharedVisibilityScope: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun CompilerStageBox(
    stage: CompilerStage,
    borderColor: Color,
    boxVisible: Transition<Boolean>,
    contentVisible: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {
    Box(modifier.width(156.dp)) {
        boxVisible.AnimatedVisibility(
            visible = { it },
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
                    )
                    .fillMaxWidth()
                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                contentVisible.AnimatedVisibility(
                    visible = { it },
                    enter = fadeIn(fadeInSpec()),
                    exit = fadeOut(fadeOutSpec()),
                ) {
                    Text(
                        stage.name,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .sharedBounds(
                                rememberSharedContentState("text:$stage"),
                                animatedVisibilityScope = sharedVisibilityScope,
                                boundsTransform = TextMovementSpec,
                            )
                    )
                }
            }
        }
    }
}
