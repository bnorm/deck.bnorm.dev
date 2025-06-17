package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.CodeString
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.PatienceStart(before: CodeString, after: CodeString) {
    scene(
        stateCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {
        val state = transition.createChildTransition { it.toState() }

        Patience(state, before, after)
    }
}

fun StoryboardBuilder.PatienceEnd(before: CodeString, after: CodeString) {
    scene(
        stateCount = 3,
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.TopCenter),
            end = SceneEnter(alignment = Alignment.BottomCenter),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.TopCenter),
            end = SceneExit(alignment = Alignment.BottomCenter),
        ),
    ) {
        val state = transition.createChildTransition { it.toState() + 3 }

        Patience(state, before, after)
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun Patience(
    state: Transition<Int>,
    before: CodeString,
    after: CodeString,
) {
    Row {
        Vertical(MaterialTheme.colors.primary) {
            PatienceInfo(
                state,
                modifier = Modifier.sharedElement(rememberSharedContentState("patience-info")),
            )
        }

        Vertical(
            MaterialTheme.colors.secondary,
            modifier = Modifier.sharedElement(rememberSharedContentState("diff-example")),
        ) {
            PatienceSample(before, after)
        }
    }
}

@Composable
fun PatienceInfo(progress: Transition<Int>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row {
                OutlinedText("Patience", style = MaterialTheme.typography.h2)
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
                Bullet(step = 1, text = "Created by Bram Cohen of BitTorrent fame.")
                Bullet(step = 2, text = "Finds a shared set of unique elements from each sequence.")
                Bullet(
                    step = 3,
                    text = "Computes the longest common subsequence (LCS) of the unique element sequences."
                )
                Bullet(
                    step = 4,
                    text = "Breaks up the original sequences based on the LCS and repeats on each section."
                )
                Bullet(step = 5, text = "Falls back to Myers if no unique elements are found in both sequences.")
            }
        }
    }
}

@Composable
private fun PatienceSample(
    before: CodeString,
    after: CodeString,
    modifier: Modifier = Modifier,
) {
    @Composable
    fun Before(modifier: Modifier = Modifier) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedText("Before", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Text(
                    text = before.text,
                    style = MaterialTheme.typography.code1,
                    modifier = Modifier.padding(16.dp),
                )
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
                    text = after.text,
                    style = MaterialTheme.typography.code1,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }

    Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Before(Modifier.weight(1f))
        After(Modifier.weight(1f))
    }
}
