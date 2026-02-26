import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.bnorm.deck.kc25.companion.App
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.rememberAnimatic

fun main() {
    application {
        val animatic = rememberAnimatic { createStoryboard() }
        Window(onCloseRequest = ::exitApplication, title = "Companion") {
            App(animatic)
        }
    }
}
