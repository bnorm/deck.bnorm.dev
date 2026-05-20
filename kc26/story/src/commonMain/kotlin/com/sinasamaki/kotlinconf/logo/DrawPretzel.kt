package com.sinasamaki.kotlinconf.logo

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.util.lerp
import com.sinasamaki.kotlinconf.utils.expandPathCubic
import com.sinasamaki.kotlinconf.utils.lineTo
import com.sinasamaki.kotlinconf.utils.moveTo
import com.sinasamaki.kotlinconf.utils.oscillateToZero

fun DrawScope.drawPretzel(progress: Float) {
    val path = Path().apply {
        moveTo(26, 125)
        lineTo(14, 110)
        cubicTo(
            8f, 104f,
            2f, 112f,
            2f, 115f,
        )
        cubicTo(
            0f, 126f,
            14f, 126f,
            14f, 126f,
        )

    }.let { path ->
        expandPathCubic(
            source = path,
            padding = 1.5f,
        )
    }

    val left = path.copy()
    left.transform(
        matrix = Matrix().apply {
            scale(x = -1f)
        }
    )
    left.transform(
        matrix = Matrix().apply {
            translate(x = 28f)
        }
    )

    clipRect(
        left = 0f,
        right = 28f,
    ) {
        rotate(
            degrees = lerp(-4f, 4f, progress.oscillateToZero(5)),
            pivot = Offset(15f, 117f)
        ) {
            translate(
                top = lerp(-.25f, .5f, progress.oscillateToZero(2))
            ) {
                drawPath(
                    path = path.or(left),
                    brush = brush,
                    style = Stroke(
                        width = strokeWidth,
                    )
                )
            }
        }
    }
}
