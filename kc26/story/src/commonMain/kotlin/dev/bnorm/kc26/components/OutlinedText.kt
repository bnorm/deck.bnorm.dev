package dev.bnorm.kc26.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

private const val DefaultWidth = 2f

@Composable
fun OutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
    outlineColor: Color = Color.Black,
    outlineStroke: Stroke = Stroke(width = DefaultWidth),
) {
    Box(modifier) {
        Text(text, textAlign = textAlign, style = style.copy(color = outlineColor, drawStyle = outlineStroke))
        Text(text, textAlign = textAlign, style = style)
    }
}

@Composable
fun OutlinedText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
    outlineColor: Color = Color.Black,
    outlineStroke: Stroke = Stroke(width = DefaultWidth),
) {
    Box(modifier) {
        Text(text, textAlign = textAlign, style = style.copy(color = outlineColor, drawStyle = outlineStroke))
        Text(text, textAlign = textAlign, style = style)
    }
}
