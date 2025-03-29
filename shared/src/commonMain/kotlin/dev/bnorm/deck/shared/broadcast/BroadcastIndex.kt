package dev.bnorm.deck.shared.broadcast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.storyboard.core.SceneDecorator
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.ui.LocalStoryState
import dev.bnorm.storyboard.ui.StoryEffect
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Storyboard.Index.toBroadcast(): BroadcastIndex {
    return BroadcastIndex(sceneIndex, stateIndex)
}

fun BroadcastIndex.toStoryboard(): Storyboard.Index {
    return Storyboard.Index(sceneIndex, stateIndex)
}

@Serializable
@SerialName("index")
data class BroadcastIndex(
    val sceneIndex: Int,
    val stateIndex: Int,
) : BroadcastMessage()

val LocalBroadcaster = compositionLocalOf<Broadcaster?> { null }

@Composable
fun BroadcastIndex() {
    val storyState = LocalStoryState.current ?: return
    val broadcaster = LocalBroadcaster.current ?: return
    val frame = storyState.currentIndex
    StoryEffect(frame) {
        broadcaster.broadcast(frame.toBroadcast())
    }
}

val BROADCAST_INDEX_DECORATOR = SceneDecorator { content ->
    BroadcastIndex()
    content()
}
