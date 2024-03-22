import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.test.DesktopComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.KotlinPlusPowerAssertEqualsLove
import dev.bnorm.kc24.Theme
import dev.bnorm.librettist.DEFAULT_SLIDE_SIZE
import dev.bnorm.librettist.ScaledBox
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.show.*
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
        val doc = PDDocument()

        val slides = buildSlides(ShowBuilder::KotlinPlusPowerAssertEqualsLove)
        for ((page, advancement) in slides.advancements.withIndex()) {
            setSlide(slides, advancement)
            createPage(captureToImage(), page, doc)
        }

        doc.save("test.pdf")
        doc.close()
    }

    private fun DesktopComposeUiTest.setSlide(slides: List<Slide>, slideIndex: Pair<Int, Int>) {
        setContent {
            val content = slides[slideIndex.first].content
            val scope = SlideScope(rememberTransition(MutableTransitionState(slideIndex.second)))

            ShowTheme(Theme.dark) {
                ScaledBox(
                    targetSize = DpSize(width.dp, height.dp),
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            key(slideIndex.first, slideIndex.second) {
                                scope.content()
                            }
                        }
                    }
                }
            }
        }
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
