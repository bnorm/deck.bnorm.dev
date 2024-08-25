package dev.bnorm.deck.youtube

fun Options(): Options = js("({})")
fun Options(block: Options.() -> Unit): Options = Options().apply(block)

external interface Options : JsAny {
    var width: JsAny? /* JsNumber? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var height: JsAny? /* JsNumber? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var videoId: JsString?
        get() = definedExternally
        set(value) = definedExternally
    var host: JsString?
        get() = definedExternally
        set(value) = definedExternally
    var playerVars: PlayerVars?
        get() = definedExternally
        set(value) = definedExternally
    var events: JsAny?
        get() = definedExternally
        set(value) = definedExternally
}
