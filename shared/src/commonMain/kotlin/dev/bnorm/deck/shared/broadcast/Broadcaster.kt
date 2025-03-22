package dev.bnorm.deck.shared.broadcast

import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

expect fun Broadcaster(channelId: String): Broadcaster?

class Broadcaster(
    private val bearerToken: String,
    private val channelId: String,
) {
    private val json = DefaultJson
    private val client = HttpClient(BROADCAST_ENGINE) {
        install(SSE)

        install(ContentNegotiation) {
            json(json)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(bearerToken, refreshToken = null)
                }
            }
        }
    }

    suspend fun broadcast(message: BroadcastMessage) {
        client.post("https://broadcast.bnorm.dev/channels/$channelId") {
            contentType(ContentType.Application.Json)
            setBody(message)
        }
    }
}
