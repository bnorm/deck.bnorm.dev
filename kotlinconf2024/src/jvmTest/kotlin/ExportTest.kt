import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.KotlinPlusPowerAssertEqualsLove
import dev.bnorm.kc24.Theme
import dev.bnorm.librettist.DEFAULT_SLIDE_SIZE
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.SlideShowDisplay
import dev.bnorm.librettist.show.Advancement
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.ShowState
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ExportTest {
    private val width = DEFAULT_SLIDE_SIZE.width.value
    private val height = DEFAULT_SLIDE_SIZE.height.value

    @Test
    fun testDrawSquare() = runDesktopComposeUiTest(width.toInt(), height.toInt()) {
        val showState = ShowState(ShowBuilder::KotlinPlusPowerAssertEqualsLove)
        setContent {
            ShowTheme(Theme.dark) {
                SlideShowDisplay(
                    showState = showState,
                    slideSize = DpSize(width.dp, height.dp),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        val doc = PDDocument()
        var index = 0

        createPage(captureToImage(), index++, doc)
        while (showState.index < showState.slides.size - 1) {
            showState.advance(Advancement(direction = Advancement.Direction.Forward))
            waitForIdle()
            createPage(captureToImage(), index++, doc)
        }

        doc.save("test.pdf")
        doc.close()
    }

    private fun createPage(imageBitmap: ImageBitmap, index: Int, doc: PDDocument) {
        val image = Image.makeFromBitmap(imageBitmap.asSkiaBitmap())
        val bytes = image.encodeToData(EncodedImageFormat.PNG)?.bytes
        val name = "slide-${index.toString().padStart(3, '0')}"

        val page = PDPage(PDRectangle(width, height))
        doc.addPage(page)

        val contentStream = PDPageContentStream(doc, page)
        contentStream.drawImage(PDImageXObject.createFromByteArray(doc, bytes, name), 0f, 0f)
        contentStream.close()
    }
}
