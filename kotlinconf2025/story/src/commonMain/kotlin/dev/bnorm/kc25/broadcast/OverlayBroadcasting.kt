package dev.bnorm.kc25.broadcast

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope

@Composable
fun StoryOverlayScope.OverlayBroadcasting(
    enabled: Boolean,
    onClick: () -> Unit,
    alignment: Alignment = Alignment.Companion.TopStart,
) {
    Surface(
        modifier = Modifier.Companion
            .align(alignment)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .overlayElement()
    ) {
        IconButton(
            text = "Broadcasting",
            icon = if (enabled) Icons.AutoMirrored.Rounded.Send else Icons.Rounded.Warning,
            onClick = onClick
        )
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
            modifier = Modifier.Companion
                .pointerHoverIcon(PointerIcon.Companion.Hand),
        )
    }
}
