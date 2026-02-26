package dev.bnorm.kc25.broadcast

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import dev.bnorm.kc25.template.KodeeReactionDecorator
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.easel.Animatic
import dev.bnorm.storyboard.easel.assist.Caption
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.stateIn

class Broadcast {
    private val storyBroadcaster = mutableStateOf<StoryBroadcaster?>(null)
    private val reactionListener = mutableStateOf<ReactionListener?>(null)

    // TODO move these to file and make private
    val caption: Caption = BroadcastCaption(storyBroadcaster, reactionListener)
    val decorator: ContentDecorator = KodeeReactionDecorator(reactionListener)

    suspend fun attach(animatic: Animatic) {
        coroutineScope {
            while (true) {
                val indexes = snapshotFlow { animatic.targetIndex }
                val broadcaster = snapshotFlow { storyBroadcaster.value }.stateIn(this)
                indexes.collect { frame ->
                    broadcaster.value?.broadcast(
                        BroadcastMessage(
                            sceneIndex = frame.sceneIndex,
                            stateIndex = frame.frameIndex,
                        )
                    )
                }
            }
        }

        // TODO hoist reaction state here
    }
}
