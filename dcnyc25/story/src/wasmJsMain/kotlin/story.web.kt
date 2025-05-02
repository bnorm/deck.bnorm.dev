package dev.bnorom.dcon25.story.web

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.dcnyc25.createStoryboard
import dev.bnorm.storyboard.easel.WebStoryEasel
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val storyboard = createStoryboard()
    val element = document.getElementById("ComposeTarget") as HTMLCanvasElement
    element.focus()
    CanvasBasedWindow(canvasElementId = element.id, title = storyboard.title) {
        MaterialTheme(colors = darkColors()) {
            WebStoryEasel(storyboard)
        }
    }
}
