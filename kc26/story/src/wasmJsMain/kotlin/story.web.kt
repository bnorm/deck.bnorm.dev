package dev.bnorom.kc26.story.web

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.bnorm.kc26.createStoryboard
import dev.bnorm.storyboard.easel.WebEasel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport("composeApp") {
        MaterialTheme(colors = darkColors()) {
            WebEasel { createStoryboard() }
        }
    }
}
