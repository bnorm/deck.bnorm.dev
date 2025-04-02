package dev.bnorm.deck.shared.broadcast

import kotlinx.serialization.KSerializer

actual fun <T> BroadcastClient(serializer: KSerializer<T>): BroadcastClient<T> {
    val bearerToken = System.getenv("BROADCAST_TOKEN")
    return BroadcastClient(bearerToken, serializer)
}
