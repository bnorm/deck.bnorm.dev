package dev.bnorm.deck.youtube

import kotlin.js.Promise

external interface YouTubePlayer {
    fun getCurrentTime(): Promise<Number>
}
