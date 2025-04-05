package dev.bnorm.kc25.story.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import dev.bnorm.kc25.broadcast.Broadcast
import dev.bnorm.kc25.broadcast.Broadcaster
import dev.bnorm.kc25.broadcast.OverlayBroadcasting
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.ExperimentalStoryStateApi
import dev.bnorm.storyboard.easel.rememberStoryState
import dev.bnorm.storyboard.easel.DesktopStory
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import org.jetbrains.compose.reload.DevelopmentEntryPoint

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    application {
        val state = rememberStoryState()
        var broadcaster by remember { mutableStateOf<Broadcaster?>(null) }
        DevelopmentEntryPoint {
            remember { createStoryboard().also { state.updateStoryboard(it) } }
            Broadcast(state, broadcaster) // Important to be after updating state with storyboard.

            MaterialTheme(colors = darkColors()) {
                DesktopStory(
                    storyState = state,
                    overlay = {
                        OverlayNavigation(state)
                        OverlayBroadcasting(
                            enabled = broadcaster != null,
                            onClick = {
                                broadcaster = if (broadcaster == null) Broadcaster() else null
                            },
                        )
                    }
                )
            }
        }
    }
}
