import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.kc24.KotlinPlusPowerAssertEqualsLove
import dev.bnorm.kc24.Theme
import dev.bnorm.librettist.EmbeddedSlideShow
import dev.bnorm.librettist.show.ShowBuilder
import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val params = URLSearchParams(window.location.search.toJsString())
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        EmbeddedSlideShow(
            theme = Theme.dark,
            startSlide = params.get("slide")?.toIntOrNull() ?: 0,
            builder = ShowBuilder::KotlinPlusPowerAssertEqualsLove
        )
    }
}
