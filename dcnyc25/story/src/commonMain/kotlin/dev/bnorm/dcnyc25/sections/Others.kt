package dev.bnorm.dcnyc25.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.dcnyc25.template.Horizontal
import dev.bnorm.dcnyc25.template.OutlinedText
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.alex
import dev.bnorm.deck.story.generated.resources.jesse
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import org.jetbrains.compose.resources.imageResource

fun StoryboardBuilder.Others() {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {
        val titleStyle =
            MaterialTheme.typography.h4.copy(fontSize = 30.sp, lineHeight = 32.sp, fontWeight = FontWeight.SemiBold)
        val speakerStyle = MaterialTheme.typography.h5
        val timeStyle = MaterialTheme.typography.h6.copy(fontStyle = FontStyle.Italic)

        Column {
            Horizontal(MaterialTheme.colors.primary) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(48.dp),
                ) {
                    Image(
                        imageResource(Res.drawable.jesse),
                        contentDescription = null,
                        modifier = Modifier.clip(CircleShape)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column {
                            OutlinedText("", style = titleStyle)
                            OutlinedText("Coroutines Party Tricks", style = titleStyle)
                        }
                        OutlinedText("Jesse Wilson", style = speakerStyle)
                        OutlinedText("Today at 1:25pm", style = timeStyle)
                    }
                    Spacer(Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxHeight().aspectRatio(16f / 9f)
                            .border(2.dp, Color.White),
                    ) {
                        Text("(title slide here)")
                    }
                }
            }
            Horizontal(MaterialTheme.colors.secondary) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(48.dp),
                ) {
                    Image(
                        imageResource(Res.drawable.alex),
                        contentDescription = null,
                        modifier = Modifier.clip(CircleShape)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column {
                            OutlinedText("Handling configuration", style = titleStyle)
                            OutlinedText("changes in Compose", style = titleStyle)
                        }
                        OutlinedText("Alex Vanyo", style = speakerStyle)
                        OutlinedText("Tomorrow at 2:35pm", style = timeStyle)
                    }
                    Spacer(Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxHeight().aspectRatio(16f / 9f)
                            .border(2.dp, Color.White),
                    ) {
                        Text("(title slide here)")
                    }
                }
            }
        }
    }
    // Jesse Wilson
    // Coroutines Party Tricks

    // Alex Vanyo
    // Handling configuration changes in Compose
}
