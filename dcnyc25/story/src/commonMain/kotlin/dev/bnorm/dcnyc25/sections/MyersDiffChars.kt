package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.sections.MyersDiffState.SampleAlgorithm
import dev.bnorm.dcnyc25.template.TextSurface
import dev.bnorm.dcnyc25.template.Vertical
import dev.bnorm.dcnyc25.template.code1
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toState

enum class MyersDiffState(
    val showName: Boolean,
    val infoProgress: Int,
) {
    IntroAlgorithm(
        showName = false,
        infoProgress = 0,
    ),
    Algorithm1(
        showName = true,
        infoProgress = 1,
    ),
    Algorithm2(
        showName = true,
        infoProgress = 2,
    ),
    Algorithm3(
        showName = true,
        infoProgress = 3,
    ),
    Algorithm4(
        showName = true,
        infoProgress = 4,
    ),
    SampleAlgorithm(
        showName = true,
        infoProgress = 4,
    ),
    ReverseAlgorithm(
        showName = true,
        infoProgress = 4,
    ),
}

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
        states = MyersDiffState.entries.toList(),
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {
        val state = transition.createChildTransition { it.toState() }

        Row {
            Vertical(MaterialTheme.colors.primary) {
                MyersDiffInfo(
                    state.createChildTransition { it.showName },
                    state.createChildTransition { it.infoProgress },
                    modifier = Modifier.sharedElement(rememberSharedContentState("myers-diff")),
                )
            }

            MyersDiffCharsExample(
                state, sampleEnd, sampleStart,
                modifier = Modifier.sharedElement(rememberSharedContentState("diff-example")),
            )
        }
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun MyersDiffCharsExample(
    state: Transition<MyersDiffState>,
    sampleEnd: AnnotatedString,
    sampleStart: AnnotatedString,
    modifier: Modifier = Modifier,
) {
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
                            if (it == SampleAlgorithm) sampleEnd.toChars() else sampleStart.toChars()
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

    Row(modifier = modifier) {
        Vertical(MaterialTheme.colors.secondary) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Before(
                    Modifier.weight(1f).sharedElement(
                        rememberSharedContentState("before"),
                        boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                    )
                )
                After(
                    Modifier.weight(1f).sharedElement(
                        rememberSharedContentState("after"),
                        boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                    )
                )
            }
        }
        Vertical(MaterialTheme.colors.primary) {
            // !!!
            // Fake vertical to make sure the shared element
            // doesn't change bounds with the previous scene element.
            // !!!
        }
    }
}

fun AnnotatedString.toChars(): List<AnnotatedString> {
    return buildList {
        for (i in 0..<length) {
            add(subSequence(i, i + 1))
        }
    }
}
