package dev.bnorm.deck

import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.px

object DeckStyleSheet : StyleSheet() {
    init {
        "body" style {
            backgroundColor(Color("#121212"))
        }

        universal style {
            margin(0.px)
        }
    }
}