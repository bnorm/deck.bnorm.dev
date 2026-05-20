package com.sinasamaki.kotlinconf.logo

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import com.sinasamaki.kotlinconf.utils.expandPathCubic
import com.sinasamaki.kotlinconf.utils.oscillateToZero

fun DrawScope.bottomCapsules(
    progress: Float = 0f
) {
    val progress = 1f - progress.oscillateToZero(3)
    val delta = progress * 28f
    val left = expandPathCubic(
        source = Path().apply {
            moveTo(28f, 117f)
            relativeLineTo(delta, 0f)
        },
        padding = 11f,
        cornerRadius = RoundedCornerShape(
            topStart = CornerSize(50),
            topEnd = CornerSize(50),
            bottomStart = CornerSize(0),
            bottomEnd = CornerSize(0),
        )
    )

    val right = expandPathCubic(
        source = Path().apply {
            moveTo(50f + delta, 117f)
            lineTo(80f, 117f)
        },
        padding = 11f,
        cornerRadius = RoundedCornerShape(
            topStart = CornerSize(50),
            topEnd = CornerSize(50),
            bottomStart = CornerSize(50),
            bottomEnd = CornerSize(50),
        )
    )

    drawPath(
        path = left,
        brush = brush,
        style = Stroke(
            width = strokeWidth
        )
    )

    drawPath(
        path = right,
        brush = brush,
        style = Stroke(
            width = strokeWidth
        )
    )

    clipPath(
        path = left,
    ) {
        translate(
            delta
        ) {
            for (i in 0..2) {
                val x = 28f + 4 * i
                drawLine(
                    brush = brush,
                    start = Offset(x, 102f),
                    end = Offset(x, 128f),
                    strokeWidth = strokeWidth,
                )
            }
        }
    }
}
