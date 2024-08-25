package dev.bnorm.deck.youtube

fun PlayerVars(): PlayerVars = js("({})")
fun PlayerVars(block: PlayerVars.() -> Unit): PlayerVars = PlayerVars().apply(block)

external interface PlayerVars : JsAny {
    var autoplay: JsNumber? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var cc_lang_pref: JsString?
        get() = definedExternally
        set(value) = definedExternally
    var cc_load_policy: JsNumber? /* 1 */
        get() = definedExternally
        set(value) = definedExternally
    var color: JsString? /* "red" | "white" */
        get() = definedExternally
        set(value) = definedExternally
    var controls: JsNumber? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var disablekb: JsNumber? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var enablejsapi: JsNumber? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var end: JsNumber?
        get() = definedExternally
        set(value) = definedExternally
    var fs: JsNumber? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var hl: JsString?
        get() = definedExternally
        set(value) = definedExternally
    var iv_load_policy: JsNumber? /* 1 | 3 */
        get() = definedExternally
        set(value) = definedExternally
    var list: JsString?
        get() = definedExternally
        set(value) = definedExternally
    var listType: JsString? /* "playlist" | "search" | "user_uploads" */
        get() = definedExternally
        set(value) = definedExternally
    var loop: JsNumber? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var modestbranding: JsNumber? /* 1 */
        get() = definedExternally
        set(value) = definedExternally
    var origin: JsString?
        get() = definedExternally
        set(value) = definedExternally
    var playlist: JsString?
        get() = definedExternally
        set(value) = definedExternally
    var playsinline: JsNumber? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var rel: JsNumber? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var start: JsNumber?
        get() = definedExternally
        set(value) = definedExternally
    var widget_referrer: JsString?
        get() = definedExternally
        set(value) = definedExternally
}
