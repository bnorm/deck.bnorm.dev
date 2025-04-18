package dev.bnorm.kc25.sections.intro

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.LocalSceneMode
import dev.bnorm.storyboard.SceneMode
import dev.bnorm.storyboard.easel.assist.SceneCaption
import dev.bnorm.storyboard.easel.template.StoryEffect
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun animateSampleIndex(
    samples: List<CodeSample>,
    previewIndex: Int = samples.size - 1,
    defaultDelay: Duration = 3.seconds,
): State<Int> {
    val size = samples.size

    // When rendering the scene for preview, render the finished state and do not animate the sample.
    val isStory = LocalSceneMode.current == SceneMode.Story
    var playing by remember { mutableStateOf(isStory) }
    val index = remember(size) { mutableIntStateOf(if (isStory) 0 else previewIndex) }

    SceneCaption {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Playing")
            Spacer(Modifier.width(16.dp))
            Switch(playing, onCheckedChange = { playing = it })
            if (!playing) {
                IconButton(onClick = {
                    index.value = (if (index.value <= 0) size else index.value) - 1
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Previous",
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    )
                }
                IconButton(onClick = {
                    index.value = (index.value + 1) % size
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Next",
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    )
                }
            }
        }
    }

    StoryEffect(playing) {
        while (playing) {
            val data = samples[index.value].data
            delay(data as? Duration ?: defaultDelay)
            index.value = (index.value + 1) % size
        }
    }

    return index
}
