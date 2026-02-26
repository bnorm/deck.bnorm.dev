package dev.bnorom.dcon25.story.web

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.bnorm.dcnyc25.createStoryboard
import dev.bnorm.storyboard.easel.WebEasel
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val storyboard = createStoryboard()
    val element = document.getElementById("ComposeTarget") as HTMLCanvasElement
    element.focus()
    ComposeViewport(viewportContainer = element) {
        MaterialTheme(colors = darkColors()) {
            WebEasel { storyboard }
        }
    }
}
