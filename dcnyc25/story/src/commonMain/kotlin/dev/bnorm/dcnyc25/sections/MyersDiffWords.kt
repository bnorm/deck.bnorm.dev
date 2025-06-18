package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.CodeString
import dev.bnorm.dcnyc25.old.magic.MagicTextDiff
import dev.bnorm.dcnyc25.old.magic.MagicTextMyers
import dev.bnorm.dcnyc25.old.magic.diff
import dev.bnorm.dcnyc25.sections.MyersDiffWordsState.*
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.text.magic.toWords
import dev.bnorm.storyboard.toState

// TODO sequence
//  1. highlight character diff
//  2. change info from string to sequence
//  3. highlight word diff
//  4. remove highlighting
//  5. animate sample
//  6. introduce new after
//  7. animation fail
//  8. reverse animation fail

private enum class MyersDiffWordsState(
    val showCharColor: Boolean,
    val showWordColor: Boolean,
    val showNewColor: Boolean,
    val infoProgress: Int,
) {
    Start(
        showCharColor = false,
        showWordColor = false,
        showNewColor = false,
        infoProgress = 4,
    ),
    HighlightCharDiff(
        showCharColor = true,
        showWordColor = false,
        showNewColor = false,
        infoProgress = 4
    ),
    InfoUpdate(
        showCharColor = true,
        showWordColor = false,
        showNewColor = false,
        infoProgress = 5,
    ),
    HighlightWordDiff(
        showCharColor = false,
        showWordColor = true,
        showNewColor = false,
        infoProgress = 5
    ),
    RemoveWordHighlighting(
        showCharColor = false,
        showWordColor = false,
        showNewColor = false,
        infoProgress = 5,
    ),
    AnimateSample(
        showCharColor = false,
        showWordColor = false,
        showNewColor = false,
        infoProgress = 5,
    ),
    IntroduceNewAfter(
        showCharColor = false,
        showWordColor = false,
        showNewColor = false,
        infoProgress = 5,
    ),
    AnimationFail(
        showCharColor = false,
        showWordColor = false,
        showNewColor = false,
        infoProgress = 5,
    ),
    ReverseAnimationFail(
        showCharColor = false,
        showWordColor = false,
        showNewColor = false,
        infoProgress = 5,
    ),
    HighlightNewDiff(
        showCharColor = false,
        showWordColor = false,
        showNewColor = true,
        infoProgress = 5,
    ),
    RemoveNewHighlighting(
        showCharColor = false,
        showWordColor = false,
        showNewColor = false,
        infoProgress = 5,
    ),
}

fun StoryboardBuilder.MyersDiffWords(before: CodeString, after: CodeString, problem: CodeString) {
    scene(
        states = MyersDiffWordsState.entries.toList(),
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.TopCenter),
            end = SceneEnter(alignment = Alignment.BottomCenter),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.TopCenter),
            end = SceneExit(alignment = Alignment.BottomCenter),
        ),
    ) {
        val state = transition.createChildTransition { it.toState() }

        Row {
            Vertical(MaterialTheme.colors.primary) {
                MyersDiffInfo(
                    updateTransition(targetState = true),
                    state.createChildTransition { it.infoProgress },
                    Modifier.sharedElement(rememberSharedContentState("myers-diff"))
                )
            }

            Sample(
                state = state,
                before = before,
                after = after,
                problem = problem,
                modifier = Modifier.sharedElement(rememberSharedContentState("diff-example"))
            )
        }
    }
}

