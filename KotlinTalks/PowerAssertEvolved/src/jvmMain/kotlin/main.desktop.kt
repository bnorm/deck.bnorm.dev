import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.application
import dev.bnorm.evolved.createStoryboard
import dev.bnorm.storyboard.easel.DesktopStoryboard
import org.jetbrains.compose.reload.DevelopmentEntryPoint

fun main() {
    val storyboard = createStoryboard()
    application {
        DevelopmentEntryPoint {
            MaterialTheme(colors = darkColors()) {
                DesktopStoryboard(storyboard = storyboard)
            }
        }
    }
}
