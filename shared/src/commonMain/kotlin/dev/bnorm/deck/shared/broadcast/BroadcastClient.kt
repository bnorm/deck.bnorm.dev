package dev.bnorm.deck.shared.broadcast

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

object BroadcastClient {
    private val json = DefaultJson

    // OkHttp has a 10-second timeout which can cause problems.
    // TODO is there a way to still use OkHttp and increase the timeout?
    private val client = HttpClient(BROADCAST_ENGINE) {
        install(SSE)

        install(ContentNegotiation) {
            json(json)
        }
    }

    fun subscribe(channelId: String): Flow<BroadcastMessage> = channelFlow {
        client.sse(
            "https://broadcast.bnorm.dev/channels/$channelId/subscribe",
            showRetryEvents = true,
            showCommentEvents = true,
        ) {
            incoming.collect {
                val data = it.data
                if (data != null) {
                    println(data)
                    channel.send(json.decodeFromString(BroadcastMessage.serializer(), data))
                }
            }
        }
    }
}