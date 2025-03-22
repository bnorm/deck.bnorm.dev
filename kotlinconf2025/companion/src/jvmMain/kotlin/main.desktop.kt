import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.bnorm.deck.kc25.companion.App
import org.jetbrains.compose.reload.DevelopmentEntryPoint

fun main() {
    application {
        DevelopmentEntryPoint {
            Window(onCloseRequest = ::exitApplication) {
                App()
            }
        }
    }
}
