package dev.bnorm.deck.kc25.companion.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.kc25.companion.utils.rememberMaxIndex
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.easel.Animatic
import dev.bnorm.storyboard.easel.Easel
import dev.bnorm.storyboard.easel.overlay.EaselOverlay
import dev.bnorm.storyboard.easel.overlay.EaselOverlayScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun EmbeddedStory(animatic: Animatic) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Slides", style = MaterialTheme.typography.h2)
        Spacer(Modifier.height(16.dp))

        // TODO do we need to disable key events? doesn't seem like it...
        EaselOverlay(
            overlay = { OverlayNavigation(animatic) }
        ) {
            Easel(animatic)
        }
    }
}

@Composable
private fun EaselOverlayScope.OverlayNavigation(
    animatic: Animatic,
    alignment: Alignment = Alignment.BottomCenter,
) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    val indices = animatic.storyboard.indices

    val maxIndex = rememberMaxIndex(animatic)

    Surface(
        modifier = Modifier
            .align(alignment)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .overlayElement()
    ) {
        Row {
            IconButton(
                text = "Previous",
                icon = Icons.AutoMirrored.Rounded.ArrowBack,
                enabled = animatic.targetIndex > indices.first(),
                onClick = {
                    job?.cancel()
                    job = coroutineScope.launch {
                        animatic.advance(AdvanceDirection.Backward)
                        job = null
                    }
                }
            )
            IconButton(
                text = "Next",
                icon = Icons.AutoMirrored.Rounded.ArrowForward,
                enabled = animatic.targetIndex < indices.last() && animatic.targetIndex < maxIndex.value,
                onClick = {
                    job?.cancel()
                    job = coroutineScope.launch {
                        animatic.advance(AdvanceDirection.Forward)
                        job = null
                    }
                }
            )
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
