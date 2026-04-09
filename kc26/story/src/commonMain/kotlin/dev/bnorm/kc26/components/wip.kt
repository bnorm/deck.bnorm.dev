package dev.bnorm.kc26.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WIP() {
    Box(Modifier.fillMaxSize()) {
        WIP(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun WIP(modifier: Modifier) {
    Text(
        text = "WIP",
        style = MaterialTheme.typography.h1.copy(color = Color.White),
        modifier = modifier
            .rotate(-30f)
            .alpha(0.5f)
            .border(2.dp, color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    )
}
