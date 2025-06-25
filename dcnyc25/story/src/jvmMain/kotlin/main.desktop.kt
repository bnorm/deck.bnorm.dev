package dev.bnorm.dcnyc25.story.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import dev.bnorm.dcnyc25.createStoryboard
import dev.bnorm.storyboard.easel.DesktopStoryEasel
import dev.bnorm.storyboard.easel.ExperimentalStoryStateApi
import dev.bnorm.storyboard.easel.StoryState

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    val state = StoryState()

    application {
        remember {
            createStoryboard(
                includeTextFieldSamples = true,
            ).also { state.updateStoryboard(it) }
        }

        MaterialTheme(colors = darkColors()) {
            DesktopStoryEasel(
                storyState = state,
                overlay = {},
            )
        }
    }
}
