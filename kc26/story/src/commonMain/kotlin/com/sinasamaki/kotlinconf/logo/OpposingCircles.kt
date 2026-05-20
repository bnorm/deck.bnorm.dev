package com.sinasamaki.kotlinconf.logo

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.sinasamaki.kotlinconf.utils.expandPathCubic

fun DrawScope.opposingCircles(
    progress: Float = 0f
) {
    val delta = progress * 10f
    val left = expandPathCubic(
        source = Path().apply {
            moveTo(28f, 74.5f)
            lineTo(30f + delta, 74.5f)
        },
        padding = 10.5f,
        cornerRadius = RoundedCornerShape(
            topStart = CornerSize(50),
            topEnd = CornerSize(50),
            bottomStart = CornerSize(0),
            bottomEnd = CornerSize(0),
        )
    )

    val right = expandPathCubic(
        source = Path().apply {
            moveTo(51f + delta, 74.5f)
            lineTo(64f, 74.5f)
        },
        padding = 10.5f,
        cornerRadius = RoundedCornerShape(
            topStart = CornerSize(0),
            topEnd = CornerSize(0),
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


}
