package dev.bnorm.kc25.story.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import dev.bnorm.kc25.broadcast.Broadcaster
import dev.bnorm.kc25.broadcast.LocalBroadcaster
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.core.ExperimentalStoryStateApi
import dev.bnorm.storyboard.core.rememberStoryState
import dev.bnorm.storyboard.easel.DesktopStory
import org.jetbrains.compose.reload.DevelopmentEntryPoint

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    application {
        val state = rememberStoryState()
        DevelopmentEntryPoint {
            remember { createStoryboard().also { state.updateStoryboard(it) } }
            MaterialTheme(colors = darkColors()) {
                CompositionLocalProvider(LocalBroadcaster provides Broadcaster()) {
                    DesktopStory(state)
                }
            }
        }
    }
}
