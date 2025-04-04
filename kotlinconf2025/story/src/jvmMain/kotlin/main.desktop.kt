package dev.bnorm.kc25.story.desktop

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import dev.bnorm.kc25.broadcast.Broadcaster
import dev.bnorm.kc25.broadcast.LocalBroadcaster
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.storyboard.core.ExperimentalStoryStateApi
import dev.bnorm.storyboard.core.rememberStoryState
import dev.bnorm.storyboard.easel.DesktopStory
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import org.jetbrains.compose.reload.DevelopmentEntryPoint

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    application {
        val state = rememberStoryState()
        var broadcaster by remember { mutableStateOf<Broadcaster?>(null) }
        DevelopmentEntryPoint {
            remember { createStoryboard().also { state.updateStoryboard(it) } }
            MaterialTheme(colors = darkColors()) {
                CompositionLocalProvider(LocalBroadcaster provides broadcaster) {
                    DesktopStory(
                        storyState = state,
                        overlay = {
                            OverlayNavigation(state)
                            OverlayBroadcasting(
                                enabled = broadcaster != null,
                                onClick = {
                                    broadcaster = if (broadcaster != null) null else Broadcaster()
                                },
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StoryOverlayScope.OverlayBroadcasting(
    enabled: Boolean,
    onClick: () -> Unit,
    alignment: Alignment = Alignment.TopStart,
) {
    Surface(
        modifier = Modifier
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
            modifier = Modifier
                .pointerHoverIcon(PointerIcon.Hand),
        )
    }
}
