package com.sinasamaki.kotlinconf.logo

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.util.lerp
import com.sinasamaki.kotlinconf.utils.expandPathCubic

fun DrawScope.drawHeart(progress: Float = 0f) {
    val t = (progress * 6f) % 1f
    val heartbeatScale = when {
        t < 0.10f -> lerp(0.85f, 1.00f, t / 0.10f)
        t < 0.20f -> lerp(1.00f, 0.90f, (t - 0.10f) / 0.10f)
        t < 0.30f -> lerp(0.90f, 1.00f, (t - 0.20f) / 0.10f)
        t < 0.40f -> lerp(1.00f, 0.85f, (t - 0.30f) / 0.10f)
        else -> 0.85f
    }
    val pivot = Offset(14.25f, 12.5f)

    scale(scale = heartbeatScale, pivot = pivot) {
        val path1 = expandPathCubic(
            source = Path().apply {
                val x = 12f
                moveTo(18.5f - x, 18.5f - x)
                lineTo(18.5f, 18.5f)
            },
            padding = 6f,
            cornerRadius = RoundedCornerShape(
                topStart = CornerSize(0),
                topEnd = CornerSize(0),
                bottomStart = CornerSize(50),
                bottomEnd = CornerSize(50),
            )
        )
        val path = expandPathCubic(
            source = Path().apply {
                val x = 12f
                moveTo(10f + x, 18.5f - x)
                lineTo(10f, 18.5f)
            },
            padding = 6f,
            cornerRadius = RoundedCornerShape(
                topStart = CornerSize(0),
                topEnd = CornerSize(0),
                bottomStart = CornerSize(50),
                bottomEnd = CornerSize(50),
            )
        )

        drawPath(
            path = path.or(path1),
            brush = brush,
            style = Stroke(
                width = strokeWidth,
            )
        )
    }
}
