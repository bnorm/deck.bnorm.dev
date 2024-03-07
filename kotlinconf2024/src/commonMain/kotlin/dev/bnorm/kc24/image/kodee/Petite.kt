package dev.bnorm.kc24.image.kodee

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.image.Kodee

val Kodee.Petite: ImageVector
    get() {
        if (_Petite != null) {
            return _Petite!!
        }
        _Petite = Builder(
            name = "Petite",
            defaultWidth = 21.0.dp,
            defaultHeight = 17.8.dp,
            viewportWidth = 21.0f,
            viewportHeight = 17.8f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(2.3f, 17.8f)
                lineToRelative(16.4f, 0.0f)
                curveToRelative(1.3f, 0.0f, 2.3f, -1.0f, 2.3f, -2.3f)
                lineToRelative(0.0f, -12.9f)
                curveToRelative(0.0f, -2.1f, -2.4f, -3.3f, -4.0f, -2.0f)
                lineToRelative(-5.9f, 3.2f)
                curveToRelative(-0.3f, 0.2f, -0.8f, 0.2f, -1.1f, 0.0f)
                lineTo(4.1f, 0.5f)
                curveToRelative(-1.7f, -1.2f, -4.0f, -0.1f, -4.0f, 2.0f)
                lineToRelative(0.0f, 12.9f)
                curveTo(0.0f, 16.7f, 1.0f, 17.8f, 2.3f, 17.8f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(17.2f, 16.1f)
                lineToRelative(-13.5f, 0.0f)
                curveToRelative(-1.1f, 0.0f, -2.1f, -0.9f, -2.1f, -2.1f)
                lineToRelative(0.0f, -5.8f)
                curveToRelative(0.0f, -1.1f, 0.9f, -2.1f, 2.1f, -2.1f)
                lineToRelative(13.5f, 0.0f)
                curveToRelative(1.1f, 0.0f, 2.1f, 0.9f, 2.1f, 2.1f)
                lineToRelative(0.0f, 5.8f)
                curveTo(19.3f, 15.2f, 18.4f, 16.1f, 17.2f, 16.1f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(6.3f, 7.0f)
                lineToRelative(0.0f, 1.3f)
                curveToRelative(0.6f, 0.0f, 1.2f, 0.2f, 1.6f, 0.7f)
                reflectiveCurveToRelative(0.7f, 1.0f, 0.7f, 1.6f)
                curveToRelative(0.0f, 1.3f, -1.0f, 2.3f, -2.3f, 2.3f)
                curveToRelative(-1.3f, 0.0f, -2.3f, -1.0f, -2.3f, -2.3f)
                curveToRelative(0.0f, -1.3f, 1.0f, -2.3f, 2.3f, -2.3f)
                lineTo(6.3f, 7.0f)
                moveTo(6.3f, 7.0f)
                curveToRelative(-2.0f, 0.0f, -3.5f, 1.6f, -3.6f, 3.5f)
                curveToRelative(0.0f, 2.0f, 1.6f, 3.6f, 3.5f, 3.6f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                curveToRelative(2.0f, 0.0f, 3.5f, -1.6f, 3.6f, -3.5f)
                curveTo(9.9f, 8.5f, 8.3f, 7.0f, 6.3f, 7.0f)
                curveTo(6.3f, 7.0f, 6.3f, 7.0f, 6.3f, 7.0f)
                lineTo(6.3f, 7.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(14.7f, 7.0f)
                lineToRelative(0.0f, 1.3f)
                curveToRelative(1.3f, 0.0f, 2.3f, 1.0f, 2.3f, 2.3f)
                curveToRelative(0.0f, 1.3f, -1.0f, 2.3f, -2.3f, 2.3f)
                curveToRelative(-1.3f, 0.0f, -2.3f, -1.0f, -2.3f, -2.3f)
                curveToRelative(0.0f, -1.3f, 1.0f, -2.3f, 2.3f, -2.3f)
                lineTo(14.7f, 7.0f)
                moveTo(14.7f, 7.0f)
                curveToRelative(-2.0f, 0.0f, -3.5f, 1.6f, -3.6f, 3.5f)
                curveToRelative(0.0f, 2.0f, 1.6f, 3.6f, 3.5f, 3.6f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                curveToRelative(2.0f, 0.0f, 3.5f, -1.6f, 3.6f, -3.5f)
                curveTo(18.3f, 8.6f, 16.7f, 7.0f, 14.7f, 7.0f)
                curveTo(14.7f, 7.0f, 14.7f, 7.0f, 14.7f, 7.0f)
                lineTo(14.7f, 7.0f)
                close()
            }
        }
            .build()
        return _Petite!!
    }

private var _Petite: ImageVector? = null
