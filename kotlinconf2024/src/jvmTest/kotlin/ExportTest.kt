import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
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

/**
 * TODO In the future, we may be able to use GraphicsLayer?
 *
 * ```
 * val scope = rememberCoroutineScope()
 * val graphicsLayer = rememberGraphicsLayer()
 *
 * ComposableToCapture(
 *     modifier = modifier.layout { measurable, constraints ->
 *         val placeable = measurable.measure(constraints)
 *         layout(placeable.width, placeable.height) {
 *             placeable.placeWithLayer(0, 0, graphicsLayer)
 *         }
 *     }
 * )
 *
 * Button(
 *     onClick = {
 *         scope.launch {
 *             val bitmap = graphicsLayer.toImageBitmap()
 *         }
 *     }
 * )
 * ```
 *
 * Or maybe we can use this now?
 *
 * ```
 * Column(
 *     modifier = Modifier
 *         .padding(padding)
 *         .fillMaxSize()
 *         .drawWithCache {
 *             // Example that shows how to redirect rendering to an Android Picture and then
 *             // draw the picture into the original destination
 *             val width = this.size.width.toInt()
 *             val height = this.size.height.toInt()
 *
 *             onDrawWithContent {
 *                 val pictureCanvas =
 *                     androidx.compose.ui.graphics.Canvas(
 *                         picture.beginRecording(
 *                             width,
 *                             height
 *                         )
 *                     )
 *                 // requires at least 1.6.0-alpha01+
 *                 draw(this, this.layoutDirection, pictureCanvas, this.size) {
 *                     this@onDrawWithContent.drawContent()
 *                 }
 *                 picture.endRecording()
 *
 *                 drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
 *             }
 *         }
 *
 * ) {
 *     ScreenContentToCapture()
 * }
 *
 * val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
 *     Bitmap.createBitmap(picture)
 * } else {
 *     val bitmap = Bitmap.createBitmap(
 *         picture.width,
 *         picture.height,
 *         Bitmap.Config.ARGB_8888
 *     )
 *     val canvas = android.graphics.Canvas(bitmap)
 *     canvas.drawColor(android.graphics.Color.WHITE)
 *     canvas.drawPicture(picture)
 *     bitmap
 * }
 * ```
 *
 * TODO or use SingleLayerComposeScene?
 * ```
 *     val scene = SingleLayerComposeScene()
 *     scene.setContent(content)
 *     val bmp = ImageBitmap(size.width, size.height)
 *     val canvas = Canvas(bmp)
 *     scene.render(canvas, 0L)
 *     val result = ImageIO.write(bmp.toAwtImage(), "png", File("C:\\Users\\your-user\\Desktop\\test.jpg"))
 *     scene.close()
 * ```
 *
 * TODO or use ImageComposeScene?
 */
@OptIn(ExperimentalTestApi::class)
class ExportTest {
    private val width = DEFAULT_SLIDE_SIZE.width.value
    private val height = DEFAULT_SLIDE_SIZE.height.value

    @Test
    fun testDrawSquare() = runDesktopComposeUiTest(width.toInt(), height.toInt()) {
        val doc = PDDocument()

        val slides = buildSlides(ShowBuilder::KotlinPlusPowerAssertEqualsLove)
        for ((page, index) in slides.toIndexes().withIndex()) {
            setSlide(slides, index)
            createPage(captureToImage(), page, doc)
        }

        doc.save("test.pdf")
        doc.close()
    }

    private fun DesktopComposeUiTest.setSlide(slides: List<Slide>, index: Slide.Index) {
        setContent {
            val content = slides[index.index].content

            ShowTheme(Theme.dark) {
                ScaledBox(
                    targetSize = DpSize(width.dp, height.dp),
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        key(index) {
                            SharedTransitionLayout {
                                AnimatedContent(Unit) {
                                    val scope = SlideScope(
                                        SlideState.Index(index.state),
                                        this@AnimatedContent,
                                        this@SharedTransitionLayout,
                                    )
                                    scope.content()
                                }
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
