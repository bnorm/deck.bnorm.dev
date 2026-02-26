package dev.bnorm.deck

import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Iframe

@Composable
fun EmbeddedVimeoVideo(
    videoId: String,
) {
    Iframe({
        classes("talk-video")
        attr("src", "https://player.vimeo.com/video/$videoId?autopause=0&amp;autoplay=0&amp;color=00adef&amp;portrait=0&amp;byline=0&amp;title=0")
        attr("title", "Vimeo video player")
        attr("webkitallowfullscreen", "")
        attr("mozallowfullscreen", "")
        attr("allowfullscreen", "")
        attr("data-vimeo-tracked", "true")
        attr("data-ready", "true")
    })
}
