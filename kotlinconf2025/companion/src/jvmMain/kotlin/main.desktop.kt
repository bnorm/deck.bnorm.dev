import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.bnorm.deck.kc25.companion.App
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.rememberStoryState

fun main() {
    application {
        val storyState = rememberStoryState(remember { createStoryboard() })
        Window(onCloseRequest = ::exitApplication, title = "Companion") {
            App(storyState)
        }
    }
}
