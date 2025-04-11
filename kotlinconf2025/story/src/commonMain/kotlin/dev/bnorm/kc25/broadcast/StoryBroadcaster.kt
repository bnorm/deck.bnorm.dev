package dev.bnorm.kc25.broadcast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.storyboard.easel.StoryState
import io.ktor.http.*

class StoryBroadcaster {
    companion object {
        private const val CHANNEL_ID = "story-kc25"
    }

    private val client = BroadcastClient(BroadcastMessage.serializer())
    private var enabled = true

    suspend fun broadcast(message: BroadcastMessage) {
        if (!enabled) return

        val response = client.broadcast(CHANNEL_ID, message)
        if (response.status != HttpStatusCode.OK && response.status != HttpStatusCode.Created) {
            // An unsuccessful response should disable the broadcaster.
            enabled = false
        }
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
