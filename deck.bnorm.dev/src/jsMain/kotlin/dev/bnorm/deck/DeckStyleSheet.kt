package dev.bnorm.deck

import org.jetbrains.compose.web.css.*

object DeckStyleSheet : StyleSheet() {
    init {
        "body" style {
            backgroundColor(Color("#121212"))

            color(Color.white)
            letterSpacing("normal")
            fontFamily("Inter", "sans-serif")
            property("font-optical-sizing", "auto")
        }

        ".talk-card" style {
            boxSizing("border-box")
            padding(32.px)
            borderRadius(16.px)
            backgroundColor(Color("#323236"))
        }

        ".talk-title" style {
            fontSize(48.px)
            fontWeight(400)
        }

        ".talk-subtitle" style {
            fontSize(32.px)
            fontWeight(200)
            fontStyle("italic")
        }

        ".talk-video" style {
            width(560.px)
            height(315.px)
        }

        ".talk-deck" style {
            width(560.px)
            height(315.px)
        }

        universal style {
            margin(0.px)
        }
    }
}