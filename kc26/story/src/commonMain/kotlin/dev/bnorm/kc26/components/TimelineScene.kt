package dev.bnorm.kc26.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.bnorm.kc26.template.gradientBackground
import dev.bnorm.kc26.template.gradientOverlay
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.toValue
import kotlin.math.sqrt

enum class TimelineState {
    Empty,
    ThirdParty,
    Bundled,
    Improvements,
    Explanations,
    Future,
}

fun StoryboardBuilder.TimelineScene(
    start: TimelineState = TimelineState.Empty,
    end: TimelineState = TimelineState.Future,
) {
    scene(
        frames = TimelineState.entries.dropWhile { it != start }.dropLastWhile { it != end },
        enterTransition = { fadeIn(tween(500, easing = EaseInOut)) },
        exitTransition = { fadeOut(tween(500, easing = EaseInOut)) },
    ) {
        Timeline(start, end, transition)
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
            Box(dotModifier.size(circleSize).clip(CircleShape).gradientBackground())
            Spacer(Modifier.size(spacing))
            Box(Modifier.size(width = 2.dp, height = 32.dp).gradientBackground())
            Spacer(Modifier.size(spacing))
            Text(text = description, style = descriptionStyle, textAlign = TextAlign.Center)
        }
    }

    // TODO center timeline within the scene
    //  right now the line is slightly above the center line
    val timelinePlacement = remember { mutableStateOf<LayoutCoordinates?>(null) }
    val placements = remember { mutableStateMapOf<TimelineState, LayoutCoordinates>() }

    // TODO use AlignmentLine to align line and dots
    Box(modifier = Modifier.fillMaxSize().onPlaced { timelinePlacement.value = it }) {
        Box(Modifier.align(Alignment.Center)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("", style = dateStyle)
                Spacer(Modifier.size(spacing))
                Spacer(Modifier.size((circleSize - lineHeight) / 2f))

                val percent by transition.animateFloat(
                    transitionSpec = { spec() },
                ) { if (it == Frame.Start && start == TimelineState.Empty) 0f else 1f }
                Box(Modifier.height(lineHeight).fillMaxWidth(percent).gradientBackground())
            }
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

        run {
            val fraction = transition.animateFloat(
                transitionSpec = { spec() },
                targetValueByState = { if (it == Frame.End) 1f else 0f },
            )
            Canvas(Modifier.fillMaxSize().gradientOverlay()) {
                val boundingBox = placements[end]?.let {
                    if (transition.currentState.toValue(start, end) < end) return@let null
                    timelinePlacement.value?.localBoundingBoxOf(it)
                }

                if (boundingBox != null) {
                    val center = boundingBox.center
                    val width = if (boundingBox.center.x > size.width / 2f) center.x else size.width - center.x
                    val height = if (boundingBox.center.y > size.height / 2f) center.y else size.height - center.y
                    val size = sqrt(width * width + height * height)
                    drawCircle(Color.White, radius = fraction.value * size, center = center)
                }
            }
        }

        run {
            val fraction = transition.animateFloat(
                transitionSpec = { spec() },
                targetValueByState = { if (it == Frame.Start && start != TimelineState.Empty) 1f else 0f },
            )
            Canvas(Modifier.fillMaxSize().gradientOverlay()) {
                val boundingBox = placements[start]?.let { timelinePlacement.value?.localBoundingBoxOf(it) }

                if (boundingBox != null) {
                    val center = boundingBox.center
                    val width = if (boundingBox.center.x > size.width / 2f) center.x else size.width - center.x
                    val height = if (boundingBox.center.y > size.height / 2f) center.y else size.height - center.y
                    val size = sqrt(width * width + height * height)
                    drawCircle(Color.White, radius = fraction.value * size, center = center)
                }
            }
        }
    }
}