package dev.bnorm.deck

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.KotlinPlusPowerAssertEqualsLove
import dev.bnorm.kc24.Theme
import dev.bnorm.librettist.EmbeddedSlideShow
import dev.bnorm.librettist.show.ShowBuilder

@Composable
fun Deck() {
    Column {
        Row {
            Column {
                Text("Slide Show")
                Box(Modifier.requiredSize(width = 560.dp, height = 315.dp)) {
                    EmbeddedSlideShow(
                        theme = Theme.dark,
                        builder = ShowBuilder::KotlinPlusPowerAssertEqualsLove
                    )
                }
            }

            Spacer(Modifier.size(32.dp))

            Column {
                Text("YouTube Recording")
                EmbeddedYouTubeVideo(videoId = "N8u-6d0iCiE", modifier = Modifier.size(width = 560.dp, height = 315.dp))
            }
        }
    }
}
