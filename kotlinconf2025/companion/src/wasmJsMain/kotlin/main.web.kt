import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.bnorm.deck.kc25.companion.App
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.easel.rememberAnimatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val samples = mutableListOf<CodeSample>()
    val storyboard = createStoryboard(sink = samples)
    ComposeViewport(viewportContainerId = "ComposeTarget") {
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                for (sample in samples) {
                    sample.string // Initialize the string to ensure it is loaded.
                    delay(100) // TODO is this the best delay?
                }
            }
        }

        val animatic = rememberAnimatic { storyboard }
        App(animatic)
    }
}
