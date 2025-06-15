package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.old.kc24.animateList
import dev.bnorm.dcnyc25.old.kc24.startAnimation
import dev.bnorm.dcnyc25.old.kc24.thenLineEndDiff
import dev.bnorm.dcnyc25.sections.LineEndingState.HighlightLineEndDiff
import dev.bnorm.dcnyc25.sections.LineEndingState.SampleAlgorithm
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.toState

private enum class LineEndingState(
    val showHighlight: Boolean,
    val showInfo: Boolean,
    val infoProgress: Int,
) {
    Sample(
        showHighlight = false,
        showInfo = false,
        infoProgress = 0,
    ),
    HighlightDiff(
        showHighlight = true,
        showInfo = false,
        infoProgress = 0,
    ),
    IntroAlgorithm(
        showHighlight = true,
        showInfo = true,
        infoProgress = 0,
    ),
    HighlightLineEndDiff(
        showHighlight = true,
        showInfo = true,
        infoProgress = 0,
    ),
    Algorithm1(
        showHighlight = false,
        showInfo = true,
        infoProgress = 1,
    ),
    Algorithm2(
        showHighlight = false,
        showInfo = true,
        infoProgress = 2,
    ),
    Algorithm3(
        showHighlight = false,
        showInfo = true,
        infoProgress = 3,
    ),
    Algorithm4(
        showHighlight = false,
        showInfo = true,
        infoProgress = 4,
    ),
    Algorithm5(
        showHighlight = false,
        showInfo = true,
        infoProgress = 5,
    ),
    SampleAlgorithm(
        showHighlight = false,
        showInfo = true,
        infoProgress = 5,
    ),
    RevertAlgorithm(
        showHighlight = false,
        showInfo = true,
        infoProgress = 5,
    ),
    Algorithm6(
        showHighlight = false,
        showInfo = true,
        infoProgress = 6,
    ),
}

fun StoryboardBuilder.LineEnding(sampleStart: AnnotatedString, sampleEnd: AnnotatedString) {
    scene(
        states = LineEndingState.entries.subList(fromIndex = 0, toIndex = LineEndingState.entries.size - 1),
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.CenterEnd),
            end = SceneEnter(alignment = Alignment.BottomCenter),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.CenterEnd),
            end = SceneExit(alignment = Alignment.BottomCenter),
        ),
    ) {

        val state = transition.createChildTransition {
            it.toState()
        }

        val scrollState = rememberScrollState()
        state.animateScroll(scrollState, transitionSpec = { tween(durationMillis = 750) }) {
            with(LocalDensity.current) { (SceneHalfWidth * if (it.showInfo) 0 else 1).roundToPx() }
        }

        Row(Modifier.horizontalScroll(scrollState, enabled = false)) {
            Vertical(MaterialTheme.colors.primary) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        OutlinedText("Line Ending", style = MaterialTheme.typography.h2)
                    }
                    TextSurface {
                        @Composable
                        fun Bullet(step: Int, text: String) {
                            state.AnimatedVisibility(
                                visible = { it.infoProgress >= step },
                                enter = fadeIn(tween(750)), exit = fadeOut(tween(750)),
                            ) {
                                Text("• $text")
                            }
                        }

                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Bullet(step = 1, text = "Find the common prefix of each line.")
                            Bullet(
                                step = 2,
                                text = "Create a sequence removing all non-prefix characters - one at a time - from the last line to the first."
                            )
                            Bullet(
                                step = 3,
                                text = "Continue the sequence adding non-prefix characters from the first line to the last."
                            )
                            Bullet(
                                step = 4,
                                text = "Use 'animateIntAsState' or similar to iterate through the sequence."
                            )
                            Bullet(step = 5, text = "Combine with 'LinearEasing' to get a cursor like animation!")
                            Bullet(step = 6, text = "TODO: animate an actual cursor.")
                        }
                    }
                }
            }

            SampleDiff(
                state,
                modifier = Modifier.sharedElement(rememberSharedContentState("diff-example"),),
                sampleStart,
                sampleEnd,
            )
        }
    }
}

