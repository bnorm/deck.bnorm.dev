package dev.bnorm.deck

import androidx.compose.runtime.*
import dev.bnorm.deck.youtube.Options
import dev.bnorm.deck.youtube.PlayerFactory
import dev.bnorm.deck.youtube.PlayerVars
import dev.bnorm.deck.youtube.YouTubePlayer
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Iframe

@Composable
fun EmbeddedYouTubeVideo(
    videoId: String,
    onReady: suspend (YouTubePlayer) -> Unit = {},
) {
    var player by remember { mutableStateOf<YouTubePlayer?>(null) }

    Iframe({
        attr("src", "https://www.youtube.com/embed/$videoId")
        attr("title", "YouTube video player")
        attr("frameborder", "0")
        attr("referrerpolicy", "strict-origin-when-cross-origin")
        attr("allowfullscreen", "")
        style {
            width(560.px)
            height(315.px)
            marginRight(32.px)
        }
    }) {
        DisposableEffect(Unit) {
            player = PlayerFactory(
                maybeElementId = scopeElement,
                options = Options {
                    this.videoId = videoId
                    playerVars = PlayerVars {
                        fs = 0
                    }
                }
            )
            onDispose { player = null }
        }
    }

    LaunchedEffect(player, onReady) {
        onReady(player ?: return@LaunchedEffect)
    }
}
