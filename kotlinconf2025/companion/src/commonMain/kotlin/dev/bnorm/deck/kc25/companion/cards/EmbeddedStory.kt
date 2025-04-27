package dev.bnorm.deck.kc25.companion.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.Story
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.overlay.StoryOverlay

@Composable
fun EmbeddedStory(storyState: StoryState) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Slides", style = MaterialTheme.typography.h2)
        Spacer(Modifier.height(16.dp))

        // TODO force render the storyboard in preview mode?
        //  - or at least while the navigation is synced?
        // TODO do we need to disable key events? doesn't seem like it...
        StoryOverlay(
            overlay = {
                // TODO enable navigation when the story is complete?
                //  - or maybe custom navigation that only allows navigating before latest
            }
        ) {
            Story(storyState)
        }
    }
}
