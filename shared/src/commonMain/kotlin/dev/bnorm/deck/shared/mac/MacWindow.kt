package dev.bnorm.deck.shared.mac

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MacWindow(
    color: Color,
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    @Composable
    fun WindowButton(color: Color) {
        val border1 = color.copy(
            red = color.red * .95f,
            green = color.green * .95f,
            blue = color.blue * .95f,
        )
        val border2 = color.copy(
            red = color.red * .9f,
            green = color.green * .9f,
            blue = color.blue * .9f,
        )
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp)
                .size(12.dp)
                .border(width = 1.dp, color = border1, shape = CircleShape)
                .border(width = .5.dp, color = border2, shape = CircleShape)
                .background(color, shape = CircleShape)
        )
    }

    val shape = RoundedCornerShape(10.dp)
    Box(modifier = modifier.border(1.dp, Color.Gray, shape).clip(shape)) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(color)
                    .height(28.dp)
            ) {
                Row {
                    WindowButton(Color(0xFFFF5F57))
                    WindowButton(Color(0xFFFEBC2E))
                    WindowButton(Color(0xFF28C940))
                }
                if (title != null) {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        title()
                    }
                }
            }
            content()
        }
    }
}
