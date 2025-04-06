package dev.bnorm.deck.youtube

fun Options(): Options = js("({})")
fun Options(block: Options.() -> Unit): Options = Options().apply(block)

external interface Options {
    var width: Any? /* JsNumber? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var height: Any? /* JsNumber? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var videoId: String?
        get() = definedExternally
        set(value) = definedExternally
    var host: String?
        get() = definedExternally
        set(value) = definedExternally
    var playerVars: PlayerVars?
        get() = definedExternally
        set(value) = definedExternally
    var events: Any?
        get() = definedExternally
        set(value) = definedExternally
}
