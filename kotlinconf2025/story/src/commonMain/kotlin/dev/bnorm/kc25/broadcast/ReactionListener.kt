package dev.bnorm.kc25.broadcast

import androidx.compose.runtime.staticCompositionLocalOf
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ReactionListener {
    companion object {
        private const val CHANNEL_ID = "story-kc25-react"
    }

    private val client = BroadcastClient(ReactionMessage.serializer())
    private var enabled = true

    fun listen(): Flow<ReactionMessage> = flow {
        while (enabled) {
            emitAll(client.subscribe(CHANNEL_ID))
            // TODO delay?
            // TODO error response?

            // Open the channel if it isn't already open
            val response = client.broadcast(CHANNEL_ID, ReactionMessage.Ping(getTimeMillis()), public = true)
            if (response.status != HttpStatusCode.Companion.OK && response.status != HttpStatusCode.Companion.Created) {
                // An unsuccessful response should disable the broadcaster.
                enabled = false
            }
        }
    }
}

val LocalReactionListener = staticCompositionLocalOf<ReactionListener?> { null }
