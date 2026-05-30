package dev.bnorm.deck.youtube

import org.w3c.dom.HTMLElement

@JsModule("youtube-player")
@JsNonModule
external fun PlayerFactory(
    maybeElementId: HTMLElement,
    options: Options = definedExternally,
    strictState: Boolean = definedExternally,
): YouTubePlayer

@JsModule("youtube-player")
@JsNonModule
external fun PlayerFactory(
    maybeElementId: String,
    options: Options = definedExternally,
    strictState: Boolean = definedExternally,
): YouTubePlayer
