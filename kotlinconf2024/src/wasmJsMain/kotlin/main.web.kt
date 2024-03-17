import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.kc24.KotlinPlusPowerAssertEqualsLove
import dev.bnorm.kc24.Theme
import dev.bnorm.librettist.EmbeddedSlideShow
import dev.bnorm.librettist.show.ShowBuilder

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        EmbeddedSlideShow(
            theme = Theme.dark,
            builder = ShowBuilder::KotlinPlusPowerAssertEqualsLove
        )
    }
}
