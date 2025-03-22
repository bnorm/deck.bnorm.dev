package dev.bnorm.deck.shared.broadcast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.storyboard.core.SlideDecorator
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.ui.LocalStoryboard
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Storyboard.Frame.toBroadcast(): BroadcastFrame {
    return BroadcastFrame(slideIndex, stateIndex)
}

fun BroadcastFrame.toStoryboard(): Storyboard.Frame {
    return Storyboard.Frame(slideIndex, stateIndex)
}

@Serializable
@SerialName("frame")
data class BroadcastFrame(
    val slideIndex: Int,
    val stateIndex: Int,
) : BroadcastMessage()

val LocalBroadcaster = compositionLocalOf<Broadcaster?> { null }

@Composable
fun BroadcastFrame(storyboard: Storyboard = LocalStoryboard.current) {
    val broadcaster = LocalBroadcaster.current
    if (broadcaster != null) {
        val frame = storyboard.currentFrame.toBroadcast()
        LaunchedEffect(frame) { broadcaster.broadcast(frame) }
    }
}

val BROADCAST_FRAME_DECORATOR = SlideDecorator { content ->
    BroadcastFrame()
    content()
}
