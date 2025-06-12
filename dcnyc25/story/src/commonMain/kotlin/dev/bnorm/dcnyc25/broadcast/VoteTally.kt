package dev.bnorm.dcnyc25.broadcast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.assist.Caption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KClass

@OptIn(ExperimentalCoroutinesApi::class)
class VoteTally(coroutineScope: CoroutineScope) {
    private val voteListener = mutableStateOf<VoteListener?>(null)

    val caption: Caption = VoteTallyCaption(voteListener)

    val votes = mutableStateMapOf<String, MutableMap<KClass<out VoteMessage>, VoteMessage>>()

    init {
        snapshotFlow { voteListener.value }
            .flatMapLatest { it?.listen() ?: emptyFlow() }
            .onEach { vote ->
                val userVotes = votes.getOrPut(vote.userId) { mutableStateMapOf() }
                userVotes[vote::class] = vote
            }
            .launchIn(coroutineScope)
    }
}

inline fun <reified T : VoteMessage> VoteTally.votes(): List<T> {
    return votes.values.mapNotNull { it[T::class] as? T }
}

val LocalVoteTally: ProvidableCompositionLocal<VoteTally?> = compositionLocalOf { null }

private fun VoteTallyCaption(
    voteListener: MutableState<VoteListener?>,
): Caption {
    return Caption {
        Column {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                IconButton(
                    onClick = {
                        voteListener.value = if (voteListener.value == null) VoteListener() else null
                    },
                ) {
                    Icon(
                        imageVector = if (voteListener.value != null) Icons.AutoMirrored.Rounded.Send else Icons.Rounded.Warning,
                        contentDescription = "Broadcasting",
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    )
                }
            }
        }
    }
}
