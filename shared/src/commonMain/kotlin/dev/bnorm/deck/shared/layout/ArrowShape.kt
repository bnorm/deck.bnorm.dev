package dev.bnorm.deck.shared.layout

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

val ArrowShape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val centerX = size.width / 2
        val centerY = size.height / 2

        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(centerX, centerY)
        path.lineTo(0f, size.height)
        path.close()

        return Outline.Generic(path)
    }
}
