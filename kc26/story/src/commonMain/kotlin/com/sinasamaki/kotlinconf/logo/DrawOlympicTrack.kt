package com.sinasamaki.kotlinconf.logo

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.util.lerp
import com.sinasamaki.kotlinconf.utils.expandPathCubic
import com.sinasamaki.kotlinconf.utils.lineTo
import com.sinasamaki.kotlinconf.utils.moveTo

fun DrawScope.drawOlympicTrack(progress: Float = 1f) {

    clipRect(
        left = 8f,
        top = 64f,
        right = 28f,
        bottom = 106f,
    ) {
        for (i in 0..3) {
            val progress = ((progress * 1f) + i / 3f) % 1f
            val path = Path().apply {
                moveTo(18, 74)
                lineTo(18, 96)
            }.let { path ->
                expandPathCubic(
                    source = path,
                    padding = lerp(2f, 13.5f, progress)
                )

            }

            drawPath(
                path = path,
                brush = brush,
                style = Stroke(
                    width = strokeWidth * (progress / .2f).coerceAtMost(1f),
                )
            )
        }
    }

}
