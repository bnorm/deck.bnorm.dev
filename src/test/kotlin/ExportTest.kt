import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runDesktopComposeUiTest
import dev.bnorm.kc24.PreviewSlide
import dev.bnorm.kc24.Title
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.nio.file.Files
import kotlin.io.path.writeBytes
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ExportTest {
    @Test
    fun testDrawSquare() = runDesktopComposeUiTest(1000, 563) {
        setContent {
            PreviewSlide(1f) {
                Title()
            }
        }
        val image = Image.makeFromBitmap(captureToImage().asSkiaBitmap())
        val output = Files.createTempFile("test-draw-square", ".png")
        val bytes = image.encodeToData(EncodedImageFormat.PNG)?.bytes
        output.writeBytes(bytes ?: error("Could not encode image as png"))
        println(output)
    }
}