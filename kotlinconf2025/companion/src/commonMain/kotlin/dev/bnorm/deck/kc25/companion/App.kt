package dev.bnorm.deck.kc25.companion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.kc25.companion.cards.CompilerPlugins
import dev.bnorm.deck.kc25.companion.cards.EmbeddedStory
import dev.bnorm.deck.kc25.companion.cards.Resources
import dev.bnorm.deck.kc25.companion.cards.SendAReaction
import dev.bnorm.deck.kc25.companion.cards.Stages
import dev.bnorm.deck.kc25.companion.cards.Title
import dev.bnorm.deck.kc25.companion.utils.ListenToStory
import dev.bnorm.deck.kc25.companion.utils.rememberMaxIndex
import dev.bnorm.kc25.broadcast.BroadcastMessage
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.StoryState

fun BroadcastMessage.toStoryboard(): Storyboard.Index {
    return Storyboard.Index(sceneIndex, stateIndex)
}

// TODO doesn't seem to work on iOS Chrome: https://developer.chrome.com/blog/debugging-chrome-on-ios
// TODO the app seems to become non-responsive on Android Chrome after the phone goes to sleep
@Composable
fun App(storyState: StoryState) {
    ListenToStory(storyState)

    // TODO switch between light and dark themes?
    MaterialTheme(colors = lightColors(background = Color.LightGray)) {
        val state = rememberScrollState()

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colors.background)
                .scrollable(state, orientation = Orientation.Vertical, reverseDirection = true)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(state)
                    .padding(16.dp)
                    .fillMaxHeight()
                    // TODO can i make the content scrollable once smaller than min width?
                    .widthIn(max = 960.dp)
                    .requiredWidthIn(min = 720.dp)
            ) {
                Content(storyState)
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(storyState: StoryState) {
    ContentCard {
        Title()
    }

    ContentCard(Modifier.padding(top = 16.dp)) {
        EmbeddedStory(storyState)
    }

    ContentCard(Modifier.padding(top = 16.dp)) {
        SendAReaction()
    }

    // TODO ideas
    //  * [ ] card with links to previous compiler plugin talks and blog post
    //  * [ ] card with links to FIR documentation:
    //    - https://github.com/JetBrains/kotlin/blob/v2.2.0-Beta1/compiler/fir/checkers/module.md
    //    - https://github.com/JetBrains/kotlin/blob/v2.2.0-Beta1/docs/fir/fir-basics.md
    //    - https://github.com/JetBrains/kotlin/blob/v2.2.0-Beta1/docs/fir/fir-plugins.md
    //    - https://github.com/JetBrains/kotlin/blob/v2.2.0-Beta1/docs/fir/k2_kmp.md
    //  * [ ] card with link to buildable sample compiler plugin
    //  * [ ] card with link to compiler plugin template
    //  * [ ] card with link to KT-49508: Stabilize the K2 Compiler Plugin API
    //  * [ ] card with link to KTIJ-29248: K2 IDE: Enable non-bundled compiler plugins in IDE by default
    //  * [ ] card with link to test documentation:
    //    - https://github.com/JetBrains/kotlin/blob/v2.2.0-Beta1/compiler/test-infrastructure/ReadMe.md

    val maxIndex by rememberMaxIndex(storyState)
//    val maxIndex = storyState.targetIndex

    ContentCard(Modifier.padding(top = 16.dp)) {
        Text(maxIndex.toString())
    }

    val cards = remember {
        mapOf(
            Storyboard.Index(2, 0) to Resources,
            Storyboard.Index(4, 0) to CompilerPlugins,
            Storyboard.Index(12, 0) to Stages,
        )
    }

    for ((index, card) in cards.entries.sortedBy { it.key }) {
        AnimatedVisibility(maxIndex >= index) {
            ContentCard(Modifier.padding(top = 16.dp)) {
                card.content(maxIndex)
            }
        }
    }
    repeat(50) {
        AnimatedVisibility(maxIndex.sceneIndex >= it) {
            ContentCard(Modifier.padding(top = 16.dp)) {
                Text("Information ${it + 1}", style = MaterialTheme.typography.h2)
            }
        }
    }
}
