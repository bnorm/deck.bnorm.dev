import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.application
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.DesktopStoryboard
import org.jetbrains.compose.reload.DevelopmentEntryPoint

fun main() {
    application {
        val storyboard = createStoryboard()
        DevelopmentEntryPoint {
            MaterialTheme(colors = darkColors()) {
                DesktopStoryboard(storyboard = storyboard)
            }
        }
    }
}
