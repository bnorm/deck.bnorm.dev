package dev.bnorm.deck.shared.image.kodee

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.image.Kodee

public val Kodee.Regular: ImageVector
    get() {
        if (_Regular != null) {
            return _Regular!!
        }
        _Regular = Builder(
            name = "Regular",
            defaultWidth = 150.0.dp,
            defaultHeight = 150.0.dp,
            viewportWidth = 150.0f,
            viewportHeight = 150.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(108.4f, 70.2f)
                curveToRelative(-2.1f, 0.0f, -4.0f, -1.4f, -4.5f, -3.6f)
                curveToRelative(-0.6f, -2.5f, 1.0f, -5.0f, 3.4f, -5.5f)
                curveToRelative(0.3f, -0.1f, 16.0f, -3.9f, 24.3f, -12.6f)
                curveToRelative(8.1f, -8.6f, 8.2f, -22.2f, 8.2f, -22.3f)
                curveToRelative(0.0f, -2.5f, 2.1f, -4.6f, 4.6f, -4.6f)
                curveToRelative(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                curveToRelative(2.5f, 0.0f, 4.6f, 2.0f, 4.6f, 4.6f)
                curveToRelative(0.0f, 0.7f, 0.0f, 17.4f, -10.8f, 28.7f)
                curveToRelative(-10.2f, 10.7f, -28.1f, 15.0f, -28.8f, 15.2f)
                curveTo(109.1f, 70.2f, 108.7f, 70.2f, 108.4f, 70.2f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(5.6f, 110.4f)
                curveToRelative(-0.3f, 0.0f, -0.5f, 0.0f, -0.8f, -0.1f)
                curveToRelative(-2.5f, -0.4f, -4.2f, -2.8f, -3.8f, -5.3f)
                curveToRelative(0.1f, -0.4f, 1.9f, -11.0f, 9.6f, -22.3f)
                curveToRelative(8.4f, -12.3f, 20.0f, -20.9f, 20.5f, -21.3f)
                curveToRelative(2.1f, -1.5f, 4.9f, -1.1f, 6.4f, 1.0f)
                curveToRelative(1.5f, 2.1f, 1.1f, 4.9f, -1.0f, 6.4f)
                curveToRelative(-0.1f, 0.1f, -10.9f, 8.0f, -18.3f, 19.0f)
                curveToRelative(-6.5f, 9.6f, -8.1f, 18.6f, -8.1f, 18.7f)
                curveTo(9.7f, 108.8f, 7.8f, 110.4f, 5.6f, 110.4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(39.0f, 89.8f)
                lineToRelative(68.2f, -4.8f)
                curveToRelative(5.3f, -0.4f, 9.3f, -5.0f, 9.0f, -10.3f)
                lineToRelative(-3.8f, -54.0f)
                curveToRelative(-0.6f, -8.6f, -10.8f, -12.9f, -17.4f, -7.2f)
                lineTo(71.7f, 33.9f)
                curveToRelative(-1.3f, 1.1f, -3.2f, 1.3f, -4.7f, 0.3f)
                lineToRelative(-25.9f, -17.0f)
                curveToRelative(-7.3f, -4.7f, -16.8f, 0.9f, -16.2f, 9.6f)
                lineToRelative(3.8f, 53.9f)
                curveTo(29.1f, 86.1f, 33.7f, 90.1f, 39.0f, 89.8f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(52.5f, 139.1f)
                horizontalLineToRelative(-8.7f)
                curveToRelative(-2.5f, 0.0f, -4.6f, -2.1f, -4.6f, -4.6f)
                reflectiveCurveToRelative(2.1f, -4.6f, 4.6f, -4.6f)
                horizontalLineToRelative(5.1f)
                lineToRelative(11.5f, -45.6f)
                curveToRelative(0.6f, -2.5f, 3.1f, -4.0f, 5.6f, -3.3f)
                curveToRelative(2.5f, 0.6f, 4.0f, 3.1f, 3.3f, 5.6f)
                lineTo(57.0f, 135.6f)
                curveTo(56.5f, 137.6f, 54.6f, 139.1f, 52.5f, 139.1f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF7F52FF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(97.2f, 139.1f)
                horizontalLineToRelative(-8.7f)
                curveToRelative(-2.1f, 0.0f, -4.0f, -1.4f, -4.5f, -3.5f)
                lineTo(71.7f, 86.5f)
                curveToRelative(-0.6f, -2.5f, 0.9f, -5.0f, 3.3f, -5.6f)
                curveToRelative(2.5f, -0.6f, 5.0f, 0.9f, 5.6f, 3.3f)
                lineToRelative(11.5f, 45.6f)
                horizontalLineToRelative(5.1f)
                curveToRelative(2.5f, 0.0f, 4.6f, 2.1f, 4.6f, 4.6f)
                reflectiveCurveTo(99.8f, 139.1f, 97.2f, 139.1f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(105.6f, 80.3f)
                lineToRelative(-65.7f, 4.6f)
                curveToRelative(-3.3f, 0.2f, -6.2f, -2.3f, -6.5f, -5.6f)
                lineToRelative(-2.1f, -30.7f)
                curveToRelative(-0.2f, -3.3f, 2.3f, -6.2f, 5.6f, -6.5f)
                lineToRelative(65.7f, -4.6f)
                curveToRelative(3.3f, -0.2f, 6.2f, 2.3f, 6.5f, 5.6f)
                lineToRelative(2.1f, 30.7f)
                curveTo(111.5f, 77.1f, 109.0f, 80.0f, 105.6f, 80.3f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(55.4f, 51.9f)
                curveToRelative(4.5f, 0.0f, 8.2f, 3.5f, 8.5f, 8.0f)
                curveToRelative(0.2f, 2.3f, -0.6f, 4.5f, -2.1f, 6.2f)
                reflectiveCurveToRelative(-3.6f, 2.8f, -5.9f, 2.9f)
                curveToRelative(-0.2f, 0.0f, -0.4f, 0.0f, -0.6f, 0.0f)
                curveToRelative(-4.5f, 0.0f, -8.2f, -3.5f, -8.5f, -8.0f)
                curveToRelative(-0.2f, -2.3f, 0.6f, -4.5f, 2.1f, -6.2f)
                curveToRelative(1.5f, -1.7f, 3.6f, -2.8f, 5.9f, -2.9f)
                curveTo(55.0f, 51.9f, 55.2f, 51.9f, 55.4f, 51.9f)
                moveTo(55.4f, 47.1f)
                curveToRelative(-0.3f, 0.0f, -0.6f, 0.0f, -0.9f, 0.0f)
                curveToRelative(-7.4f, 0.5f, -13.0f, 6.9f, -12.4f, 14.3f)
                curveToRelative(0.5f, 7.1f, 6.4f, 12.5f, 13.4f, 12.5f)
                curveToRelative(0.3f, 0.0f, 0.6f, 0.0f, 0.9f, 0.0f)
                curveToRelative(7.4f, -0.5f, 13.0f, -6.9f, 12.4f, -14.3f)
                curveTo(68.3f, 52.5f, 62.4f, 47.1f, 55.4f, 47.1f)
                lineTo(55.4f, 47.1f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(86.9f, 49.8f)
                curveToRelative(4.5f, 0.0f, 8.2f, 3.5f, 8.5f, 8.0f)
                curveToRelative(0.2f, 2.3f, -0.6f, 4.5f, -2.1f, 6.2f)
                reflectiveCurveToRelative(-3.6f, 2.8f, -5.9f, 2.9f)
                curveToRelative(-0.2f, 0.0f, -0.4f, 0.0f, -0.6f, 0.0f)
                curveToRelative(-4.5f, 0.0f, -8.2f, -3.5f, -8.5f, -8.0f)
                curveToRelative(-0.2f, -2.3f, 0.6f, -4.5f, 2.1f, -6.2f)
                curveToRelative(1.5f, -1.7f, 3.6f, -2.8f, 5.9f, -2.9f)
                curveTo(86.5f, 49.8f, 86.7f, 49.8f, 86.9f, 49.8f)
                moveTo(86.9f, 44.9f)
                curveToRelative(-0.3f, 0.0f, -0.6f, 0.0f, -0.9f, 0.0f)
                curveToRelative(-7.4f, 0.5f, -13.0f, 6.9f, -12.4f, 14.3f)
                curveToRelative(0.5f, 7.1f, 6.4f, 12.5f, 13.4f, 12.5f)
                curveToRelative(0.3f, 0.0f, 0.6f, 0.0f, 0.9f, 0.0f)
                curveToRelative(7.4f, -0.5f, 13.0f, -6.9f, 12.4f, -14.3f)
                curveTo(99.8f, 50.4f, 93.9f, 44.9f, 86.9f, 44.9f)
                lineTo(86.9f, 44.9f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(66.4f, 74.9f)
                curveToRelative(1.9f, 0.7f, 4.0f, 0.9f, 5.9f, 0.7f)
                curveToRelative(2.0f, -0.2f, 4.0f, -0.7f, 5.8f, -1.7f)
                curveToRelative(1.1f, -0.7f, 0.1f, -2.4f, -1.0f, -1.7f)
                curveToRelative(-2.9f, 1.8f, -7.1f, 2.1f, -10.2f, 0.8f)
                curveToRelative(-0.5f, -0.2f, -1.1f, 0.2f, -1.2f, 0.7f)
                curveTo(65.5f, 74.2f, 65.9f, 74.7f, 66.4f, 74.9f)
                lineTo(66.4f, 74.9f)
                close()
            }
        }
            .build()
        return _Regular!!
    }

private var _Regular: ImageVector? = null
