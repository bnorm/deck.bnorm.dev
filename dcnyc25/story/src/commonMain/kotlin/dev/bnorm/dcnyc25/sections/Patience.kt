package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.old.magic.MagicTextMyers
import dev.bnorm.dcnyc25.old.magic.toWords
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
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.Patience(sampleStart: AnnotatedString, sampleEnd: AnnotatedString) {
    scene(
        stateCount = 3,
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
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        OutlinedText("Patience", style = MaterialTheme.typography.h2)
                    }
                    TextSurface {
                        // TODO
                    }
                }
            }

            PatienceSample(
                state,
                sampleEnd,
                sampleStart,
                Modifier.sharedElement(rememberSharedContentState("diff-example"))
            )
        }
    }
}

@Composable
private fun PatienceSample(
    state: Transition<Int>,
    sampleEnd: AnnotatedString,
    sampleStart: AnnotatedString,
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
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicTextMyers(
                            transition = state.createChildTransition {
                                if (it >= 2) sampleEnd.toWords() else sampleStart.toWords()
                            },
                            modifier = Modifier.padding(16.dp),
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
