package dev.bnorm.kc26.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.OutlinedText
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.toValue

private val points = listOf(
    SimpleTimelinePoint(
        title = "Kotlin 2.0",
        description = "Power-Assert\nBundled",
        start = TimelineState.Bundled,
    ),
    SimpleTimelinePoint(
        title = "",
        description = "Improvements\nand Problems",
        start = TimelineState.Improvements,
    ),
    SimpleTimelinePoint(
        title = "Kotlin 2.4",
        description = "Explanations\nand Annotations",
        start = TimelineState.Explanations,
    ),
    SimpleTimelinePoint(
        title = "Kotlin 2.?",
        description = "The Future",
        start = TimelineState.Future,
    ),
)

enum class TimelineState {
    Empty,
    Bundled,
    Improvements,
    Explanations,

    // TODO money bad emoji for joke?
    // TODO eyes emoji for description?
    // TODO change to 2.8 as joke?
    Future,
}

// TODO add an outline to everything against the gradient?
fun StoryboardBuilder.TimelineScene(
    start: TimelineState = TimelineState.Empty,
    end: TimelineState = TimelineState.Future,
) {
    val states = TimelineState.entries.dropWhile { it != start }.dropLastWhile { it != end }
    scene(
        frames = states,
    ) {
        Timeline(
            start = start,
            end = end,
            transition = transition,
            modifier = Modifier.sharedElement(rememberSharedContentState("timeline"))
        )
    }
}

private fun <T> spec(delay: Int = 0): TweenSpec<T> = tween(500, delayMillis = delay, easing = EaseInOut)

private val outline = 1.dp
private val spacing = 8.dp
private val circleSize = 16.dp
private val lineHeight = 4.dp
private val lineWidth = 4.dp
private val lineLength = 32.dp
private val descriptionSize = DpSize(192.dp, 80.dp)

private val descriptionStyle: TextStyle
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)

private val dateStyle: TextStyle
    @Composable @ReadOnlyComposable
    get() = MaterialTheme.typography.h5.copy(color = Color.White)

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun Timeline(
    current: TimelineState,
    modifier: Modifier = Modifier,
) {
    Timeline(
        start = current,
        end = current,
        transition = updateTransition(Frame.Start),
        modifier = modifier.sharedElement(
            rememberSharedContentState("timeline"),
            renderInOverlayDuringTransition = false,
        )
    )
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun Timeline(
    start: TimelineState,
    end: TimelineState,
    transition: Transition<out Frame<TimelineState>>,
    modifier: Modifier = Modifier,
) {
    val stateTransition = transition.createChildTransition { it.toValue(start, end) }
    val minimizedTransition = transition.createChildTransition { it !is Frame.Value<*> }

    Box(modifier.fillMaxSize()) {
        val offset by minimizedTransition.animateDp(
            transitionSpec = {
                if (!targetState) tween(500, easing = EaseInOut)
                else tween(500, delayMillis = 1000, easing = EaseInOut)
            },
            targetValueByState = { if (!it) 270.dp else 476.dp }
        )

        Column(Modifier.offset(y = offset - 101.dp)) {
            // Draw the points on the timeline.
            Box(contentAlignment = Alignment.TopCenter) {
                minimizedTransition.AnimatedVisibility(
                    visible = { !it },
                    enter = expandHorizontally(spec(delay = 500)),
                    exit = shrinkHorizontally(spec(delay = 500)),
                ) {
                    // Draw the line for the timeline.
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text("", style = dateStyle)
                        Spacer(Modifier.size(spacing))
                        Spacer(Modifier.size((circleSize - lineHeight) / 2f))
                        Box(contentAlignment = Alignment.Center) {
                            Box(Modifier.height(lineHeight).fillMaxWidth().background(Color.Black))
                        }
                    }
                }

                minimizedTransition.AnimatedVisibility(
                    visible = { !it },
                    enter = expandHorizontally(spec(delay = 500)),
                    exit = shrinkHorizontally(spec(delay = 500)),
                    modifier = Modifier.zIndex(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("", style = dateStyle)
                        Spacer(Modifier.size(spacing))
                        Spacer(Modifier.size((circleSize - lineHeight) / 2f))
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.height(lineHeight)) {
                            Box(Modifier.height(lineHeight - outline).fillMaxWidth().background(Color.White))
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    for (point in points) {
                        stateTransition.AnimatedVisibility(
                            visible = { it in point.visible },
                            enter = fadeIn(spec(delay = 500)) + expandHorizontally(spec()),
                            exit = fadeOut(spec()) + shrinkHorizontally(spec(delay = 500)),
                        ) {
                            val alpha by minimizedTransition.animateFloat(
                                transitionSpec = {
                                    if (!targetState) spec(delay = 1000)
                                    else spec()
                                },
                            ) {
                                if (it) 0f else 1f
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 16.dp).width(descriptionSize.width)
                                    .alpha(alpha)
                            ) {
                                // TODO would be cool to animate this more
                                //  1. dot should slide all the way from the right
                                //  2. date should appear after the dot
                                //  3. line should extend from the dot after the date
                                //  4. description should appear after the line
                                point.Title(stateTransition)
                                Spacer(Modifier.size(spacing))
                                Box(contentAlignment = Alignment.Center) {
                                    Box(Modifier.size(circleSize).clip(CircleShape).background(Color.Black))
                                    Box(Modifier.size(circleSize - outline).clip(CircleShape).background(Color.White))
                                }
                                Spacer(Modifier.size(spacing))
                                Box(contentAlignment = Alignment.Center) {
                                    Box(Modifier.size(width = lineWidth, height = lineLength).background(Color.Black))
                                    Box(
                                        Modifier.size(width = lineWidth - outline, height = lineLength - outline)
                                            .background(Color.White)
                                    )

                                }
                                Spacer(Modifier.size(spacing))
                            }
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (point in points) {
                    val highlighted = transition.createChildTransition {
                        it is Frame.Value<*> || it.toValue(start, end) in point.highlighted
                    }
                    stateTransition.AnimatedVisibility(
                        visible = { it in point.visible },
                        enter = fadeIn(spec(delay = 500)) + expandHorizontally(spec()),
                        exit = fadeOut(spec()) + shrinkHorizontally(spec(delay = 500)),
                    ) {
                        val background by highlighted.animateColor(
                            transitionSpec = {
                                if (targetState) tween(500, easing = EaseInOut)
                                else tween(500, delayMillis = 1000, easing = EaseInOut)
                            },
                            targetValueByState = {
                                if (it) Color.White else Color.White.copy(alpha = 0.5f)
                            }
                        )

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = background,
                            modifier = Modifier.padding(horizontal = 16.dp).size(descriptionSize)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                point.Description(
                                    stateTransition,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private abstract class TimelinePoint {
    abstract val visible: Set<TimelineState>
    abstract val highlighted: Set<TimelineState>

    @Composable
    abstract fun Title(state: Transition<TimelineState>)

    @Composable
    abstract fun Description(state: Transition<TimelineState>)
}

private class SimpleTimelinePoint(
    val title: String,
    val description: String,
    val start: TimelineState,
) : TimelinePoint() {
    override val visible = TimelineState.entries.dropWhile { it != start }.toSet()
    override val highlighted = setOf(start)

    @Composable
    override fun Title(state: Transition<TimelineState>) {
        OutlinedText(text = title, style = dateStyle)
    }

    @Composable
    override fun Description(state: Transition<TimelineState>) {
        GradientText(
            text = description,
            style = descriptionStyle,
            textAlign = TextAlign.Center,
        )
    }
}
