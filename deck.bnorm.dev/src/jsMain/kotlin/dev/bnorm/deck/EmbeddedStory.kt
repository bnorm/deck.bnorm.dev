package dev.bnorm.deck

import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Iframe
import org.jetbrains.compose.web.dom.Text

@Composable
fun EmbeddedStory(storyId: String) {
    var loaded by remember { mutableStateOf(false) }
    if (loaded) {
        Iframe({
            classes("talk-deck")
            attr("src", "https://deck.bnorm.dev/$storyId")
            attr("frameborder", "0")
        })
    } else {
        Div({
            classes("talk-deck")
            onClick {
                loaded = true
            }
            style {
                display(DisplayStyle.Grid)
                property("place-items", "center")
                cursor("pointer")
                border(width = 2.px, style = LineStyle.Solid, color = Color.white)
            }
        }) {
            Text("Click to load slides")
        }
    }
}
