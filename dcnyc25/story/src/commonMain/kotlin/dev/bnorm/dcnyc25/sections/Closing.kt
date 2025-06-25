package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.alex
import dev.bnorm.deck.story.generated.resources.jesse
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource

fun StoryboardBuilder.Closing() {
    scene(
        states = listOf(
            0 to 0,
            1 to 1,
            2 to 2,
            2 to 3,
            2 to 4,
            3 to 5,
            3 to 6,
            3 to 7,
        ),
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {
        val offset = transition.createChildTransition { it.toState().first }
        val step = transition.createChildTransition { it.toState().second }

        val scrollState = rememberScrollState()
        offset.animateScroll(scrollState, transitionSpec = { tween(durationMillis = 750) }) {
            with(LocalDensity.current) { (SceneHalfHeight * it).roundToPx() }
        }

        Column(Modifier.verticalScroll(scrollState, enabled = false)) {
            Horizontal(MaterialTheme.colors.primary) {
                Jesse()
            }
            Horizontal(MaterialTheme.colors.secondary) {
                Alex()
            }
            Horizontal(MaterialTheme.colors.primary) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    OutlinedText("In summary:", style = MaterialTheme.typography.h1)
                }
            }
            Horizontal(MaterialTheme.colors.secondary) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedText("Write a diff algorithm.", style = MaterialTheme.typography.h2)
                        step.Reveal(step = 3) {
                            OutlinedText("It's fun!", style = MaterialTheme.typography.h3)
                        }
                        step.Reveal(step = 4) {
                            OutlinedText("(and you'll probably learn something)", style = MaterialTheme.typography.h5)
                        }
                    }
                }
            }
            Horizontal(MaterialTheme.colors.primary) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedText("Fin", style = MaterialTheme.typography.h1)
                        step.Reveal(step = 6) {
                            OutlinedText(
                                text = "(github.com/bnorm/storyboard)",
                                style = MaterialTheme.typography.code1
                            )
                        }
                        step.Reveal(step = 7) {
                            OutlinedText(
                                text = "(deck.bnorm.dev/dcnyc25)",
                                style = MaterialTheme.typography.code2,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Transition<Int>.Reveal(step: Int, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = { it >= step },
        enter = expandVertically(tween(750), clip = false, expandFrom = Alignment.Top) + fadeIn(tween(750)),
        exit = shrinkVertically(tween(750), clip = false, shrinkTowards = Alignment.Top) + fadeOut(tween(750)),
    ) {
        content()
    }
}

@Composable
private fun Jesse() {
    DroidconTalk(
        head = Res.drawable.jesse,
        title = "Coroutines Party Tricks",
        speaker = "Jesse Wilson",
        time = "Today at 1:25pm"
    )
}

@Composable
private fun Alex() {
    DroidconTalk(
        head = Res.drawable.alex,
        title = "Handling configuration\nchanges in Compose",
        speaker = "Alex Vanyo",
        time = "Tomorrow at 2:35pm"
    )
}

@Composable
private fun DroidconTalk(
    head: DrawableResource,
    title: String,
    speaker: String,
    time: String,
) {
    val titleStyle =
        MaterialTheme.typography.h4.copy(fontSize = 30.sp, lineHeight = 32.sp, fontWeight = FontWeight.SemiBold)
    val speakerStyle = MaterialTheme.typography.h5
    val timeStyle = MaterialTheme.typography.h6.copy(fontStyle = FontStyle.Italic)

    val titleLines = remember(title) {
        buildList {
            val lines = title.lines()
            repeat(2 - lines.size) {
                add("")
            }
            addAll(lines)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.padding(48.dp),
    ) {
        Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            Image(
                imageResource(head),
                contentDescription = null,
                modifier = Modifier.clip(CircleShape)
            )
        }
        Box(Modifier.weight(2f), contentAlignment = Alignment.CenterStart) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Column {
                    for (line in titleLines) {
                        OutlinedText(line, style = titleStyle)
                    }
                }
                OutlinedText(speaker, style = speakerStyle)
                OutlinedText(time, style = timeStyle)
            }
        }
    }
}
