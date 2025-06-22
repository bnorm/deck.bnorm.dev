package dev.bnorm.deck.shared.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun DecoratedScope.Outline(
    key: Any,
    color: Color,
    stroke: Stroke,
    modifier: Modifier = Modifier,
) {
    Decoration(modifier) { layout ->
        val rect = layout.getBoundingBox(key) ?: return@Decoration
        drawRect(color, rect.topLeft, rect.size, style = stroke)
    }
}

@Composable
fun DecoratedScope.Outline(
    key: Any,
    brush: Brush,
    stroke: Stroke,
    modifier: Modifier = Modifier,
) {
    Decoration(modifier) { layout ->
        val rect = layout.getBoundingBox(key) ?: return@Decoration
        drawRect(brush, rect.topLeft, rect.size, style = stroke)
    }
}
