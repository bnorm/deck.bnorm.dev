package dev.bnorm.kc25.broadcast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.assist.Caption

fun BroadcastCaption(
    state: StoryState,
    reactionListener: MutableState<ReactionListener?>,
): Caption {
    val storyBroadcaster = mutableStateOf<StoryBroadcaster?>(null)

    return Caption {
        Broadcast(state, storyBroadcaster.value)
        Column {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                IconButton(
                    text = "Broadcasting",
                    icon = if (storyBroadcaster.value != null) Icons.AutoMirrored.Rounded.Send else Icons.Rounded.Warning,
                    onClick = {
                        storyBroadcaster.value = if (storyBroadcaster.value == null) StoryBroadcaster() else null
                        reactionListener.value = if (reactionListener.value == null) ReactionListener() else null
                    }
                )
            }

            ReactionGraph(reactionListener.value)
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
