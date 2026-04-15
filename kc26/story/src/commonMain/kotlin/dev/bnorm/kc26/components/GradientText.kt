package dev.bnorm.kc26.components

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import dev.bnorm.kc26.template.gradientOverlay

@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        text = text,
        modifier = modifier.gradientOverlay(),
        textAlign = textAlign,
        style = style,
    )
}

@Composable
fun GradientText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        text = text,
        modifier = modifier.gradientOverlay(),
        textAlign = textAlign,
        style = style,
    )
}
