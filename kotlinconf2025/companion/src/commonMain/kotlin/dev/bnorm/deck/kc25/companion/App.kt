package dev.bnorm.deck.kc25.companion

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.kc25.companion.cards.EmbeddedStory
import dev.bnorm.deck.kc25.companion.cards.SendAReaction
import dev.bnorm.deck.kc25.companion.cards.Title
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
    MaterialTheme(colors = lightColors()) {
        val state = rememberLazyListState()
        val maxIndex = rememberMaxIndex(storyState)

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
                .scrollable(state, orientation = Orientation.Vertical, reverseDirection = true)
        ) {
            LazyColumn(
                state = state,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    // TODO can i make the content scrollable once smaller than min width?
                    .widthIn(max = 960.dp)
                    .requiredWidthIn(min = 720.dp)
            ) {
                Content(maxIndex, storyState)
            }
        }
    }
}

private fun LazyListScope.Content(
    latest: State<Storyboard.Index>,
    storyState: StoryState,
) {
    // TODO make some of these sticky?
    item("title") {
        ContentCard(Modifier.animateItem()) {
            Title()
        }
    }

    item("story") {
        ContentCard(Modifier.animateItem()) {
            EmbeddedStory(storyState)
        }
    }

    item("Reactions") {
        ContentCard(Modifier.animateItem()) {
            SendAReaction()
        }
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

    repeat(latest.value.sceneIndex) {
        item(it) {
            ContentCard(Modifier.animateItem()) {
                Text("Information ${it + 1}", style = MaterialTheme.typography.h2)
            }
        }
    }
}