@Composable
private fun Sample(
    state: Transition<MyersDiffWordsState>,
    before: CodeString,
    after: CodeString,
    problem: CodeString,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()

    val codeStyle = MaterialTheme.typography.code1
    val measuredBefore = remember(before.text, codeStyle) { measurer.measure(before.text, codeStyle) }
    val measuredAfter = remember(after.text, codeStyle) { measurer.measure(after.text, codeStyle) }

    val charDiff = remember(before.text, after.text) { diff(before.text.toChars(), after.text.toChars()) }
    val wordDiff = remember(before.text, after.text) { diff(before.text.toWords(), after.text.toWords()) }
    val problemDiff = remember(after.text, problem.text) { diff(after.text.toWords(), problem.text.toWords()) }

    @Composable
    fun BoxScope.BeforeHighlighting(
        visible: (MyersDiffWordsState) -> Boolean,
        diffs: List<MagicTextDiff>,
        measured: TextLayoutResult,
        modifier: Modifier = Modifier,
    ) {
        state.AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(750)),
            exit = fadeOut(tween(750)),
            modifier = modifier.matchParentSize()
        ) {
            Box(Modifier.beforeHighlighting(diffs, DeleteColor, MatchColor, measured))
        }
    }

    @Composable
    fun BoxScope.AfterHighlighting(
        visible: (MyersDiffWordsState) -> Boolean,
        diffs: List<MagicTextDiff>,
        textLayout: () -> TextLayoutResult,
        modifier: Modifier = Modifier,
    ) {
        state.AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(750)),
            exit = fadeOut(tween(750)),
            modifier = modifier.matchParentSize()
        ) {
            Box(Modifier.afterHighlighting(diffs, AddColor, MatchColor, textLayout))
        }
    }

    @Composable
    fun Before(modifier: Modifier = Modifier) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedText("Before", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                ProvideTextStyle(codeStyle) {
                    Box(Modifier.padding(16.dp)) {
                        BeforeHighlighting(visible = { it.showCharColor }, charDiff, measuredBefore)
                        BeforeHighlighting(visible = { it.showWordColor }, wordDiff, measuredBefore)
                        BeforeHighlighting(visible = { it.showNewColor }, problemDiff, measuredAfter)
                        MagicTextMyers(
                            transition = state.createChildTransition {
                                when {
                                    it.ordinal == AnimationFail.ordinal -> problem.text.toWords()
                                    it.ordinal >= AnimateSample.ordinal -> after.text.toWords()
                                    else -> before.text.toWords()
                                }
                            },
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun After(modifier: Modifier = Modifier) {
        var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }

        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedText("After", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Box(Modifier.padding(16.dp)) {
                    AfterHighlighting(visible = { it.showCharColor }, charDiff, { textLayout!! })
                    AfterHighlighting(visible = { it.showWordColor }, wordDiff, { textLayout!! })
                    Text(
                        text = after.text,
                        style = codeStyle,
                        onTextLayout = { textLayout = it },
                    )
                }
            }
        }
    }

    @Composable
    fun Problem(modifier: Modifier = Modifier) {
        var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }

        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("After", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Box(Modifier.padding(16.dp)) {
                    AfterHighlighting(visible = { it.showNewColor }, problemDiff, { textLayout!! })
                    Text(
                        text = problem.text,
                        style = codeStyle,
                        onTextLayout = { textLayout = it },
                    )
                }
            }
        }
    }

    Vertical(MaterialTheme.colors.secondary, modifier = modifier) {
        Column(Modifier.padding(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Before(Modifier.weight(1f).padding(horizontal = 16.dp))
            state.createChildTransition { it.ordinal >= IntroduceNewAfter.ordinal }.AnimatedContent(
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    if (targetState) {
                        slideInHorizontally(tween(750)) { it } togetherWith slideOutHorizontally(tween(750)) { -it }
                    } else {
                        slideInHorizontally(tween(750)) { -it } togetherWith slideOutHorizontally(tween(750)) { it }
                    }
                }
            ) {
                if (it) {
                    Problem(Modifier.padding(horizontal = 16.dp))
                } else {
                    After(Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

private fun Modifier.afterHighlighting(
    charDiff: List<MagicTextDiff>,
    addColor: Color,
    matchColor: Color,
    textLayout: () -> TextLayoutResult,
): Modifier = drawBehind {
    val textLayout = textLayout()

    var index = 0
    for (diff in charDiff) {
        val length = diff.after.length
        if (length == 0) continue

        val color = if (diff.before.text != diff.after.text) addColor else matchColor
        val box = textLayout.getBoundingBox(index, index + length - 1)
        drawRoundRect(
            color = color,
            topLeft = box.topLeft,
            size = box.size,
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
        )
        index += length
    }
}

private fun Modifier.beforeHighlighting(
    charDiff: List<MagicTextDiff>,
    deleteColor: Color,
    matchColor: Color,
    textLayout: TextLayoutResult,
): Modifier = drawBehind {
    // !!!
    // There's an every so subtle difference between Text and Row { Text() }.
    // The 0.2f offset is an attempt to overcome that difference.
    // TODO figure out what causes this and fix it in MagicText?
    // !!!

    var index = 0
    var offset = 0
    for (diff in charDiff) {
        val length = diff.before.length
        if (length == 0) continue

        if (diff.before.text == "\n") {
            index += 1
            offset = 0
            continue
        }

        val color = if (diff.before.text != diff.after.text) deleteColor else matchColor
        val box = textLayout.getBoundingBox(index, index + length - 1)
        drawRoundRect(
            color = color,
            topLeft = box.topLeft + Offset(x = offset * 0.2f, y = 0f),
            size = box.size,
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
        )

        index += length
        offset += length
    }
}
