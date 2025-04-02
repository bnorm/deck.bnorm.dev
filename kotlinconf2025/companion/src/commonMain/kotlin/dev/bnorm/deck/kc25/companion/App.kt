package dev.bnorm.deck.kc25.companion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.kc25.broadcast.BroadcastMessage
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.rememberStoryState
import dev.bnorm.storyboard.easel.EmbeddedStory
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

fun BroadcastMessage.toStoryboard(): Storyboard.Index {
    return Storyboard.Index(sceneIndex, stateIndex)
}

@Composable
fun App() {
    val storyboard = rememberStoryState(remember { createStoryboard() })
    val broadcastClient = remember { BroadcastClient(bearerToken = null, BroadcastMessage.serializer()) }

    var latest by remember { mutableStateOf<BroadcastMessage?>(null) }
    LaunchedEffect(Unit) {
        while (true) {
            broadcastClient.subscribe("story-kc25").collect {
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

            else -> {
                EmbeddedStory(storyboard)

                LaunchedEffect(latest) {
                    storyboard.jumpTo(latest.toStoryboard())
                }
            }
        }
    }
}