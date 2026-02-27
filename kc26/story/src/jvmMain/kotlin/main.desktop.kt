package dev.bnorm.kc26.story.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.application
import dev.bnorm.kc26.createStoryboard
import dev.bnorm.storyboard.easel.DesktopEasel

fun main() {
    application {
        MaterialTheme(colors = darkColors()) {
            DesktopEasel { createStoryboard() }
        }
    }
}
