import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.bnorm.deck.kc25.companion.App

fun main() {
    application {
        Window(onCloseRequest = ::exitApplication, title = "droidcon NYC 2025 - Companion") {
            App()
        }
    }
}
