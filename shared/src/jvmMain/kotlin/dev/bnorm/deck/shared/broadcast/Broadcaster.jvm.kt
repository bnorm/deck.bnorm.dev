package dev.bnorm.deck.shared.broadcast

actual fun Broadcaster(channelId: String): Broadcaster? {
    val accessToken = System.getenv("BROADCAST_TOKEN") ?: return null
    return Broadcaster(accessToken, channelId)
}
