package dev.bnorm.deck

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.StoryboardState
import dev.bnorm.storyboard.easel.EmbeddedStoryboard

@Composable
fun Talk(title: @Composable () -> Unit, videoId: String, storyboard: Storyboard? = null) {
    val state = remember(storyboard) { storyboard?.let { StoryboardState(it) } }

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
                // TODO update storyboard to the desired slide?
                EmbeddedYouTubeVideo(
                    videoId = videoId,
                    modifier = Modifier.requiredSize(width = 560.dp, height = 315.dp),
                )
                if (state != null) {
                    Box(Modifier.requiredSize(width = 560.dp, height = 315.dp)) {
                        EmbeddedStoryboard(state)
                    }
                }
            }
        }
    }
}
