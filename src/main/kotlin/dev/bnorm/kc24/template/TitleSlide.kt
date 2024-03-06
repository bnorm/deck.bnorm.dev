package dev.bnorm.kc24.template

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.kc24.PreviewSlide

@Composable
fun TitleSlide(
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ProvideTextStyle(MaterialTheme.typography.h1) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun ExampleTitleSlide() {
    PreviewSlide {
        TitleSlide {
            Text(text = "Click to add title")
            Text(text = "Click to add subtitle", style = MaterialTheme.typography.h2)
        }
    }
}
