package dev.bnorm.deck.youtube

import kotlin.js.Promise

external interface YouTubePlayer : JsAny {
    fun getCurrentTime(): Promise<JsNumber>
}
