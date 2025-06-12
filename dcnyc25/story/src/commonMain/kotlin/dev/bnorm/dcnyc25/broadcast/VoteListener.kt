package dev.bnorm.dcnyc25.broadcast

import dev.bnorm.deck.shared.broadcast.BroadcastClient
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class VoteListener {
    companion object {
        private const val CHANNEL_ID = "story-dcnyc25-vote"
    }

    private val client = BroadcastClient(VoteMessage.serializer())
    private var enabled = true

    fun listen(): Flow<VoteMessage> = flow {
        while (enabled) {
            emitAll(client.subscribe(CHANNEL_ID))
            // TODO delay?
            // TODO error response?

            // Open the channel if it isn't already open
            val response = client.broadcast(CHANNEL_ID, VoteMessage.Ping(userId = "speaker"), public = true)
            if (!response.status.isSuccess()) {
                // An unsuccessful response should disable the broadcaster.
                enabled = false
            }
        }
    }
}
