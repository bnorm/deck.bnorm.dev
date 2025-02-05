import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.kc25.DARK_COLORS
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.WebStoryboard

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val storyboard = createStoryboard(mutableStateOf(DARK_COLORS))
    CanvasBasedWindow(canvasElementId = "ComposeTarget", title = storyboard.title) {
        MaterialTheme(colors = darkColors()) {
            WebStoryboard(storyboard = storyboard)
        }
    }
}
