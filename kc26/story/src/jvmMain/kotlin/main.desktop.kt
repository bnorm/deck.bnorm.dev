package dev.bnorm.kc26.story.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.application
import dev.bnorm.kc26.createStoryboard
import dev.bnorm.storyboard.easel.DesktopEasel
import dev.bnorm.storyboard.easel.rememberAnimatic

fun main() {
    application {
        val animatic = rememberAnimatic("animatic") { createStoryboard() }
        MaterialTheme(colors = darkColors()) {
            DesktopEasel(animatic)
        }
    }
}
