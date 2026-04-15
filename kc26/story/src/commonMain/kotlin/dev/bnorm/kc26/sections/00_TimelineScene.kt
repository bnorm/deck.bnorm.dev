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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.OutlinedText
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.toValue

enum class TimelineState(
    val title: String,
    val description: String?,
) {
    Empty("", null),
    Bundled("Kotlin 2.0", "Power-Assert\nBundled"),
    Improvements("", "Improvements\nand Problems"),
    Explanations("Kotlin 2.4", "Explanations"),

    // TODO money bad emoji for joke?
    // TODO eyes emoji for description?
    // TODO change to 2.8 as joke?
    Future("Kotlin 2.?", "???"),
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
    val dateStyle = MaterialTheme.typography.h5
    val descriptionStyle = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
    val outline = 1.dp
    val spacing = 8.dp
    val circleSize = 16.dp
    val lineHeight = 4.dp
    val lineWidth = 4.dp
    val lineLength = 32.dp

    @Composable
    fun <T> spec(delay: Int = 0): TweenSpec<T> = tween(500, delayMillis = delay, easing = EaseInOut)

    @Composable
    fun TimelinePoint(state: TimelineState) {
        transition.AnimatedVisibility(
            visible = { it.toValue(start, end) >= state },
            enter = fadeIn(spec(delay = 500)) + expandHorizontally(spec()),
            exit = fadeOut(spec()) + shrinkHorizontally(spec(delay = 500)),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                // TODO would be cool to animate this more
                //  1. dot should slide all the way from the right
                //  2. date should appear after the dot
                //  3. line should extend from the dot after the date
                //  4. description should appear after the line
                OutlinedText(text = state.title, style = dateStyle)
                Spacer(Modifier.size(spacing))
                Box(contentAlignment = Alignment.Center) {
                    Box(Modifier.size(circleSize).clip(CircleShape).background(Color.Black))
                    Box(Modifier.size(circleSize - outline).clip(CircleShape).background(Color.White))
                }
                if (state.description != null) {
                    Spacer(Modifier.size(spacing))
                    Box(contentAlignment = Alignment.Center) {
                        Box(Modifier.size(width = lineWidth, height = lineLength).background(Color.Black))
                        Box(
                            Modifier.size(width = lineWidth - outline, height = lineLength - outline)
                                .background(Color.White)
                        )
                    }
                    Spacer(Modifier.size(spacing))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(192.dp, 80.dp).padding(8.dp)
                        ) {
                            GradientText(
                                text = state.description,
                                style = descriptionStyle,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                        Box(Modifier.height(lineHeight - outline).fillMaxWidth().background(Color.White))
                    }
                }

                // Draw the points on the timeline.
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    for (entry in TimelineState.entries.drop(1)) {
                        TimelinePoint(entry)
                    }
                }
            }
        }
    }
}
