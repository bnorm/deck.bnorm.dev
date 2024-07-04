package dev.bnorm.deck

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bnorm.deck.html.HtmlView

@Composable
fun EmbeddedYouTubeVideo(videoId: String, modifier: Modifier = Modifier) {
    HtmlView(
        modifier = modifier,
        factory = {
            createElement("iframe").apply {
                setAttribute("src", "https://www.youtube.com/embed/$videoId")
                setAttribute("title", "YouTube video player")
                setAttribute("frameborder", "0")
                setAttribute("referrerpolicy", "strict-origin-when-cross-origin")
                setAttribute("allowfullscreen", "")
            }
        }
    )
}
