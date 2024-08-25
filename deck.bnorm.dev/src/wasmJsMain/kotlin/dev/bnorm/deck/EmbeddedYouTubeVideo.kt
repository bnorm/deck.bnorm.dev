package dev.bnorm.deck

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.bnorm.deck.html.HtmlView
import dev.bnorm.deck.youtube.Options
import dev.bnorm.deck.youtube.PlayerFactory
import dev.bnorm.deck.youtube.PlayerVars
import dev.bnorm.deck.youtube.YouTubePlayer
import org.w3c.dom.HTMLElement

@Composable
fun EmbeddedYouTubeVideo(
    videoId: String,
    onReady: suspend (YouTubePlayer) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var player by remember { mutableStateOf<YouTubePlayer?>(null) }

    HtmlView(
        modifier = modifier,
        factory = {
            createElement("iframe").apply {
                setAttribute("src", "https://www.youtube.com/embed/$videoId")
                setAttribute("title", "YouTube video player")
                setAttribute("frameborder", "0")
                setAttribute("referrerpolicy", "strict-origin-when-cross-origin")
                setAttribute("allowfullscreen", "")

                player = PlayerFactory(
                    maybeElementId = this as HTMLElement,
                    options = Options {
                        this.videoId = videoId.toJsString()
                        playerVars = PlayerVars {
                            fs = 0.toJsNumber()
                        }
                    }
                )
            }
        }
    )

    LaunchedEffect(player, onReady) {
        onReady(player ?: return@LaunchedEffect)
    }
}
