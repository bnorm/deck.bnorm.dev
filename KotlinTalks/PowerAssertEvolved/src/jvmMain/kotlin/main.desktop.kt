import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.application
import dev.bnorm.evolved.createStoryboard
import dev.bnorm.storyboard.easel.DesktopStoryboard

fun main() {
    val storyboard = createStoryboard()
    application {
        MaterialTheme(colors = darkColors()) {
            DesktopStoryboard(storyboard = storyboard)
        }
    }
}
