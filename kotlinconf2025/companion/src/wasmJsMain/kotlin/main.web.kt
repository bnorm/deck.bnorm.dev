import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.deck.kc25.companion.App
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.rememberStoryState

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val storyboard = createStoryboard()
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        val storyState = rememberStoryState(storyboard)
        App(storyState)
    }
}
