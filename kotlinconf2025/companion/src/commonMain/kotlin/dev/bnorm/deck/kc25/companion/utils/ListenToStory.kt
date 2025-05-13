package dev.bnorm.deck.kc25.companion.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import dev.bnorm.deck.kc25.companion.toStoryboard
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.kc25.broadcast.BroadcastMessage
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.easel.StoryState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun ListenToStory(storyState: StoryState) {
    // TODO allow detaching from active listening when navigating backwards in the story
    val broadcastListener = remember { BroadcastClient(bearerToken = null, BroadcastMessage.serializer()) }

    LaunchedEffect(Unit) {
        while (true) {
            try {
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
                }
            } catch (e: Throwable) {
                if (e is CancellationException) throw e
            }
            delay(30.seconds)
        }
    }
}
