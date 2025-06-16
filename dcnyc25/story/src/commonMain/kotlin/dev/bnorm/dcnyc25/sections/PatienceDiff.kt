package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.template.BulletSpacing
import dev.bnorm.dcnyc25.template.OutlinedText
import dev.bnorm.dcnyc25.template.TextSurface
import dev.bnorm.dcnyc25.template.Vertical
import dev.bnorm.dcnyc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.Patience(after: AnnotatedString, before: AnnotatedString) {
    scene(
        stateCount = 6,
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {
        val state = transition.createChildTransition { it.toState() }

        Row {
            Vertical(MaterialTheme.colors.primary) {
                PatienceInfo(state)
            }

            PatienceSample(
                before,
                after,
                Modifier.sharedElement(rememberSharedContentState("diff-example")),
            )
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
                Bullet(step = 3, text = "Computes the longest common subsequence (LCS) of the unique element sequences.")
                Bullet(step = 4, text = "Breaks up the original sequences based on the LCS and repeats on each section.")
                Bullet(step = 5, text = "Falls back to Myers if no unique elements are found in both sequences.")
            }
        }
    }
}

@Composable
private fun PatienceSample(
    before: AnnotatedString,
    after: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    Vertical(
        MaterialTheme.colors.secondary,
        modifier = modifier
    ) {
        @Composable
        fun Before(modifier: Modifier = Modifier) {
            Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    OutlinedText("Before", style = MaterialTheme.typography.h2)
                }
                TextSurface {
                    Text(
                        text = after,
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
                        text = before,
                        style = MaterialTheme.typography.code1,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }

        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Before(Modifier.weight(1f))
            After(Modifier.weight(1f))
        }
    }
}
