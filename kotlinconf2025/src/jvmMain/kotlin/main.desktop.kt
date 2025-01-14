import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.application
import dev.bnorm.kc25.DARK_COLORS
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.DesktopStoryboard

fun main() {
    application {
        MaterialTheme(colors = darkColors()) {
            DesktopStoryboard(storyboard = createStoryboard(mutableStateOf(DARK_COLORS)))
        }
    }
}
