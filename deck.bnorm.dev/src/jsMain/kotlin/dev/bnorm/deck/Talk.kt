package dev.bnorm.deck

import androidx.compose.runtime.Composable
import dev.bnorm.deck.layout.Column
import dev.bnorm.deck.layout.Row
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text

@Composable
fun Talk(title: String, videoId: String, storyId: String? = null) {
    Section({
        style {
            boxSizing("border-box")
            marginTop(32.px)
            marginLeft(32.px)
            marginRight(32.px)
            padding(32.px)
            borderRadius(8.px)
            backgroundColor(Color("#323236"))
        }
    }) {
        Column {
            Title(title)
            Row {
                EmbeddedYouTubeVideo(videoId)
                if (storyId != null) {
                    EmbeddedStory(storyId)
                }
            }
        }
    }
}

@Composable
fun Title(title: String) {
    P({
        style {
            color(Color.white)
            fontSize(48.px)
            letterSpacing("normal")
            fontWeight(400)
            marginBottom(32.px)

            property(
                "font-family",
                "system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
            )
        }
    }) {
        Text(title)
    }
}
