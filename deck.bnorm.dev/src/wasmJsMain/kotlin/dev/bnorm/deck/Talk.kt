package dev.bnorm.deck

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.rememberStoryState
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlay
import dev.bnorm.storyboard.ui.StoryScene

@Composable
fun Talk(title: @Composable () -> Unit, videoId: String, storyboard: Storyboard? = null) {
    val state = storyboard?.let { rememberStoryState(it) }

    Surface(elevation = 8.dp, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(32.dp), modifier = Modifier.padding(32.dp)) {
            Box(Modifier.align(Alignment.CenterHorizontally)) {
                ProvideTextStyle(MaterialTheme.typography.h3) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        title()
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                // TODO update storyboard to the desired scene?
                EmbeddedYouTubeVideo(
                    videoId = videoId,
                    modifier = Modifier.requiredSize(width = 560.dp, height = 315.dp),
                )
                if (state != null) {
                    Box(Modifier.requiredSize(width = 560.dp, height = 315.dp)) {
                        StoryOverlay(overlay = { OverlayNavigation(state) }) {
                            StoryScene(state)
                        }
                    }
                }
            }
        }
    }
}
