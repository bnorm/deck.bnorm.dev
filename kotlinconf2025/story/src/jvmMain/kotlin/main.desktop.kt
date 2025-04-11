package dev.bnorm.kc25.story.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.window.application
import dev.bnorm.kc25.broadcast.*
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.DesktopStoryEasel
import dev.bnorm.storyboard.easel.ExperimentalStoryStateApi
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import dev.bnorm.storyboard.easel.rememberStoryState
import org.jetbrains.compose.reload.DevelopmentEntryPoint

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    application {
        val state = rememberStoryState()
        var storyBroadcaster by remember { mutableStateOf<StoryBroadcaster?>(null) }
        var reactionListener by remember { mutableStateOf<ReactionListener?>(null) }

        @Composable
        fun StoryOverlayScope.DesktopOverlay() {
            OverlayNavigation(state)
            OverlayBroadcasting(
                enabled = storyBroadcaster != null,
                onClick = {
                    storyBroadcaster = if (storyBroadcaster == null) StoryBroadcaster() else null
                    reactionListener = if (reactionListener == null) ReactionListener() else null
                },
            )
        }

        DevelopmentEntryPoint {
            remember { createStoryboard().also { state.updateStoryboard(it) } }
            Broadcast(state, storyBroadcaster) // Important to be after updating state with storyboard.

            MaterialTheme(colors = darkColors()) {
                CompositionLocalProvider(LocalReactionListener provides reactionListener) {
                    DesktopStoryEasel(
                        storyState = state,
                        overlay = { DesktopOverlay() }
                    )
                }
            }
        }
    }
}
