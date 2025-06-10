package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.old.kc24.animateList
import dev.bnorm.dcnyc25.old.kc24.startAnimation
import dev.bnorm.dcnyc25.old.kc24.thenLineEndDiff
import dev.bnorm.dcnyc25.template.code1
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.LineEndDiff() {
    scene(
        stateCount = 2,
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {
        val start = remember {
            """
                fun main() {
                  println("Hello, KotlinConf!")
                }
            """.trimIndent().highlight(INTELLIJ_LIGHT, language = Language.Kotlin)
        }

        val end = remember {
            """
                fun main() {
                  println("Hello, droidcon NYC!")
                }
            """.trimIndent().highlight(INTELLIJ_LIGHT, language = Language.Kotlin)
        }

        val animation = remember(start, end) {
            startAnimation(start)
                .thenLineEndDiff(end)
                .toList()
        }

        val text by transition.animateList(
            values = animation,
            transitionSpec = { typing(animation.size) }
        ) {
            if (it.toState() == 0) 0 else animation.lastIndex
        }

        Column {
            Row {
                Quarter(MaterialTheme.colors.primary) {
                }
                Quarter(MaterialTheme.colors.secondary) {
                }
            }
            Row {
                Quarter(MaterialTheme.colors.secondary) {
                    TextSurface {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.code1,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
                Quarter(MaterialTheme.colors.primary) {
                    TextSurface {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.code1,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}

private fun <T> typing(characters: Int): TweenSpec<T> =
    tween(durationMillis = 35 * characters, easing = LinearEasing)
