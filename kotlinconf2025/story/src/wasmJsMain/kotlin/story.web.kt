package dev.bnorom.kc25.story.web

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.easel.WebEasel
import kotlinx.browser.document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.w3c.dom.HTMLCanvasElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val samples = mutableListOf<CodeSample>()
    val storyboard = createStoryboard(sink = samples)
    val element = document.getElementById("ComposeTarget") as HTMLCanvasElement
    element.focus()
    ComposeViewport(viewportContainer = element) {
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                for (sample in samples) {
                    sample.string // Initialize the string to ensure it is loaded.
                    delay(100) // TODO is this the best delay?
                }
            }
        }

        MaterialTheme(colors = darkColors()) {
            WebEasel { storyboard }
        }
    }
}
