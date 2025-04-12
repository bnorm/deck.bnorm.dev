package dev.bnorm.deck.kc25.companion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.kc25.broadcast.BroadcastMessage
import dev.bnorm.kc25.broadcast.ReactionMessage
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.Story
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.overlay.StoryOverlay
import dev.bnorm.storyboard.easel.rememberStoryState
import io.ktor.util.date.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

fun BroadcastMessage.toStoryboard(): Storyboard.Index {
    return Storyboard.Index(sceneIndex, stateIndex)
}

// TODO doesn't seem to work on iOS Chrome: https://developer.chrome.com/blog/debugging-chrome-on-ios
@Composable
fun App() {
    val storyState = rememberStoryState(remember { createStoryboard() })
    val broadcastListener = remember { BroadcastClient(bearerToken = null, BroadcastMessage.serializer()) }
    val broadcastReactor = remember { BroadcastClient(bearerToken = null, ReactionMessage.serializer()) }

    var targetIndex by remember { mutableStateOf<Storyboard.Index?>(null) }
    LaunchedEffect(Unit) {
        while (true) {
            broadcastListener.subscribe("story-kc25").collect {
                val targetIndex = it.toStoryboard()
                val i = storyState.storyboard.indices.binarySearch(targetIndex)
                if (i < 0) return@collect // TODO error?

                val direction = when (targetIndex > storyState.currentIndex) {
                    true -> AdvanceDirection.Forward
                    false -> AdvanceDirection.Backward
                }
                val previous = when (direction) {
                    AdvanceDirection.Forward -> storyState.storyboard.indices.getOrNull(i - 1)
                    AdvanceDirection.Backward -> storyState.storyboard.indices.getOrNull(i + 1)
                }

                // Jump to the index directly to the target state if we cannot gracefully advance to it.
                if (storyState.currentIndex != previous) {
                    storyState.jumpTo(targetIndex)
                } else {
                    storyState.advance(direction)
                }
            }
            delay(30.seconds)
        }
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
                Content(targetIndex, storyState, broadcastReactor)
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

private fun LazyListScope.Content(
    latest: Storyboard.Index?,
    storyState: StoryState,
    broadcastReactor: BroadcastClient<ReactionMessage>,
) {
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

    item("Reactions") {
        ContentCard {
            Text("Send A Reaction!", style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Light))
            Spacer(Modifier.height(16.dp))

            val scope = rememberCoroutineScope()
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Reaction(
                    scope = scope,
                    broadcastReactor = broadcastReactor,
                    onClick = { ReactionMessage.Heart(getTimeMillis()) },
                )
                Reaction(
                    scope = scope,
                    broadcastReactor = broadcastReactor,
                    onClick = { ReactionMessage.Excited(getTimeMillis()) },
                )
                Reaction(
                    scope = scope,
                    broadcastReactor = broadcastReactor,
                    onClick = { ReactionMessage.Electrified(getTimeMillis()) },
                )
                Reaction(
                    scope = scope,
                    broadcastReactor = broadcastReactor,
                    onClick = { ReactionMessage.Lost(getTimeMillis()) },
                )
            }
        }
    }

    repeat(latest?.sceneIndex ?: 0) {
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

@Composable
private fun Reaction(
    scope: CoroutineScope,
    broadcastReactor: BroadcastClient<ReactionMessage>,
    onClick: () -> ReactionMessage,
) {
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
