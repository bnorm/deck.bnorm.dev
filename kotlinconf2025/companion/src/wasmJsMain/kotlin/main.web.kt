import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.deck.kc25.companion.App
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.easel.rememberStoryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val samples = mutableListOf<CodeSample>()
    val storyboard = createStoryboard(sink = samples)
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                for (sample in samples) {
                    sample.string // Initialize the string to ensure it is loaded.
                    delay(100) // TODO is this the best delay?
                }
            }
        }

        val storyState = rememberStoryState(storyboard)
        App(storyState)
    }
}
