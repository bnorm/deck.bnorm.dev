import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.kc24.KotlinPlusPowerAssertEqualsLove
import dev.bnorm.storyboard.easel.EmbeddedStoryboard
import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val params = URLSearchParams(window.location.search.toJsString())
    val storyboard = KotlinPlusPowerAssertEqualsLove

    val frame = storyboard.frames.getOrNull(params.get("slide")?.toIntOrNull() ?: 0)
    if (frame != null) storyboard.jumpTo(frame)

    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        EmbeddedStoryboard(storyboard)
    }
}
