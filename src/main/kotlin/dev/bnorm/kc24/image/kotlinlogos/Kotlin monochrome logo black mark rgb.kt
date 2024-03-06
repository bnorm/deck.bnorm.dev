package dev.bnorm.kc24.image.kotlinlogos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.image.KotlinLogos

public val dev.bnorm.kc24.image.KotlinLogos.`Kotlin monochrome logo black mark rgb`: ImageVector
    get() {
        if (`_kotlin monochrome logo black mark rgb` != null) {
            return `_kotlin monochrome logo black mark rgb`!!
        }
        `_kotlin monochrome logo black mark rgb` = Builder(name = "Kotlin monochrome logo black mark rgb", defaultWidth = 500.0.dp, defaultHeight = 500.0.dp, viewportWidth = 500.0f,
                viewportHeight = 500.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(500.0f, 500.0f)
                lineToRelative(-500.0f, 0.0f)
                lineToRelative(0.0f, -500.0f)
                lineToRelative(500.0f, 0.0f)
                lineToRelative(-250.0f, 250.0f)
                close()
            }
        }
        .build()
        return `_kotlin monochrome logo black mark rgb`!!
    }

private var `_kotlin monochrome logo black mark rgb`: ImageVector? = null
