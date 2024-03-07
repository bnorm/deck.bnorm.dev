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

public val Kodee.Small: ImageVector
    get() {
        if (_Small != null) {
            return _Small!!
        }
        _Small = Builder(
            name = "Small",
            defaultWidth = 55.0.dp,
            defaultHeight = 55.0.dp,
            viewportWidth = 55.0f,
            viewportHeight = 55.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(39.6f, 25.8f)
                curveToRelative(-0.7f, 0.0f, -1.4f, -0.5f, -1.6f, -1.3f)
                curveToRelative(-0.2f, -0.9f, 0.3f, -1.8f, 1.2f, -2.0f)
                curveToRelative(0.1f, 0.0f, 5.8f, -1.4f, 8.8f, -4.6f)
                curveToRelative(3.0f, -3.1f, 3.0f, -8.1f, 3.0f, -8.1f)
                curveToRelative(0.0f, -0.9f, 0.7f, -1.6f, 1.6f, -1.7f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                curveToRelative(0.9f, 0.0f, 1.6f, 0.7f, 1.7f, 1.6f)
                curveToRelative(0.0f, 0.3f, 0.0f, 6.3f, -3.9f, 10.4f)
                curveToRelative(-3.7f, 3.9f, -10.2f, 5.5f, -10.5f, 5.5f)
                curveTo(39.9f, 25.7f, 39.8f, 25.8f, 39.6f, 25.8f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(2.2f, 40.4f)
                curveToRelative(-0.1f, 0.0f, -0.2f, 0.0f, -0.3f, 0.0f)
                curveToRelative(-0.9f, -0.2f, -1.5f, -1.0f, -1.3f, -1.9f)
                curveToRelative(0.0f, -0.2f, 0.7f, -4.0f, 3.5f, -8.1f)
                curveToRelative(3.0f, -4.5f, 7.3f, -7.6f, 7.5f, -7.7f)
                curveToRelative(0.7f, -0.5f, 1.8f, -0.4f, 2.3f, 0.4f)
                reflectiveCurveToRelative(0.4f, 1.8f, -0.4f, 2.3f)
                curveToRelative(0.0f, 0.0f, -4.0f, 2.9f, -6.7f, 6.9f)
                curveToRelative(-2.4f, 3.5f, -3.0f, 6.8f, -3.0f, 6.8f)
                curveTo(3.7f, 39.8f, 3.0f, 40.4f, 2.2f, 40.4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(14.4f, 32.9f)
                lineToRelative(24.8f, -1.7f)
                curveToRelative(1.9f, -0.1f, 3.4f, -1.8f, 3.3f, -3.7f)
                lineTo(41.1f, 7.8f)
                curveToRelative(-0.2f, -3.1f, -3.9f, -4.7f, -6.3f, -2.6f)
                lineToRelative(-8.5f, 7.4f)
                curveToRelative(-0.5f, 0.4f, -1.2f, 0.5f, -1.7f, 0.1f)
                lineToRelative(-9.4f, -6.2f)
                curveTo(12.5f, 4.8f, 9.1f, 6.9f, 9.3f, 10.0f)
                lineToRelative(1.4f, 19.6f)
                curveTo(10.8f, 31.6f, 12.5f, 33.0f, 14.4f, 32.9f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(19.3f, 50.8f)
                horizontalLineToRelative(-3.2f)
                curveToRelative(-0.9f, 0.0f, -1.7f, -0.7f, -1.7f, -1.7f)
                reflectiveCurveToRelative(0.7f, -1.7f, 1.7f, -1.7f)
                horizontalLineTo(18.0f)
                lineToRelative(4.2f, -16.6f)
                curveToRelative(0.2f, -0.9f, 1.1f, -1.4f, 2.0f, -1.2f)
                curveToRelative(0.9f, 0.2f, 1.4f, 1.1f, 1.2f, 2.0f)
                lineToRelative(-4.5f, 17.9f)
                curveTo(20.7f, 50.3f, 20.1f, 50.8f, 19.3f, 50.8f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(35.6f, 50.8f)
                horizontalLineToRelative(-3.2f)
                curveToRelative(-0.8f, 0.0f, -1.4f, -0.5f, -1.6f, -1.2f)
                lineToRelative(-4.5f, -17.9f)
                curveToRelative(-0.2f, -0.9f, 0.3f, -1.8f, 1.2f, -2.0f)
                curveToRelative(0.9f, -0.2f, 1.8f, 0.3f, 2.0f, 1.2f)
                lineToRelative(4.2f, 16.6f)
                horizontalLineToRelative(1.9f)
                curveToRelative(0.9f, 0.0f, 1.7f, 0.7f, 1.7f, 1.7f)
                reflectiveCurveTo(36.5f, 50.8f, 35.6f, 50.8f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(38.4f, 29.0f)
                lineToRelative(-23.5f, 1.6f)
                curveToRelative(-1.1f, 0.1f, -2.1f, -0.8f, -2.2f, -1.9f)
                lineToRelative(-0.8f, -11.0f)
                curveToRelative(-0.1f, -1.1f, 0.8f, -2.1f, 1.9f, -2.2f)
                lineToRelative(23.5f, -1.6f)
                curveToRelative(1.1f, -0.1f, 2.1f, 0.8f, 2.2f, 1.9f)
                lineToRelative(0.8f, 11.0f)
                curveTo(40.4f, 27.9f, 39.5f, 28.9f, 38.4f, 29.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(20.1f, 18.8f)
                curveToRelative(1.7f, 0.0f, 3.2f, 1.3f, 3.3f, 3.1f)
                curveToRelative(0.1f, 1.8f, -1.2f, 3.4f, -3.0f, 3.5f)
                curveToRelative(-0.1f, 0.0f, -0.2f, 0.0f, -0.2f, 0.0f)
                curveToRelative(-1.7f, 0.0f, -3.2f, -1.3f, -3.3f, -3.1f)
                curveToRelative(-0.1f, -1.8f, 1.2f, -3.4f, 3.0f, -3.5f)
                curveTo(20.0f, 18.8f, 20.0f, 18.8f, 20.1f, 18.8f)
                moveTo(20.1f, 16.9f)
                curveToRelative(-0.1f, 0.0f, -0.2f, 0.0f, -0.4f, 0.0f)
                curveToRelative(-2.8f, 0.2f, -5.0f, 2.6f, -4.8f, 5.5f)
                curveToRelative(0.2f, 2.7f, 2.4f, 4.8f, 5.1f, 4.8f)
                curveToRelative(0.1f, 0.0f, 0.2f, 0.0f, 0.4f, 0.0f)
                curveToRelative(2.8f, -0.2f, 5.0f, -2.6f, 4.8f, -5.5f)
                curveTo(25.0f, 19.0f, 22.8f, 16.9f, 20.1f, 16.9f)
                lineTo(20.1f, 16.9f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(32.2f, 17.9f)
                curveToRelative(1.7f, 0.0f, 3.2f, 1.3f, 3.3f, 3.1f)
                curveToRelative(0.1f, 0.9f, -0.2f, 1.7f, -0.8f, 2.4f)
                curveToRelative(-0.6f, 0.7f, -1.4f, 1.1f, -2.2f, 1.1f)
                curveToRelative(-0.1f, 0.0f, -0.2f, 0.0f, -0.2f, 0.0f)
                curveToRelative(-1.7f, 0.0f, -3.2f, -1.3f, -3.3f, -3.1f)
                curveToRelative(-0.1f, -1.8f, 1.2f, -3.4f, 3.0f, -3.5f)
                curveTo(32.0f, 17.9f, 32.1f, 17.9f, 32.2f, 17.9f)
                moveTo(32.2f, 16.1f)
                curveToRelative(-0.1f, 0.0f, -0.2f, 0.0f, -0.4f, 0.0f)
                curveToRelative(-2.8f, 0.2f, -5.0f, 2.6f, -4.8f, 5.5f)
                curveToRelative(0.2f, 2.7f, 2.4f, 4.8f, 5.1f, 4.8f)
                curveToRelative(0.1f, 0.0f, 0.2f, 0.0f, 0.4f, 0.0f)
                curveToRelative(2.8f, -0.2f, 5.0f, -2.6f, 4.8f, -5.5f)
                curveTo(37.1f, 18.2f, 34.9f, 16.1f, 32.2f, 16.1f)
                lineTo(32.2f, 16.1f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(25.6f, 28.0f)
                curveToRelative(0.9f, 0.0f, 1.8f, -0.4f, 2.4f, -1.1f)
                curveToRelative(0.1f, -0.1f, 0.1f, -0.4f, 0.0f, -0.5f)
                curveToRelative(-0.1f, -0.1f, -0.4f, -0.1f, -0.5f, 0.0f)
                curveToRelative(-0.1f, 0.1f, -0.1f, 0.1f, -0.2f, 0.2f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, -0.1f, 0.0f)
                curveToRelative(0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, -0.1f, 0.0f)
                curveToRelative(-0.1f, 0.1f, -0.2f, 0.1f, -0.3f, 0.2f)
                curveToRelative(0.0f, 0.0f, -0.1f, 0.0f, -0.1f, 0.1f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, -0.1f, 0.0f)
                curveToRelative(0.0f, 0.0f, -0.1f, 0.0f, -0.1f, 0.0f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, -0.1f, 0.0f)
                curveToRelative(0.0f, 0.0f, -0.1f, 0.0f, -0.1f, 0.0f)
                curveToRelative(-0.1f, 0.0f, -0.2f, 0.1f, -0.3f, 0.1f)
                curveToRelative(0.0f, 0.0f, -0.1f, 0.0f, -0.1f, 0.0f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, -0.1f, 0.0f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                curveToRelative(-0.1f, 0.0f, -0.2f, 0.0f, -0.3f, 0.0f)
                curveToRelative(-0.2f, 0.0f, -0.4f, 0.2f, -0.4f, 0.4f)
                curveTo(25.2f, 27.8f, 25.4f, 28.0f, 25.6f, 28.0f)
                lineTo(25.6f, 28.0f)
                close()
            }
        }
            .build()
        return _Small!!
    }

private var _Small: ImageVector? = null
