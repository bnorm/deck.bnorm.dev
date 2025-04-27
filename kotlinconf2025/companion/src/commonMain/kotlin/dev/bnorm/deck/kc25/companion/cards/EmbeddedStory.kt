package dev.bnorm.deck.kc25.companion.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.kc25.companion.rememberMaxIndex
import dev.bnorm.deck.kc25.companion.toStoryboard
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import dev.bnorm.kc25.broadcast.BroadcastMessage
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.easel.Story
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.overlay.StoryOverlay
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun EmbeddedStory(storyState: StoryState) {
    ListenToStory(storyState)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Slides", style = MaterialTheme.typography.h2)
        Spacer(Modifier.height(16.dp))

        // TODO do we need to disable key events? doesn't seem like it...
        StoryOverlay(
            overlay = { OverlayNavigation(storyState) }
        ) {
            Story(storyState)
        }
    }
}

@Composable
private fun ListenToStory(storyState: StoryState) {
    // TODO allow detaching from active listening when navigating backwards in the story
    val broadcastListener = remember { BroadcastClient(bearerToken = null, BroadcastMessage.serializer()) }

    LaunchedEffect(Unit) {
        while (true) {
            try {
                broadcastListener.subscribe("story-kc25").collect {
                    val index = it.toStoryboard()
                    val i = storyState.storyboard.indices.binarySearch(index)
                    if (i < 0) return@collect // TODO error?

                    val direction = when (index > storyState.currentIndex) {
                        true -> AdvanceDirection.Forward
                        false -> AdvanceDirection.Backward
                    }
                    val previous = when (direction) {
                        AdvanceDirection.Forward -> storyState.storyboard.indices.getOrNull(i - 1)
                        AdvanceDirection.Backward -> storyState.storyboard.indices.getOrNull(i + 1)
                    }

                    // Jump to the index directly to the target state if we cannot gracefully advance to it.
                    if (storyState.currentIndex != previous) {
                        storyState.jumpTo(index)
                    } else {
                        storyState.advance(direction)
                    }
                }
            } catch (e: Throwable) {
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
            delay(30.seconds)
        }
    }
}

@Composable
private fun StoryOverlayScope.OverlayNavigation(
    storyState: StoryState,
    alignment: Alignment = Alignment.BottomCenter,
) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    val indices = storyState.storyboard.indices

    val maxIndex = rememberMaxIndex(storyState)

    Surface(
        modifier = Modifier
            .align(alignment)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .overlayElement()
    ) {
        Row {
            IconButton(
                text = "Previous",
                icon = Icons.AutoMirrored.Rounded.ArrowBack,
                enabled = storyState.targetIndex > indices.first(),
                onClick = {
                    job?.cancel()
                    job = coroutineScope.launch {
                        storyState.advance(AdvanceDirection.Backward)
                        job = null
                    }
                }
            )
            IconButton(
                text = "Next",
                icon = Icons.AutoMirrored.Rounded.ArrowForward,
                enabled = storyState.targetIndex < indices.last() && storyState.targetIndex < maxIndex.value,
                onClick = {
                    job?.cancel()
                    job = coroutineScope.launch {
                        storyState.advance(AdvanceDirection.Forward)
                        job = null
                    }
                }
            )
        }
    }
}

@Composable
private fun IconButton(
    text: String,
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier
                .pointerHoverIcon(PointerIcon.Hand),
        )
    }
}
