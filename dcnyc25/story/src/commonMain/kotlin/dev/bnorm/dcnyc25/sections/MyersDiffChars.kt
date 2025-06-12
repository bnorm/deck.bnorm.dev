package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.storyboard.SceneEnterTransition
import dev.bnorm.storyboard.SceneExitTransition
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.MyersDiffChars() {
    val sampleStart = """
        fun main() {
          println("Hello, KotlinConf!")
        }
    """.trimIndent().highlight(INTELLIJ_LIGHT, language = Language.Kotlin)

    val sampleEnd = """
        fun main() {
          println("Hello, droidcon!")
        }
    """.trimIndent().highlight(INTELLIJ_LIGHT, language = Language.Kotlin)

    scene(
        stateCount = 3,
        enterTransition = enter(
            start = SceneEnterTransition.None,
            end = SceneEnter(alignment = Alignment.BottomCenter),
        ),
        exitTransition = exit(
            start = SceneExitTransition.None,
            end = SceneExit(alignment = Alignment.BottomCenter),
        ),
    ) {
        val state = transition.createChildTransition { it.toState() }

        val scrollState = rememberScrollState()
        state.animateScroll(scrollState, transitionSpec = { tween(durationMillis = 750) }) {
            with(LocalDensity.current) {
                (SceneHalfWidth * when (it) {
                    in 1..2 -> 0
                    else -> 1
                }).roundToPx()
            }
        }

        @Composable
        fun Before(modifier: Modifier = Modifier) {
            Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Before", style = MaterialTheme.typography.h2)
                }
                TextSurface {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicText(
                            transition = state.createChildTransition {
                                if (it >= 2) sampleEnd.toChars() else sampleStart.toChars()
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
                    Text("After", style = MaterialTheme.typography.h2)
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

        Row(Modifier.horizontalScroll(scrollState, enabled = false)) {
            Vertical(MaterialTheme.colors.primary) {
                MyersDiffInfo(Modifier.sharedElement(rememberSharedContentState("myers-diff")))
            }

            SharedTransitionLayout {
                state.AnimatedContent(transitionSpec = { EnterTransition.None togetherWith ExitTransition.None }) {
                    val shared = it in 1..2
                    Row {
                        Vertical(MaterialTheme.colors.secondary) {
                            Column(Modifier.padding(16.dp)) {
                                Before(
                                    Modifier.weight(1f).sharedElement(
                                        rememberSharedContentState("before"),
                                        boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                                    )
                                )
                                if (shared) {
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
                            Box(Modifier.padding(16.dp)) {
                                if (!shared) {
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
    }
}
