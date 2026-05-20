package com.sinasamaki.kotlinconf.logo

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate

fun DrawScope.drawParallelLines(
    vertical: Boolean,
    topLeft: Offset,
    size: Size,
    clip: Path = Path().apply {
        addRect(
            rect = Rect(topLeft, size)
        )
    },
    progress: Float = 1f,
) {

    drawPath(
        path = clip,
        brush = brush,
        style = Stroke(width = strokeWidth)
    )
    clipPath(
        path = clip
    ) {
        translate(
            left = topLeft.x,
            top = topLeft.y,
        ) {
            for (i in 0..10) {
                if (vertical) {
                    val length = size.width + (4 - (size.width % 4))
                    val x = (4f * i + progress * length) % length
                    drawLine(
                        brush = brush,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = strokeWidth,
                    )
                } else {
                    val length = size.height + (4 - (size.height % 4))
                    val y = (4f * i + progress * length) % length
                    drawLine(
                        brush = brush,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth,
                    )
                }
            }
        }
    }
}
