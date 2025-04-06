package dev.bnorm.deck.youtube

fun PlayerVars(): PlayerVars = js("({})")
fun PlayerVars(block: PlayerVars.() -> Unit): PlayerVars = PlayerVars().apply(block)

external interface PlayerVars {
    var autoplay: Number? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var cc_lang_pref: String?
        get() = definedExternally
        set(value) = definedExternally
    var cc_load_policy: Number? /* 1 */
        get() = definedExternally
        set(value) = definedExternally
    var color: String? /* "red" | "white" */
        get() = definedExternally
        set(value) = definedExternally
    var controls: Number? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var disablekb: Number? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var enablejsapi: Number? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var end: Number?
        get() = definedExternally
        set(value) = definedExternally
    var fs: Number? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var hl: String?
        get() = definedExternally
        set(value) = definedExternally
    var iv_load_policy: Number? /* 1 | 3 */
        get() = definedExternally
        set(value) = definedExternally
    var list: String?
        get() = definedExternally
        set(value) = definedExternally
    var listType: String? /* "playlist" | "search" | "user_uploads" */
        get() = definedExternally
        set(value) = definedExternally
    var loop: Number? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var modestbranding: Number? /* 1 */
        get() = definedExternally
        set(value) = definedExternally
    var origin: String?
        get() = definedExternally
        set(value) = definedExternally
    var playlist: String?
        get() = definedExternally
        set(value) = definedExternally
    var playsinline: Number? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var rel: Number? /* 0 | 1 */
        get() = definedExternally
        set(value) = definedExternally
    var start: Number?
        get() = definedExternally
        set(value) = definedExternally
    var widget_referrer: String?
        get() = definedExternally
        set(value) = definedExternally
}
