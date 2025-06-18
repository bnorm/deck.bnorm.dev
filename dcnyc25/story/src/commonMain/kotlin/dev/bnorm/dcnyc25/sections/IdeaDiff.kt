package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.CodeString
import dev.bnorm.dcnyc25.old.magic.toWords
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.Idea(sampleStart: CodeString, sampleEnd: CodeString) {
    scene(
        stateCount = 9,
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.BottomCenter),
            end = SceneEnter(alignment = Alignment.CenterEnd),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.BottomCenter),
            end = SceneExit(alignment = Alignment.CenterEnd),
        ),
    ) {
        val state = transition.createChildTransition { it.toState() }

        Row {
            Vertical(MaterialTheme.colors.primary) {
                IdeaInfo(state)
            }

            Vertical(
                MaterialTheme.colors.secondary,
                modifier = Modifier.sharedElement(rememberSharedContentState("diff-example"))
            ) {
                IdeaSample(
                    state.createChildTransition { it == 5 },
                    sampleStart,
                    sampleEnd,
                    state.createChildTransition { it <= 1 },
                    state.createChildTransition { it in 2..3 },
                )
            }
        }
    }
}

@Composable
fun IdeaInfo(progress: Transition<Int>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row {
                OutlinedText("Idea", style = MaterialTheme.typography.h2)
            }
        }
        TextSurface {
            @Composable
            fun Bullet(step: Int, text: String) {
                progress.AnimatedVisibility(
                    visible = { it >= step },
                    enter = fadeIn(tween(750)), exit = fadeOut(tween(750)),
                ) {
                    Text("• $text")
                }
            }

            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(BulletSpacing)) {
                Bullet(step = 1, text = "Match unique elements which appear in both sequences.")
                Bullet(step = 2, text = "Expand match ranges in both sequences while beginning and end match.")
                Bullet(step = 3, text = "Repeat for remaining, unmatched elements.")
                Bullet(step = 7, text = "Some visual inconsistencies, but handles many code changes well.")
                Bullet(step = 8, text = "Use LCS idea from Patience to improve expansion order?")
            }
        }
    }
}

@Composable
private fun IdeaSample(
    showAfter: Transition<Boolean>,
    before: CodeString,
    after: CodeString,
    showUnique: Transition<Boolean>,
    showExpanded: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {
    val unique = remember(before.text, after.text) {
        val beforeUnique = before.text.toWords().map { it.text }
            .groupingBy { it }.eachCount()
            .filter { it.value == 1 }.keys

        val afterUnique = after.text.toWords().map { it.text }
            .groupingBy { it }.eachCount()
            .filter { it.value == 1 }.keys

        (beforeUnique intersect afterUnique).toList()
    }

    val expanded = remember {
        // TODO ew
        listOf(
            "fun ",
            "main() ",
            "{",
            "  println(\"",
            "Hello",
            ", ",
            "droidcon!\")",
            "}",
        )
    }

    @Composable
    fun Before(modifier: Modifier = Modifier) {
        val measurer = rememberTextMeasurer()
        val style = MaterialTheme.typography.code1
        val textLayout = remember(before.text) { measurer.measure(before.text, style = style) }

        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedText("Before", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Box(Modifier.padding(16.dp)) {
                    HighlightStrings(
                        visible = showUnique,
                        strings = unique,
                        color = MatchColor,
                        textLayout = { textLayout },
                    )
                    HighlightStrings(
                        visible = showExpanded,
                        strings = expanded,
                        color = MatchColor,
                        textLayout = { textLayout },
                    )

                    ProvideTextStyle(style) {
                        MagicText(
                            transition = showAfter.createChildTransition {
                                if (it) after.text.toWords() else before.text.toWords()
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
                    HighlightStrings(
                        visible = showUnique,
                        strings = unique,
                        color = MatchColor,
                        textLayout = { textLayout!! },
                    )
                    HighlightStrings(
                        visible = showExpanded,
                        strings = expanded,
                        color = MatchColor,
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

    Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Before(Modifier.weight(1f))
        After(Modifier.weight(1f))
    }
}
