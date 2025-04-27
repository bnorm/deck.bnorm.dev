package dev.bnorm.deck.kc25.companion.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Title() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Writing Your Third", style = MaterialTheme.typography.h2)
        Text("Kotlin Compiler Plugin", style = MaterialTheme.typography.h2)
        Spacer(Modifier.height(16.dp))
        Text("A Presentation Companion", style = MaterialTheme.typography.h4)
    }
}
