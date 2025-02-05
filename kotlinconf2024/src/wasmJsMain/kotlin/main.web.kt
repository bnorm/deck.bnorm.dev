import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.kc24.KotlinPlusPowerAssertEqualsLove
import dev.bnorm.storyboard.easel.WebStoryboard

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget", title = KotlinPlusPowerAssertEqualsLove.title) {
        MaterialTheme(colors = darkColors()) {
            WebStoryboard(KotlinPlusPowerAssertEqualsLove)
        }
    }
}
