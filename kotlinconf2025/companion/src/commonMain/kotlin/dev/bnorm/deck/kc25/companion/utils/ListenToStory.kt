package dev.bnorm.deck.kc25.companion.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import dev.bnorm.deck.kc25.companion.toStoryboard
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.kc25.broadcast.BroadcastMessage
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.easel.Animatic
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun ListenToStory(animatic: Animatic) {
    // TODO allow detaching from active listening when navigating backwards in the story
    val broadcastListener = remember { BroadcastClient(bearerToken = null, BroadcastMessage.serializer()) }

    LaunchedEffect(Unit) {
        while (true) {
            try {
                broadcastListener.subscribe("story-kc25").collect {
                    val index = it.toStoryboard()
                    val i = animatic.storyboard.indices.binarySearch(index)
                    if (i < 0) return@collect // TODO error?

                    val direction = when (index > animatic.currentIndex) {
                        true -> AdvanceDirection.Forward
                        false -> AdvanceDirection.Backward
                    }
                    val previous = when (direction) {
                        AdvanceDirection.Forward -> animatic.storyboard.indices.getOrNull(i - 1)
                        AdvanceDirection.Backward -> animatic.storyboard.indices.getOrNull(i + 1)
                    }

                    // Jump to the index directly to the target state if we cannot gracefully advance to it.
                    if (animatic.currentIndex != previous) {
                        animatic.jumpTo(index)
                    } else {
                        animatic.advance(direction)
                    }
                }
            } catch (e: Throwable) {
                if (e is CancellationException) throw e
            }
            delay(30.seconds)
        }
    }
}
