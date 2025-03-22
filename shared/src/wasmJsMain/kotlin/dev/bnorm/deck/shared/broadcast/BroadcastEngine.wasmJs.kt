package dev.bnorm.deck.shared.broadcast

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

internal actual val BROADCAST_ENGINE: HttpClientEngine = Js.create {
}
