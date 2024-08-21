package dev.bnorm.kc24.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.generated.resources.Res
import dev.bnorm.deck.shared.generated.resources.mastodon
import org.jetbrains.compose.resources.painterResource

@Composable
fun Mastodon(username: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(48.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.mastodon),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = username, style = MaterialTheme.typography.body2)
    }
}