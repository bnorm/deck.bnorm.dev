package dev.bnorm.dcnyc25.template

import androidx.compose.foundation.layout.Box
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

private const val DefaultWidth = 1f

@Composable
fun OutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Black,
    outline: Stroke = Stroke(width = DefaultWidth),
) {
    Box(modifier) {
        Text(text, style = style.copy(color = color, drawStyle = outline))
        Text(text, style = style)
    }
}

@Composable
fun OutlinedText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Black,
    outline: Stroke = Stroke(width = DefaultWidth),
) {
    Box(modifier) {
        Text(text, style = style.copy(color = color, drawStyle = outline))
        Text(text, style = style)
    }
}
