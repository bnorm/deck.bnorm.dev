package dev.bnorm.dcnyc25.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.sections.getBoundingBox

@Composable
fun BoxScope.HighlightStrings(
    visible: Transition<Boolean>,
    strings: List<String>,
    color: Color,
    textLayout: () -> TextLayoutResult,
    modifier: Modifier = Modifier,
) {
    visible.AnimatedVisibility(
        visible = { it },
        enter = fadeIn(tween(750)),
        exit = fadeOut(tween(750)),
        modifier = modifier.matchParentSize()
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val measured = textLayout()

            val original = measured.layoutInput.text
            for (string in strings) {
                if (string.isEmpty()) continue
                val index = original.indexOf(string)
                if (index < 0) continue

                val box = measured.getBoundingBox(index, index + string.length - 1)
                drawRoundRect(
                    color = color,
                    topLeft = box.topLeft,
                    size = box.size,
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                )
            }
        }
    }
}

@Composable
fun BoxScope.HighlightCharacters(
    visible: Transition<Boolean>,
    strings: List<String>,
    color: Color,
    textLayout: () -> TextLayoutResult,
    modifier: Modifier = Modifier,
) {
    visible.AnimatedVisibility(
        visible = { it },
        enter = fadeIn(tween(750)),
        exit = fadeOut(tween(750)),
        modifier = modifier.matchParentSize()
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val measured = textLayout()

            val original = measured.layoutInput.text
            for (string in strings) {
                if (string.isEmpty()) continue
                val index = original.indexOf(string)
                if (index < 0) continue

                repeat(string.length) {
                    val box = measured.getBoundingBox(index + it)
                    drawRoundRect(
                        color = color,
                        topLeft = box.topLeft,
                        size = box.size,
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                    )
                }
            }
        }
    }
}
