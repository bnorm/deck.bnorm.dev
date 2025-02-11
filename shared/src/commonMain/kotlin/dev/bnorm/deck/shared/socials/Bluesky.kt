package dev.bnorm.deck.shared.socials

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.generated.resources.Res
import dev.bnorm.deck.shared.generated.resources.bluesky_media_kit_logo_svgs
import org.jetbrains.compose.resources.painterResource

@Composable
fun Bluesky(username: String, modifier: Modifier = Modifier) {
    // TODO clip the logo so we can control padding better
    Row(
        modifier = modifier.height(32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.bluesky_media_kit_logo_svgs),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = username, style = MaterialTheme.typography.body2)
    }
}