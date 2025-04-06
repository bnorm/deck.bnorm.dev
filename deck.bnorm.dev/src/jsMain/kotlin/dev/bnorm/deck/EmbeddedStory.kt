package dev.bnorm.deck

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Iframe

@Composable
fun EmbeddedStory(storyId: String) {
    Iframe({
        attr("src", "https://deck.bnorm.dev/$storyId")
        attr("frameborder", "0")
        style {
            width(560.px)
            height(315.px)
        }
    })
}
