package dev.bnorm.deck.shared.image.kotlinlogos

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.image.KotlinLogos

val KotlinLogos.FullColorMark: ImageVector
    get() {
        if (_FullColorMark != null) {
            return _FullColorMark!!
        }
        _FullColorMark = Builder(name = "FullColorMark",
                defaultWidth = 500.0.dp, defaultHeight = 500.0.dp, viewportWidth = 500.0f,
                viewportHeight = 500.0f).apply {
            path(fill = linearGradient(0.003435144f to Color(0xFFE44857), 0.4689f to
                    Color(0xFFC711E1), 1.0f to Color(0xFF7F52FF), start =
                    Offset(500.00003f,3.903735E-6f), end = Offset(7.663141E-9f,500.0f)), stroke =
                    null, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(500.0f, 500.0f)
                lineToRelative(-500.0f, 0.0f)
                lineToRelative(0.0f, -500.0f)
                lineToRelative(500.0f, 0.0f)
                lineToRelative(-250.0f, 250.0f)
                close()
            }
        }
        .build()
        return _FullColorMark!!
    }

private var _FullColorMark: ImageVector? = null