@Composable
private fun SampleDiff(
    state: Transition<LineEndingState>,
    modifier: Modifier = Modifier,
    sampleStart: AnnotatedString,
    sampleEnd: AnnotatedString,
) {
    val measurer = rememberTextMeasurer()
    val sampleStyle = MaterialTheme.typography.code1

    val startHighlight = remember {
        val sub = "KotlinConf"
        val index = sampleStart.text.indexOf(sub)
        measurer.measure(sampleStart.text, style = sampleStyle)
            .getBoundingBox(index, index + sub.length - 1)
    }

    val startLineHighlight = remember {
        val sub = "KotlinConf!\")"
        val index = sampleStart.text.indexOf(sub)
        measurer.measure(sampleStart.text, style = sampleStyle)
            .getBoundingBox(index, index + sub.length - 1)
    }

    val endHighlight = remember {
        val sub = "droidcon"
        val index = sampleEnd.text.indexOf(sub)
        measurer.measure(sampleEnd.text, style = sampleStyle)
            .getBoundingBox(index, index + sub.length - 1)
    }

    val endLineHighlight = remember {
        val sub = "droidcon!\")"
        val index = sampleEnd.text.indexOf(sub)
        measurer.measure(sampleEnd.text, style = sampleStyle)
            .getBoundingBox(index, index + sub.length - 1)
    }

    val sampleAnimation = remember {
        startAnimation(sampleStart)
            .thenLineEndDiff(sampleEnd)
            .toList()
    }

    val sampleText by state.animateList(
        values = sampleAnimation,
        transitionSpec = { typing(sampleAnimation.size) }
    ) {
        if (it == SampleAlgorithm) sampleAnimation.lastIndex else 0
    }

    SharedTransitionLayout(modifier) {
        state.AnimatedContent(transitionSpec = { EnterTransition.None togetherWith ExitTransition.None }) {
            Row {
                @Composable
                fun Before(modifier: Modifier = Modifier) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                            OutlinedText("Before", style = MaterialTheme.typography.h2)
                        }
                        TextSurface {
                            val color by state.animateColor(transitionSpec = { tween(durationMillis = 750) }) {
                                if (it.showHighlight) Color.Red.copy(alpha = 0.5f) else Color.Red.copy(alpha = 0f)
                            }
                            val rect by state.animateRect(transitionSpec = { tween(durationMillis = 750) }) {
                                if (it >= HighlightLineEndDiff) startLineHighlight else startHighlight
                            }
                            Text(
                                text = sampleText,
                                style = sampleStyle,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .highlight(rect, color, radius = 8.dp, padding = 2.dp)
                            )
                        }
                    }
                }

                @Composable
                fun After(modifier: Modifier = Modifier) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                            OutlinedText("After", style = MaterialTheme.typography.h2)
                        }
                        TextSurface {
                            val color by state.animateColor(transitionSpec = { tween(durationMillis = 750) }) {
                                if (it.showHighlight) Color.Green.copy(alpha = 0.5f) else Color.Green.copy(alpha = 0f)
                            }
                            val rect by state.animateRect(transitionSpec = { tween(durationMillis = 750) }) {
                                if (it >= HighlightLineEndDiff) endLineHighlight else endHighlight
                            }
                            Text(
                                text = sampleEnd,
                                style = sampleStyle,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .highlight(rect, color, radius = 8.dp, padding = 2.dp)
                            )
                        }
                    }
                }

                Vertical(MaterialTheme.colors.secondary) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Before(
                            Modifier.weight(1f).sharedElement(
                                rememberSharedContentState("before"),
                                boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                            )
                        )
                        if (it.showInfo) {
                            After(
                                Modifier.weight(1f).sharedElement(
                                    rememberSharedContentState("after"),
                                    boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                                )
                            )
                        }
                    }
                }
                Vertical(MaterialTheme.colors.primary) {
                    if (!it.showInfo) {
                        Box(Modifier.padding(16.dp)) {
                            After(
                                Modifier.sharedElement(
                                    rememberSharedContentState("after"),
                                    boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

fun TextLayoutResult.getBoundingBox(from: Int, to: Int): Rect {
    require(from <= to) { "from ($from) should be less than or equal to to ($to)" }
    require(from >= 0) { "from ($from) should be greater than or equal to 0" }
    require(to <= layoutInput.text.length) { "to ($to) should be less than or equal to length (${layoutInput.text.length})" }

    val fromBox = getBoundingBox(from)
    val toBox = if (from == to) fromBox else getBoundingBox(to)

    return Rect(
        left = minOf(fromBox.left, toBox.left),
        top = minOf(fromBox.top, toBox.top),
        right = maxOf(fromBox.right, toBox.right),
        bottom = maxOf(fromBox.bottom, toBox.bottom),
    )
}

fun Modifier.highlight(
    rect: Rect,
    color: Color,
    radius: Dp = Dp.Hairline,
    padding: Dp = Dp.Hairline,
): Modifier {
    return drawBehind {
        val topLeft = rect.topLeft
        val bottomRight = rect.bottomRight
        val padding = padding.toPx()
        val radius = radius.toPx()
        drawRoundRect(
            color = color,
            topLeft = topLeft - Offset(padding, padding),
            size = Size(
                width = bottomRight.x - topLeft.x + 2 * padding,
                height = bottomRight.y - topLeft.y + 2 * padding,
            ),
            cornerRadius = CornerRadius(radius, radius),
        )
    }
}

