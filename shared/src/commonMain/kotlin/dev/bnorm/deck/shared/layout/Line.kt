package dev.bnorm.deck.shared.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotateRad
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.unit.toOffset
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DecoratedScope.Line(
    startKey: Any,
    startAlignment: Alignment,
    startCap: Shape? = null,
    endKey: Any,
    endAlignment: Alignment,
    endCap: Shape? = null,
    color: Color,
    stroke: Stroke,
    modifier: Modifier = Modifier,
) {
    val measure = remember { PathMeasure() }
    Decoration(modifier) { layout ->
        val startRect = layout.getBoundingBox(startKey) ?: return@Decoration
        val endRect = layout.getBoundingBox(endKey) ?: return@Decoration
        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            val startPosition = borderPosition(startRect, startAlignment, layoutDirection)
            val endPosition = borderPosition(endRect, endAlignment, layoutDirection)

            val path = Path().apply {
                moveTo(startPosition.x, startPosition.y)
                lineTo(endPosition.x, endPosition.y)
            }

            drawPath(
                path = path,
                color = color,
                style = stroke,
            )

            if (startCap != null) {
                drawCap(
                    cap = startCap,
                    size = Size(4f * stroke.width, 4f * stroke.width),
                    position = startPosition,
                    tangent = measure.withPath(path) { getTangent(0f) } * -1f,
                    color = color,
                )
            }

            if (endCap != null) {
                drawCap(
                    cap = endCap,
                    size = Size(4f * stroke.width, 4f * stroke.width),
                    position = endPosition,
                    tangent = measure.withPath(path) { getTangent(length) },
                    color = color,
                )
            }

            restoreToCount(checkPoint)
        }
    }
}

@Composable
fun DecoratedScope.CubicLine(
    startKey: Any,
    startAlignment: Alignment,
    startCap: Shape? = null,
    endKey: Any,
    endAlignment: Alignment,
    endCap: Shape? = null,
    color: Color,
    stroke: Stroke,
    modifier: Modifier = Modifier,
) {
    val measure = remember { PathMeasure() }
    Decoration(modifier) { layout ->
        val startRect = layout.getBoundingBox(startKey) ?: return@Decoration
        val endRect = layout.getBoundingBox(endKey) ?: return@Decoration
        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            val startPosition = borderPosition(startRect, startAlignment, layoutDirection)
            val startRadians = (startPosition - startRect.center).getRadians()

            val endPosition = borderPosition(endRect, endAlignment, layoutDirection)
            val endRadians = (endPosition - endRect.center).getRadians()

            // TODO what should the projection distance be?
            val distance = (endPosition - startPosition).getDistance() / 2f
            val startProjection = startPosition.project(distance, startRadians)
            val endProjection = endPosition.project(distance, endRadians)

            val path = Path().apply {
                moveTo(startPosition.x, startPosition.y)
                cubicTo(
                    startProjection.x, startProjection.y,
                    endProjection.x, endProjection.y,
                    endPosition.x, endPosition.y,
                )
            }

            drawPath(
                path = path,
                color = color,
                style = stroke,
            )

            if (startCap != null) {
                drawCap(
                    cap = startCap,
                    size = Size(4f * stroke.width, 4f * stroke.width),
                    position = startPosition,
                    tangent = measure.withPath(path) { getTangent(0f) } * -1f,
                    color = color,
                )
            }

            if (endCap != null) {
                drawCap(
                    cap = endCap,
                    size = Size(4f * stroke.width, 4f * stroke.width),
                    position = endPosition,
                    tangent = measure.withPath(path) { getTangent(length) },
                    color = color,
                )
            }

            restoreToCount(checkPoint)
        }
    }
}

private fun DrawScope.drawCap(
    cap: Shape,
    size: Size,
    position: Offset,
    tangent: Offset,
    color: Color,
) {
    val outline = cap.createOutline(size, layoutDirection, this)
    translate(position.x - size.width / 2, position.y - size.height / 2) {
        rotateRad(radians = atan2(tangent.y, tangent.x), pivot = size.center) {
            drawRect(color = Color.Transparent, size = size, blendMode = BlendMode.Clear)
            drawOutline(outline, color)
        }
    }
}

private fun Offset.getRadians(): Float {
    return atan2(y, x)
}

private fun Offset.project(distance: Float, radians: Float): Offset {
    return Offset(x + cos(radians) * distance, y + sin(radians) * distance)
}

private fun borderPosition(rect: Rect, alignment: Alignment, layoutDirection: LayoutDirection): Offset {
    val offset = alignment.align(IntSize.Zero, rect.size.toIntSize(), layoutDirection)
    return rect.topLeft + offset.toOffset()
}

private inline fun <R> PathMeasure.withPath(path: Path, forceClosed: Boolean = false, block: PathMeasure.() -> R): R {
    setPath(path, forceClosed)
    try {
        return block()
    } finally {
        setPath(null, false)
    }
}
