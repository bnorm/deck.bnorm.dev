package dev.bnorm.deck

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.KotlinPlusPowerAssertEqualsLove
import dev.bnorm.storyboard.easel.EmbeddedStoryboard

@Composable
fun Deck() {
    Column {
        Row {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Slide Show")
                Box(Modifier.requiredSize(width = 560.dp, height = 315.dp)) {
                    EmbeddedStoryboard(KotlinPlusPowerAssertEqualsLove)
                }
            }

            Spacer(Modifier.size(32.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("YouTube Recording")
                EmbeddedYouTubeVideo(videoId = "N8u-6d0iCiE", modifier = Modifier.size(width = 560.dp, height = 315.dp))
            }
        }
    }
}
