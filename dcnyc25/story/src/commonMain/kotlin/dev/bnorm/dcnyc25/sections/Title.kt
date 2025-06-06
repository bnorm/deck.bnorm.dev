package dev.bnorm.dcnyc25.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.template.Pane
import dev.bnorm.dcnyc25.template.Panes
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.background_building
import dev.bnorm.deck.story.generated.resources.droidcon_newyork
import dev.bnorm.storyboard.easel.LocalStoryboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toDpSize
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.Title() {
    val panes = listOf<Pane.Horizontal<Int>>(
        Pane.Horizontal {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
                Row {
                    Image(
                        painter = painterResource(Res.drawable.droidcon_newyork),
                        contentDescription = "title",
                        modifier = Modifier.width(LocalStoryboard.current!!.format.toDpSize().width / 2)
                            .padding(32.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource(Res.drawable.background_building),
                        contentDescription = "building",
                    )
                }
            }
        },
        Pane.Horizontal(
            Pane.Quarter {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.secondary) {
                    Box(
                        contentAlignment = Alignment.BottomStart,
                        modifier = Modifier.fillMaxSize().padding(24.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                                    this.append("re")
                                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                        this.append("creating\nmagic")
                                    }
                                    this.append("move\nwith compose")
                                }
                            },
                            style = MaterialTheme.typography.h2,
                        )
                    }
                }
            },
            Pane.Quarter {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.secondary) {
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                    ) {
                        Column {
                            JetBrainsEmployee(
                                name = "Brian Norman",
                                title = "Kotlin Compiler Developer",
                            )
                            Spacer(Modifier.size(4.dp))
                            Column(Modifier.padding(start = 12.dp)) {
                                Mastodon(username = "bnorm@kotlin.social")
                                Spacer(Modifier.size(4.dp))
                                Bluesky(username = "@bnorm.dev")
                            }
                        }
                    }
                }
            }
        ),
    )

    scene(
        stateCount = panes.size - 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Panes(panes)
    }
}
