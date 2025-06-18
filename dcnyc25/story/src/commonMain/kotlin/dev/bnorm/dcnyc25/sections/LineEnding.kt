package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.CodeString
import dev.bnorm.dcnyc25.old.kc24.animateList
import dev.bnorm.dcnyc25.old.kc24.startAnimation
import dev.bnorm.dcnyc25.old.kc24.thenLineEndDiff
import dev.bnorm.dcnyc25.sections.LineEndingState.*
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
        showHighlight = false,
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
}

fun StoryboardBuilder.LineEnding(before: CodeString, after: CodeString) {
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
        val state = transition.createChildTransition { it.toState() }

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
                Bullet(step = 2, text = "Create a sequence which iterates from the first line to the last.")
                Bullet(step = 3, text = "Create substrings of the line by removing each non-prefix character.")
                Bullet(step = 4, text = "Continue creating substrings by adding each non-prefix character.")
                Bullet(step = 5, text = "Use 'animateIntAsState' with 'LinearEasing' to iterate through the sequence.")
            }
        }
    }
}

@Composable
private fun LineEndingSample(
    state: Transition<LineEndingState>,
    before: CodeString,
    after: CodeString,
    modifier: Modifier = Modifier,
) {
    val highlightDiff = state.createChildTransition { it.showHighlight }
    val highlightPrefix = state.createChildTransition { it == Algorithm1 || it == Algorithm2 || it == Algorithm3 || it == Algorithm4 }
    val highlightDelete = state.createChildTransition { it == Algorithm3 || it == Algorithm4 }
    val highlightAdd = state.createChildTransition { it == Algorithm4 }

    val commonPrefix = remember {
        // TODO ew
        listOf(
            "fun main() {",
            "  println(\"Hello, ",
            "}",
        )
    }

    val sampleAnimation = remember(before.text, after.text) {
        startAnimation(before.text)
            .thenLineEndDiff(after.text)
            .toList()
    }

    val sampleText by state.animateList(
        values = sampleAnimation,
        transitionSpec = { typing(sampleAnimation.size) }
    ) {
        if (it == SampleAlgorithm) sampleAnimation.lastIndex else 0
    }

    @Composable
    fun Before(modifier: Modifier = Modifier) {
        var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                OutlinedText("Before", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Box(Modifier.padding(16.dp)) {
                    HighlightStrings(
                        visible = highlightDiff,
                        strings = listOf("KotlinConf"),
                        color = DeleteColor,
                        textLayout = { textLayout!! },
                    )
                    HighlightStrings(
                        visible = highlightPrefix,
                        strings = commonPrefix,
                        color = MatchColor,
                        textLayout = { textLayout!! },
                    )
                    HighlightCharacters(
                        visible = highlightDelete,
                        strings = listOf("KotlinConf!\")"),
                        color = DeleteColor,
                        textLayout = { textLayout!! },
                    )
                    Text(
                        text = sampleText,
                        style = MaterialTheme.typography.code1,
                        onTextLayout = { textLayout = it },
                    )
                }
            }
        }
    }

    @Composable
    fun After(modifier: Modifier = Modifier) {
        var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                OutlinedText("After", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Box(Modifier.padding(16.dp)) {
                    HighlightStrings(
                        visible = highlightDiff,
                        strings = listOf("droidcon"),
                        color = AddColor,
                        textLayout = { textLayout!! },
                    )
                    HighlightStrings(
                        visible = highlightPrefix,
                        strings = commonPrefix,
                        color = MatchColor,
                        textLayout = { textLayout!! },
                    )
                    HighlightCharacters(
                        visible = highlightAdd,
                        strings = listOf("droidcon!\")"),
                        color = AddColor,
                        textLayout = { textLayout!! },
                    )
                    Text(
                        text = after.text,
                        style = MaterialTheme.typography.code1,
                        onTextLayout = { textLayout = it },
                    )
                }
            }
        }
    }

    SharedTransitionLayout(modifier) {
        state.AnimatedContent(transitionSpec = { EnterTransition.None togetherWith ExitTransition.None }) {
            Row {
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

