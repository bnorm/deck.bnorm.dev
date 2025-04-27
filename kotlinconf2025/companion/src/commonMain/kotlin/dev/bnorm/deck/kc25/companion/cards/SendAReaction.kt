package dev.bnorm.deck.kc25.companion.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.kc25.broadcast.ReactionMessage
import io.ktor.util.date.*
import kotlinx.coroutines.launch

val broadcastReactor = BroadcastClient(bearerToken = null, ReactionMessage.serializer())

@Composable
fun SendAReaction() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Send A Reaction!", style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Light))
        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Reaction(
                broadcastReactor = broadcastReactor,
                onClick = { ReactionMessage.Heart(getTimeMillis()) },
            )
            Reaction(
                broadcastReactor = broadcastReactor,
                onClick = { ReactionMessage.Excited(getTimeMillis()) },
            )
            Reaction(
                broadcastReactor = broadcastReactor,
                onClick = { ReactionMessage.Electrified(getTimeMillis()) },
            )
            Reaction(
                broadcastReactor = broadcastReactor,
                onClick = { ReactionMessage.Lost(getTimeMillis()) },
            )
        }
    }
}

@Composable
private fun Reaction(
    broadcastReactor: BroadcastClient<ReactionMessage>,
    onClick: () -> ReactionMessage,
) {
    val scope = rememberCoroutineScope()
    val message = remember(onClick) { onClick() }
    Button(
        onClick = {
            scope.launch {
                broadcastReactor.broadcast("story-kc25-react", onClick())
            }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
    ) {
        message.Image(Modifier.size(64.dp))
    }
}
