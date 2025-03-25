package dev.bnorm.evolved

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.evolved.sections.evolution.Evolution
import dev.bnorm.evolved.sections.future.Future
import dev.bnorm.evolved.sections.intro.Intro
import dev.bnorm.evolved.sections.today.Today
import dev.bnorm.evolved.template.THEME_DECORATOR
import dev.bnorm.storyboard.core.Storyboard

fun createStoryboard() = Storyboard.build(
    title = "Power-Assert: Evolved",
    description = """
        What?
        Kotlin Power-Assert is evolving!
        
        5 years ago, a simple question on the KotlinLang Slack launched a journey to bring power-assert to the Kotlin
        language. But now that power-assert is a native compiler-plugin for Kotlin, what does the next evolution look
        like? Join me to hear a brief history of where power-assert came from, how it works today, and what it may look
        like in the future!
    """.trimIndent(),
    size = Storyboard.DEFAULT_SIZE,
    decorator = THEME_DECORATOR,
) {
    // TODO go through and add kodee reactions?

    scene {
        Title {
            Text("Power-Assert:")
            Text("Evolved!")
        }
    }
    Intro()
    Today()
    Evolution()
    Future()
    scene {
        Title {
            Text("Thank you!")
        }
    }
}

@Composable
fun Title(content: @Composable () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Column(
            Modifier.fillMaxWidth().align(Alignment.BottomStart),
            horizontalAlignment = Alignment.Start
        ) {
            ProvideTextStyle(MaterialTheme.typography.h1) {
                content()
            }
            Spacer(Modifier.size(32.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    JetBrainsEmployee(
                        name = "Brian Norman",
                        title = "Kotlin Compiler Developer",
                    )
                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.align(Alignment.CenterEnd),
                ) {
                    Bluesky(username = "@bnorm.dev")
                    Spacer(Modifier.size(4.dp))
                    Mastodon(username = "bnorm@kotlin.social")
                }
            }
        }
    }
}
