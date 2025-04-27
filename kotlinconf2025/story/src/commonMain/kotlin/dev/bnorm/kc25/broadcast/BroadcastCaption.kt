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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.assist.Caption

fun BroadcastCaption(
    storyBroadcaster: MutableState<StoryBroadcaster?>,
    reactionListener: MutableState<ReactionListener?>,
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
                        storyBroadcaster.value = if (storyBroadcaster.value == null) StoryBroadcaster() else null
                        reactionListener.value = if (reactionListener.value == null) ReactionListener() else null
                    },
                ) {
                    Icon(
                        imageVector = if (storyBroadcaster.value != null) Icons.AutoMirrored.Rounded.Send else Icons.Rounded.Warning,
                        contentDescription = "Broadcasting",
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    )
                }
            }

            ReactionGraph(reactionListener.value)
        }
    }
}
