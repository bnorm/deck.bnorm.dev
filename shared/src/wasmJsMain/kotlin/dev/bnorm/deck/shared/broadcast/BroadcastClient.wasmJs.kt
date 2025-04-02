package dev.bnorm.deck.shared.broadcast

import kotlinx.serialization.KSerializer

actual fun <T> BroadcastClient(serializer: KSerializer<T>): BroadcastClient<T> {
    return BroadcastClient(bearerToken = null, serializer)
}
