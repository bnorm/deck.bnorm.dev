package dev.bnorm.deck.shared.broadcast

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

actual val BROADCAST_ENGINE: HttpClientEngine = CIO.create {
}
