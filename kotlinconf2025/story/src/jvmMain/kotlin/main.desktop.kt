package dev.bnorm.kc25.story.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.application
import dev.bnorm.deck.shared.broadcast.Broadcaster
import dev.bnorm.deck.shared.broadcast.LocalBroadcaster
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.DesktopStoryboard
import org.jetbrains.compose.reload.DevelopmentEntryPoint

fun main() {
    application {
        val storyboard = createStoryboard()
        DevelopmentEntryPoint {
            CompositionLocalProvider(LocalBroadcaster provides Broadcaster("story-kc25")) {
                MaterialTheme(colors = darkColors()) {
                    DesktopStoryboard(storyboard = storyboard)
                }
            }
        }
    }
}
