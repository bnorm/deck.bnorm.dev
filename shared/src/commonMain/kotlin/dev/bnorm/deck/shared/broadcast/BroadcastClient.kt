package dev.bnorm.deck.shared.broadcast

import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.KSerializer

expect fun <T> BroadcastClient(serializer: KSerializer<T>): BroadcastClient<T>

class BroadcastClient<T>(
    bearerToken: String?,
    private val serializer: KSerializer<T>,
) {
    private val json = DefaultJson

    private val client = HttpClient(BROADCAST_ENGINE) {
        install(SSE)

        if (bearerToken != null) {
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(bearerToken, refreshToken = null)
                    }
                }
            }
        }
    }

    fun subscribe(channelId: String): Flow<T> = channelFlow {
        client.sse(
            "https://broadcast.bnorm.dev/channels/$channelId/subscribe",
            showRetryEvents = true,
            showCommentEvents = true,
        ) {
            incoming.collect {
                val data = it.data
                if (data != null) {
                    channel.send(json.decodeFromString(serializer, data))
                }
            }
        }
    }

    suspend fun broadcast(channelId: String, message: T, public: Boolean = false): HttpResponse {
        return client.post("https://broadcast.bnorm.dev/channels/$channelId") {
            contentType(ContentType.Application.Json)
            setBody(json.encodeToString(serializer, message))
            parameter("public", public)
        }
    }
}