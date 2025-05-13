package dev.bnorm.deck.kc25.companion

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Storyboard

fun interface CardContent {
    @Composable
    fun content(index: Storyboard.Index)
}

fun TitleContent(
    title: String,
    content: @Composable ColumnScope.(index: Storyboard.Index) -> Unit,
): CardContent = CardContent { index ->
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Light),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))
        content(index)
    }
}
