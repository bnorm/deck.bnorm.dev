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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.kc25.companion.cards.EmbeddedStory
import dev.bnorm.deck.kc25.companion.cards.SendAReaction
import dev.bnorm.deck.kc25.companion.cards.Title
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.kc25.broadcast.BroadcastMessage
import dev.bnorm.kc25.broadcast.ReactionMessage
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.StoryState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

fun BroadcastMessage.toStoryboard(): Storyboard.Index {
    return Storyboard.Index(sceneIndex, stateIndex)
}

// TODO doesn't seem to work on iOS Chrome: https://developer.chrome.com/blog/debugging-chrome-on-ios
// TODO the app seems to become non-responsive on Android Chrome after the phone goes to sleep
@Composable
fun App(storyState: StoryState) {
    val broadcastListener = remember { BroadcastClient(bearerToken = null, BroadcastMessage.serializer()) }
    val broadcastReactor = remember { BroadcastClient(bearerToken = null, ReactionMessage.serializer()) }

    var targetIndex by remember { mutableStateOf<Storyboard.Index?>(null) }
    LaunchedEffect(Unit) {
        while (true) {
            broadcastListener.subscribe("story-kc25").collect {
                val index = it.toStoryboard()
                val i = storyState.storyboard.indices.binarySearch(index)
                if (i < 0) return@collect // TODO error?

                val direction = when (index > storyState.currentIndex) {
                    true -> AdvanceDirection.Forward
                    false -> AdvanceDirection.Backward
                }
                val previous = when (direction) {
                    AdvanceDirection.Forward -> storyState.storyboard.indices.getOrNull(i - 1)
                    AdvanceDirection.Backward -> storyState.storyboard.indices.getOrNull(i + 1)
                }

                // Jump to the index directly to the target state if we cannot gracefully advance to it.
                if (storyState.currentIndex != previous) {
                    storyState.jumpTo(index)
                } else {
                    storyState.advance(direction)
                }
                targetIndex = index
            }
            delay(30.seconds)
        }
    }

    MaterialTheme(colors = lightColors()) {
        val state = rememberLazyListState()
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
                Content(targetIndex, storyState, broadcastReactor)
            }
        }
    }
}

private fun LazyListScope.Content(
    latest: Storyboard.Index?,
    storyState: StoryState,
    broadcastReactor: BroadcastClient<ReactionMessage>,
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
            SendAReaction(broadcastReactor)
        }
    }

    repeat(latest?.sceneIndex ?: 0) {
        item(it) {
            ContentCard(Modifier.animateItem()) {
                Text("Information ${it + 1}", style = MaterialTheme.typography.h2)
            }
        }
    }
}
