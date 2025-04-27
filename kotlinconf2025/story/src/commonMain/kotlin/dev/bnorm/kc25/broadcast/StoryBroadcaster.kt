package dev.bnorm.kc25.broadcast

import dev.bnorm.deck.shared.broadcast.BroadcastClient

class StoryBroadcaster {
    companion object {
        private const val CHANNEL_ID = "story-kc25"
    }

    private val client = BroadcastClient(BroadcastMessage.serializer())

    suspend fun broadcast(message: BroadcastMessage) {
        client.broadcast(CHANNEL_ID, message)
    }
}

