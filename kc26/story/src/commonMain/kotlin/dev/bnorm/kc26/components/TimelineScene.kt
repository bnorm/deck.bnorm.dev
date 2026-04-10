package dev.bnorm.kc26.components

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.toValue

enum class TimelineState {
    Empty,
    ThirdParty,
    Bundled,
    Improvements,
    Explanations,
    Future,
}

// TODO add an outline to everything against the gradient?
fun StoryboardBuilder.TimelineScene(
    start: TimelineState = TimelineState.Empty,
    end: TimelineState = TimelineState.Future,
) {
    scene(
        frames = TimelineState.entries.dropWhile { it != start }.dropLastWhile { it != end },
    ) {
        Surface(
            color = Color.Transparent,
            contentColor = Color.White,
            modifier = Modifier.fillMaxSize(),
        ) {
            val transition = transition
            transition.AnimatedVisibility(
                visible = { it is Frame.Value<*> },
                enter = slideInVertically(tween(750, easing = EaseOut)) { 2 * it / 3 },
                exit = slideOutVertically(tween(750, easing = EaseIn)) { 2 * it / 3 },
            ) {
                Timeline(start, end, transition)
            }
        }
    }
}

@Composable
fun Timeline(
    start: TimelineState = TimelineState.Empty,
    end: TimelineState = TimelineState.Future,
    transition: Transition<out Frame<TimelineState>>,
) {
    val dateStyle = MaterialTheme.typography.h6
    val descriptionStyle = MaterialTheme.typography.body2
    val spacing = 8.dp
    val circleSize = 16.dp
    val lineHeight = 4.dp

    @Composable
    fun <T> spec(delay: Int = 0): TweenSpec<T> = tween(500, delayMillis = delay, easing = EaseInOut)

    @Composable
    fun TimelinePoint(date: String, description: String, dotModifier: Modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // TODO would be cool to animate this more
            //  1. dot should slide all the way from the right
            //  2. date should appear after the dot
            //  3. line should extend from the dot after the date
            //  4. description should appear after the line
            Text(text = date, style = dateStyle)
            Spacer(Modifier.size(spacing))
            Box(contentAlignment = Alignment.Center) {
                Box(dotModifier.size(circleSize).clip(CircleShape).background(Color.Black))
                Box(dotModifier.size(circleSize - 1.dp).clip(CircleShape).background(Color.White))
            }
            Spacer(Modifier.size(spacing))
            Box(contentAlignment = Alignment.Center) {
                Box(Modifier.size(width = 3.dp, height = 32.dp).background(Color.Black))
                Box(Modifier.size(width = 2.dp, height = 31.dp).background(Color.White))
            }
            Spacer(Modifier.size(spacing))
            Text(text = description, style = descriptionStyle, textAlign = TextAlign.Center)
        }
    }

    val timelinePlacement = remember { mutableStateOf<LayoutCoordinates?>(null) }
    val placements = remember { mutableStateMapOf<TimelineState, LayoutCoordinates>() }

    Box(modifier = Modifier.fillMaxSize().onPlaced { timelinePlacement.value = it }) {
        Column(Modifier.fillMaxSize()) {
            // TODO center timeline within the scene?
            //  right now the line is slightly above the center line
            //  use AlignmentLine to align line and dots?
            Spacer(Modifier.weight(1f))
            Box(Modifier.weight(2f)) {
                // Draw the line for the timeline.
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("", style = dateStyle)
                    Spacer(Modifier.size(spacing))
                    Spacer(Modifier.size((circleSize - lineHeight) / 2f))
                    Box(contentAlignment = Alignment.Center) {
                        Box(Modifier.height(lineHeight).fillMaxWidth().background(Color.Black))
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().zIndex(1f)) {
                    Text("", style = dateStyle)
                    Spacer(Modifier.size(spacing))
                    Spacer(Modifier.size((circleSize - lineHeight) / 2f))
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.height(lineHeight)) {
                        Box(Modifier.height(lineHeight - 1.dp).fillMaxWidth().background(Color.White))
                    }
                }

                // Draw the points on the timeline.
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    transition.AnimatedVisibility(
                        visible = { it.toValue(start, end) >= TimelineState.ThirdParty },
                        enter = fadeIn(spec(delay = 500)) + expandHorizontally(spec()),
                        exit = fadeOut(spec()) + shrinkHorizontally(spec(delay = 500)),
                    ) {
                        TimelinePoint(
                            "Kotlin 1.3.60", "Power-Assert\nCompiler Plugin",
                            dotModifier = Modifier.onPlaced { placements[TimelineState.ThirdParty] = it },
                        )
                    }
                    transition.AnimatedVisibility(
                        visible = { it.toValue(start, end) >= TimelineState.Bundled },
                        enter = fadeIn(spec(delay = 500)) + expandHorizontally(spec()),
                        exit = fadeOut(spec()) + shrinkHorizontally(spec(delay = 500)),
                    ) {
                        TimelinePoint(
                            "Kotlin 2.0", "Power-Assert\nBundled",
                            dotModifier = Modifier.onPlaced { placements[TimelineState.Bundled] = it },
                        )
                    }
                    transition.AnimatedVisibility(
                        visible = { it.toValue(start, end) >= TimelineState.Improvements },
                        enter = fadeIn(spec(delay = 500)) + expandHorizontally(spec()),
                        exit = fadeOut(spec()) + shrinkHorizontally(spec(delay = 500)),
                    ) {
                        TimelinePoint(
                            "", "Minor\nImprovements",
                            dotModifier = Modifier.onPlaced { placements[TimelineState.Improvements] = it },
                        )
                    }
                    transition.AnimatedVisibility(
                        visible = { it.toValue(start, end) >= TimelineState.Explanations },
                        enter = fadeIn(spec(delay = 500)) + expandHorizontally(spec()),
                        exit = fadeOut(spec()) + shrinkHorizontally(spec(delay = 500)),
                    ) {
                        TimelinePoint(
                            "Kotlin 2.4", "Major\nImprovements",
                            dotModifier = Modifier.onPlaced { placements[TimelineState.Explanations] = it },
                        )
                    }
                    transition.AnimatedVisibility(
                        visible = { it.toValue(start, end) >= TimelineState.Future },
                        enter = fadeIn(spec(delay = 500)) + expandHorizontally(spec()),
                        exit = fadeOut(spec()) + shrinkHorizontally(spec(delay = 500)),
                    ) {
                        // TODO money bad emoji for joke?
                        // TODO eyes emoji for description?
                        // TODO change to 2.8 as joke?
                        TimelinePoint(
                            "Kotlin 2.?", "???",
                            dotModifier = Modifier.onPlaced { placements[TimelineState.Future] = it },
                        )
                    }
                }
            }
        }
    }
}