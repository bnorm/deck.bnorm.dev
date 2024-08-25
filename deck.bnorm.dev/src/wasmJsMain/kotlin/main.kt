import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.deck.Talks

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        MaterialTheme(colors = darkColors()) {
            Surface(Modifier.fillMaxSize()) {
                Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
                    Talks()
                }
            }
        }
    }
}
