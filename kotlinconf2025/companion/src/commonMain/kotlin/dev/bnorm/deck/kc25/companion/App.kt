package dev.bnorm.deck.kc25.companion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.deck.shared.broadcast.BroadcastFrame
import dev.bnorm.deck.shared.broadcast.BroadcastMessage
import dev.bnorm.deck.shared.broadcast.toStoryboard
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.easel.EmbeddedStoryboard
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun App() {
    val storyboard = remember { createStoryboard() }

    var latest by remember { mutableStateOf<BroadcastMessage?>(null) }
    LaunchedEffect(Unit) {
        while (true) {
            BroadcastClient.subscribe("story-kc25").collect {
                latest = it
            }
            delay(30.seconds)
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (val latest = latest) {
            null -> {
                Text("Waiting to start...", fontSize = 50.sp)
            }

            is BroadcastFrame -> {
                EmbeddedStoryboard(storyboard)
                storyboard.jumpTo(latest.toStoryboard())
            }
        }
    }
}