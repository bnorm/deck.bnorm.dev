package dev.bnorm.kc24.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MacWindow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    @Composable
    fun Circle(color: Color) {
        Box(modifier = Modifier.padding(start = 9.dp, top = 9.dp).size(12.dp).background(color, shape = CircleShape))
    }

    Box(
        modifier = modifier.fillMaxSize()
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp)),
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF0F0F1)).height(28.dp)) {
                Row {
                    Circle(Color(0xFFFF5F57))
                    Circle(Color(0xFFFEBC2E))
                    Circle(Color(0xFF28C940))
                }
            }
            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                content()
            }
        }
    }
}