package dev.bnorm.kc24.elements

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
    fun Circle(color: Color) {
        Box(modifier = Modifier.padding(start = 18.dp, top = 18.dp).size(24.dp).background(color, shape = CircleShape))
    }

    val shape = RoundedCornerShape(20.dp)
    Box(modifier = modifier.border(4.dp, color, shape).clip(shape)) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(color)
                    .height(56.dp)
            ) {
                Row {
                    Circle(Color(0xFFFF5F57))
                    Circle(Color(0xFFFEBC2E))
                    Circle(Color(0xFF28C940))
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