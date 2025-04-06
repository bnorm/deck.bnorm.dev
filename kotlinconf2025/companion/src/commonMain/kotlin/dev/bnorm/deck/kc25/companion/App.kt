package dev.bnorm.deck.kc25.companion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.kc25.broadcast.BroadcastMessage
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.Story
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.overlay.StoryOverlay
import dev.bnorm.storyboard.easel.rememberStoryState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

fun BroadcastMessage.toStoryboard(): Storyboard.Index {
    return Storyboard.Index(sceneIndex, stateIndex)
}

@Composable
fun App() {
    val storyboard = rememberStoryState(remember { createStoryboard() })
    val broadcastClient = remember { BroadcastClient(bearerToken = null, BroadcastMessage.serializer()) }

    var latest by remember { mutableStateOf(Storyboard.Index(3, 0)) }
    LaunchedEffect(Unit) {
        while (true) {
            broadcastClient.subscribe("story-kc25").collect {
                // latest = maxOf(latest, it.toStoryboard())
                latest = it.toStoryboard()
            }
            delay(30.seconds)
        }
    }

    LaunchedEffect(latest) {
        storyboard.jumpTo(latest)
    }

    MaterialTheme(colors = lightColors()) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    // TODO can i make the content scrollable once smaller than min width?
                    .widthIn(max = 960.dp)
                    .requiredWidthIn(min = 720.dp)
            ) {
                Content(latest, storyboard)
            }
        }
    }
}

@Composable
private fun LazyItemScope.ContentCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier
            .animateItem()
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            content()
        }
    }
}

private fun LazyListScope.Content(latest: Storyboard.Index, storyState: StoryState) {
    // TODO make some of these sticky?
    item("title") {
        ContentCard {
            Text("Writing Your Third", style = MaterialTheme.typography.h2)
            Text("Kotlin Compiler Plugin", style = MaterialTheme.typography.h2)
            Spacer(Modifier.height(16.dp))
            Text("A Presentation Companion", style = MaterialTheme.typography.h4)
        }
    }

    item("slides") {
        ContentCard {
            Text("Slides", style = MaterialTheme.typography.h2)
            Spacer(Modifier.height(16.dp))

            // TODO force render the storyboard in preview mode?
            //  - or at least while the navigation is synced?
            // TODO do we need to disable key events? doesn't seem like it...
            StoryOverlay(
                overlay = {
                    // TODO enable navigation when the story is complete?
                    //  - or maybe custom navigation that only allows navigating before latest
                }
            ) {
                Story(storyState)
            }
        }
    }

    repeat(latest.sceneIndex) {
        item(it) {
            ContentCard {
                Text("Information ${it + 1}", style = MaterialTheme.typography.h2)
            }
        }
    }

    item("End") {
        Box(Modifier.height(32.dp))
    }
}
