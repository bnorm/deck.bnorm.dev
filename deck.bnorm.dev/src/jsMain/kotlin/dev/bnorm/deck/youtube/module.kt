@file:JsModule("youtube-player")

package dev.bnorm.deck.youtube

import org.w3c.dom.HTMLElement

@JsName("default")
external fun PlayerFactory(
    maybeElementId: HTMLElement,
    options: Options = definedExternally,
    strictState: Boolean = definedExternally,
): YouTubePlayer

@JsName("default")
external fun PlayerFactory(maybeElementId: HTMLElement): YouTubePlayer

@JsName("default")
external fun PlayerFactory(maybeElementId: HTMLElement, options: Options = definedExternally): YouTubePlayer

@JsName("default")
external fun PlayerFactory(
    maybeElementId: String,
    options: Options = definedExternally,
    strictState: Boolean = definedExternally,
): YouTubePlayer

@JsName("default")
external fun PlayerFactory(maybeElementId: String): YouTubePlayer

@JsName("default")
external fun PlayerFactory(maybeElementId: String, options: Options = definedExternally): YouTubePlayer
