package dev.bnorm.deck.shared.socials

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.generated.resources.JetBrains
import dev.bnorm.deck.shared.generated.resources.Res
import org.jetbrains.compose.resources.painterResource

@Composable
fun JetBrainsEmployee(
    name: String,
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.JetBrains),
            contentDescription = "",
            modifier = Modifier.size(32.dp),
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column {
            Text(name, style = MaterialTheme.typography.h5)
            Text(title, style = MaterialTheme.typography.body2)
        }
    }
}
