package dev.bnorm.deck.shared.socials

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(Res.drawable.mastodon),
            contentDescription = "",
            modifier = modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = username, style = MaterialTheme.typography.body2)
    }
}
