package dev.bnorm.deck.kc25.companion.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.StoryState
import kotlinx.coroutines.flow.runningReduce

@Composable
fun rememberMaxIndex(storyState: StoryState): State<Storyboard.Index> {
    val maxIndex = remember {
        snapshotFlow { storyState.targetIndex }
            .runningReduce { accumulator, value -> if (value >= accumulator) value else accumulator }
    }.collectAsState(storyState.targetIndex)

    return maxIndex
}
