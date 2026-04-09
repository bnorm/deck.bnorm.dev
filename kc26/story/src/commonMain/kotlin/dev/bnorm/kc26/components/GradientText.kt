package dev.bnorm.kc26.components

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import dev.bnorm.kc26.template.gradientOverlay

// TODO do these need an outline to make them easier to read?

@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(text, style = style, modifier = modifier.gradientOverlay())
}

@Composable
fun GradientText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(text, style = style, modifier = modifier.gradientOverlay())
}
