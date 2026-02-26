package dev.bnorm.deck.kc25.companion.utils

import androidx.compose.runtime.*
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.Animatic
import kotlinx.coroutines.flow.runningReduce

@Composable
fun rememberMaxIndex(animatic: Animatic): State<Storyboard.Index> {
    val maxIndex = remember {
        snapshotFlow { animatic.targetIndex }
            .runningReduce { accumulator, value -> if (value >= accumulator) value else accumulator }
    }.collectAsState(animatic.targetIndex)

    return maxIndex
}
