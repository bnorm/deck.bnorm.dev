package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateRect
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
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
    SampleAlgorithm(
        showHighlight = false,
        showInfo = true,
        infoProgress = 4,
    ),
    RevertAlgorithm(
        showHighlight = false,
        showInfo = true,
        infoProgress = 4,
    ),
}

fun StoryboardBuilder.LineEnding(before: AnnotatedString, after: AnnotatedString) {
    scene(
        states = LineEndingState.entries,
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
                LineEndingInfo(state)
            }

            LineEndingSample(
                state,
                before,
                after,
                modifier = Modifier.sharedElement(rememberSharedContentState("diff-example")),
            )
        }
    }
}

@Composable
private fun LineEndingInfo(state: Transition<LineEndingState>) {
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

            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(BulletSpacing)) {
                Bullet(step = 1, text = "Find the common prefix of each line.")
                Bullet(step = 2, text = "Create a sequence of removing characters from the last line to the first.")
                Bullet(step = 3, text = "Continue the sequence by adding characters from the first line to the last.")
                Bullet(step = 4, text = "Use 'animateIntAsState' with 'LinearEasing' to iterate through the sequence.")
            }
        }
    }
}

@Composable
private fun LineEndingSample(
    state: Transition<LineEndingState>,
    before: AnnotatedString,
    after: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()
    val sampleStyle = MaterialTheme.typography.code1

    val beforeHighlight = remember(sampleStyle) {
        val sub = "KotlinConf"
        val index = before.text.indexOf(sub)
        measurer.measure(before.text, style = sampleStyle)
            .getBoundingBox(index, index + sub.length - 1)
    }

    val beforeLineHighlight = remember(sampleStyle) {
        val sub = "KotlinConf!\")"
        val index = before.text.indexOf(sub)
        measurer.measure(before.text, style = sampleStyle)
            .getBoundingBox(index, index + sub.length - 1)
    }

    val afterHighlight = remember(sampleStyle) {
        val sub = "droidcon"
        val index = after.text.indexOf(sub)
        measurer.measure(after.text, style = sampleStyle)
            .getBoundingBox(index, index + sub.length - 1)
    }

    val afterLineHighlight = remember(sampleStyle) {
        val sub = "droidcon!\")"
        val index = after.text.indexOf(sub)
        measurer.measure(after.text, style = sampleStyle)
            .getBoundingBox(index, index + sub.length - 1)
    }

    val sampleAnimation = remember {
        startAnimation(before)
            .thenLineEndDiff(after)
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
                                if (it.showHighlight) DeleteColor.copy(alpha = 0.5f) else DeleteColor.copy(alpha = 0f)
                            }
                            val rect by state.animateRect(transitionSpec = { tween(durationMillis = 750) }) {
                                if (it >= HighlightLineEndDiff) beforeLineHighlight else beforeHighlight
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
                                if (it.showHighlight) AddColor else AddColor.copy(alpha = 0f)
                            }
                            val rect by state.animateRect(transitionSpec = { tween(durationMillis = 750) }) {
                                if (it >= HighlightLineEndDiff) afterLineHighlight else afterHighlight
                            }
                            Text(
                                text = after,
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

