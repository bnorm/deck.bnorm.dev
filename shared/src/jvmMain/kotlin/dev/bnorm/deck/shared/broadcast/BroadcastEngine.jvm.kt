package dev.bnorm.deck.shared.broadcast

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import okhttp3.OkHttpClient
import java.time.Duration

actual val BROADCAST_ENGINE: HttpClientEngine = OkHttp.create {
    preconfigured = OkHttpClient.Builder().apply {
        readTimeout(Duration.ofMinutes(1))
    }.build()
}
