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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.old.magic.MagicTextDiff
import dev.bnorm.dcnyc25.old.magic.MagicTextMyers
import dev.bnorm.dcnyc25.old.magic.diff
import dev.bnorm.dcnyc25.sections.MyersDiffWordsState.*
import dev.bnorm.dcnyc25.template.OutlinedText
import dev.bnorm.dcnyc25.template.TextSurface
import dev.bnorm.dcnyc25.template.Vertical
import dev.bnorm.dcnyc25.template.code1
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

fun StoryboardBuilder.MyersDiffWords(sampleStart: AnnotatedString, sampleEnd: AnnotatedString, sampleEndNew: AnnotatedString) {
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
                    updateTransition(true),
                    state.createChildTransition { it.infoProgress },
                    Modifier.sharedElement(rememberSharedContentState("myers-diff"))
                )
            }

            Sample(
                state,
                sampleStart,
                sampleEnd,
                sampleEndNew,
                Modifier.sharedElement(rememberSharedContentState("diff-example"))
            )
        }
    }
}

@Composable
private fun Sample(
    state: Transition<MyersDiffWordsState>,
    sampleStart: AnnotatedString,
    sampleEnd: AnnotatedString,
    sampleNew: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()

    val style = MaterialTheme.typography.code1
    val measuredSampleStart = remember(sampleStart) { measurer.measure(sampleStart, style) }
    val measuredSampleEnd = remember(sampleEnd) { measurer.measure(sampleEnd, style) }
    val measuredSampleNew = remember(sampleNew) { measurer.measure(sampleNew, style) }

    // TODO instead of always adding the drawBehind, could this be an AnimateVisibility with a Box?

    val charDiff = remember(sampleStart, sampleEnd) { diff(sampleStart.toChars(), sampleEnd.toChars()) }
    val charMatchColor by state.animateColor(transitionSpec = { tween(750) }) {
        if (it.showCharColor) Color.Blue.copy(alpha = 0.5f)
        else Color.Blue.copy(alpha = 0f)
    }
    val charAddColor by state.animateColor(transitionSpec = { tween(750) }) {
        if (it.showCharColor) Color.Green.copy(alpha = 0.5f)
        else Color.Green.copy(alpha = 0f)
    }
    val charDeleteColor by state.animateColor(transitionSpec = { tween(750) }) {
        if (it.showCharColor) Color.Red.copy(alpha = 0.5f)
        else Color.Red.copy(alpha = 0f)
    }

    val wordDiff = remember(sampleStart, sampleEnd) { diff(sampleStart.toWords(), sampleEnd.toWords()) }
    val wordMatchColor by state.animateColor(transitionSpec = { tween(750) }) {
        if (it.showWordColor) Color.Blue.copy(alpha = 0.5f)
        else Color.Blue.copy(alpha = 0f)
    }
    val wordAddColor by state.animateColor(transitionSpec = { tween(750) }) {
        if (it.showWordColor) Color.Green.copy(alpha = 0.5f)
        else Color.Green.copy(alpha = 0f)
    }
    val wordDeleteColor by state.animateColor(transitionSpec = { tween(750) }) {
        if (it.showWordColor) Color.Red.copy(alpha = 0.5f)
        else Color.Red.copy(alpha = 0f)
    }

    val newDiff = remember(sampleEnd, sampleNew) { diff(sampleEnd.toWords(), sampleNew.toWords()) }
    val newMatchColor by state.animateColor(transitionSpec = { tween(750) }) {
        if (it.showNewColor) Color.Blue.copy(alpha = 0.5f)
        else Color.Blue.copy(alpha = 0f)
    }
    val newAddColor by state.animateColor(transitionSpec = { tween(750) }) {
        if (it.showNewColor) Color.Green.copy(alpha = 0.5f)
        else Color.Green.copy(alpha = 0f)
    }
    val newDeleteColor by state.animateColor(transitionSpec = { tween(750) }) {
        if (it.showNewColor) Color.Red.copy(alpha = 0.5f)
        else Color.Red.copy(alpha = 0f)
    }

    @Composable
    fun Before(modifier: Modifier = Modifier) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedText("Before", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                ProvideTextStyle(style) {
                    MagicTextMyers(
                        transition = state.createChildTransition {
                            when {
                                it.ordinal == AnimationFail.ordinal -> sampleNew.toWords()
                                it.ordinal >= AnimateSample.ordinal -> sampleEnd.toWords()
                                else -> sampleStart.toWords()
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .beforeCharHighlighting(charDiff, charDeleteColor, charMatchColor, measuredSampleStart)
                            .beforeCharHighlighting(wordDiff, wordDeleteColor, wordMatchColor, measuredSampleStart)
                            .beforeCharHighlighting(newDiff, newDeleteColor, newMatchColor, measuredSampleEnd),
                    )
                }
            }
        }
    }

    @Composable
    fun After(modifier: Modifier = Modifier) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedText("After", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Text(
                    text = sampleEnd,
                    style = style,
                    modifier = Modifier
                        .padding(16.dp)
                        .afterCharHighlighting(charDiff, charAddColor, charMatchColor, measuredSampleEnd)
                        .afterCharHighlighting(wordDiff, wordAddColor, wordMatchColor, measuredSampleEnd),
                )
            }
        }
    }

    @Composable
    fun NewAfter(modifier: Modifier = Modifier) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("After", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Text(
                    text = sampleNew,
                    style = style,
                    modifier = Modifier
                        .padding(16.dp)
                        .afterCharHighlighting(newDiff, newAddColor, newMatchColor, measuredSampleNew),
                )
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
                    NewAfter(Modifier.padding(horizontal = 16.dp))
                } else {
                    After(Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

private fun Modifier.afterCharHighlighting(
    charDiff: List<MagicTextDiff>,
    addColor: Color,
    matchColor: Color,
    measuredSampleEnd: TextLayoutResult,
): Modifier = drawBehind {
    var index = 0
    for (diff in charDiff) {
        val length = diff.after.length
        if (length == 0) continue

        val color = if (diff.before.text != diff.after.text) addColor else matchColor
        val box = measuredSampleEnd.getBoundingBox(index, index + length - 1)
        this.drawRoundRect(
            color = color,
            topLeft = box.topLeft,
            size = box.size,
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
        )
        index += length
    }
}

private fun Modifier.beforeCharHighlighting(
    charDiff: List<MagicTextDiff>,
    deleteColor: Color,
    matchColor: Color,
    measuredSampleStart: TextLayoutResult,
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
        val box = measuredSampleStart.getBoundingBox(index, index + length - 1)
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
