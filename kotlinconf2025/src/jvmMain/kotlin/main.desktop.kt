import androidx.compose.runtime.mutableStateOf
import dev.bnorm.kc25.DARK_COLORS
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.DesktopStoryboard

fun main() {
    DesktopStoryboard(storyboard = createStoryboard(mutableStateOf(DARK_COLORS)))
}
