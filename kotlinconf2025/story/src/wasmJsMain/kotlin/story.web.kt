package dev.bnorom.kc25.story.web

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.easel.WebStoryEasel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val samples = mutableListOf<CodeSample>()
    val storyboard = createStoryboard(sink = samples)
    CanvasBasedWindow(canvasElementId = "ComposeTarget", title = storyboard.title) {
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                for (sample in samples) {
                    sample.string // Initialize the string to ensure it is loaded.
                    delay(100) // TODO is this the best delay?
                }
            }
        }

        MaterialTheme(colors = darkColors()) {
            WebStoryEasel(storyboard = storyboard)
        }
    }
}
