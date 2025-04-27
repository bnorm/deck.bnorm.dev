package dev.bnorm.kc25.broadcast

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import dev.bnorm.kc25.template.KodeeReactionDecorator
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.assist.Caption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class Broadcast(
    private val storyState: StoryState,
    coroutineScope: CoroutineScope,
) {
    private val storyBroadcaster = mutableStateOf<StoryBroadcaster?>(null)
    private val reactionListener = mutableStateOf<ReactionListener?>(null)

    // TODO move these to file and make private
    val caption: Caption = BroadcastCaption(storyBroadcaster, reactionListener)
    val decorator: SceneDecorator = KodeeReactionDecorator(reactionListener)

    init {
        coroutineScope.launch {
            while (true) {
                val indexes = snapshotFlow { storyState.targetIndex }
                val broadcaster = snapshotFlow { storyBroadcaster.value }.stateIn(this)
                indexes.collect { frame ->
                    broadcaster.value?.broadcast(
                        BroadcastMessage(
                            sceneIndex = frame.sceneIndex,
                            stateIndex = frame.stateIndex,
                        )
                    )
                }
            }
        }

        // TODO hoist reaction state here
    }
}
