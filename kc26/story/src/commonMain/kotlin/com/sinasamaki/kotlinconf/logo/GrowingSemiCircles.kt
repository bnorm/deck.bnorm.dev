package com.sinasamaki.kotlinconf.logo

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.util.lerp

fun DrawScope.growingSemiCircles(
    progress: Float,
    bounds: Rect,
    vertical: Boolean,
) {
    val progress = (progress * 4f) % 1f
    clipRect(
        left = bounds.left,
        top = bounds.top,
        right = bounds.right,
        bottom = bounds.bottom,
    ) {
        rotate(
            degrees = if (vertical) 0f else -90f,
            pivot = bounds.center,
        ) {
            for (i in 0..2) {
                val y = (-(bounds.center.y - bounds.top) * i) + bounds.height / 2 * progress
                val halfH = bounds.height / 2f
                val scale = when {
                    y < -bounds.height -> .8f
                    y < -halfH -> lerp(.8f, 0f, (y + bounds.height) / halfH)
                    y < 0f -> 0f
                    else -> lerp(0f, .8f, y / halfH)
                }.coerceIn(0f, 1f).let {
                    FastOutSlowInEasing.transform(it)
                }
                translate(
                    top = y
                ) {
                    drawLine(
                        brush = brush,
                        strokeWidth = strokeWidth,
                        start = Offset(bounds.left, bounds.center.y),
                        end = Offset(bounds.right, bounds.center.y)
                    )
                    drawPath(
                        path = Path().apply {
                            addArc(
                                oval = bounds.copy(
                                    left = bounds.left + bounds.width / 2 * scale,
                                    bottom = bounds.bottom - bounds.height / 2 * scale,
                                    right = bounds.right - bounds.width / 2 * scale,
                                    top = bounds.top + bounds.height / 2 * scale,
                                ),
                                startAngleDegrees = 0f,
                                sweepAngleDegrees = 180f
                            )
                        },
                        brush = brush,
                        style = Stroke(strokeWidth),
                    )
                }
            }
        }
    }
    drawRect(
        brush = brush,
        topLeft = bounds.topLeft,
        size = bounds.size,
        style = Stroke(strokeWidth)
    )
}
