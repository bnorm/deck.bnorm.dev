package dev.bnorm.kc25.broadcast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.storyboard.easel.StoryState

class StoryBroadcaster {
    companion object {
        private const val CHANNEL_ID = "story-kc25"
    }

    private val client = BroadcastClient(BroadcastMessage.serializer())

    suspend fun broadcast(message: BroadcastMessage) {
        client.broadcast(CHANNEL_ID, message)
    }
}

@Composable
fun Broadcast(storyState: StoryState, storyBroadcaster: StoryBroadcaster?) {
    if (storyBroadcaster == null) return

    val frame = storyState.currentIndex
    LaunchedEffect(frame) {
        storyBroadcaster.broadcast(
            BroadcastMessage(
                sceneIndex = frame.sceneIndex,
                stateIndex = frame.stateIndex,
            )
        )
    }
}
