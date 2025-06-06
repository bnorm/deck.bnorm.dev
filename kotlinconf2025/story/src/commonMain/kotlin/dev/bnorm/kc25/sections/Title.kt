package dev.bnorm.kc25.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.start_conference
import dev.bnorm.kc25.components.KotlinConfBird
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.LocalStoryboard
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.Title() {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = KotlinConfBird(transition.createChildTransition { it is Frame.State }),
                contentDescription = "",
                modifier = Modifier.size(548.dp).offset(416.dp, 0.dp),
            )

            val storyboard = LocalStoryboard.current!!
            val xSize = storyboard.format.size.width.toFloat()
            val ySize = storyboard.format.size.height.toFloat()
            Box(
                Modifier.fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            start = Offset(xSize * 0.9f, ySize * 0.5f),
                            end = Offset(xSize, ySize),
                        )
                    )
            )

            Image(
                painter = painterResource(Res.drawable.start_conference),
                contentDescription = "",
                modifier = Modifier.size(247.dp, 26.dp).offset(40.dp, 40.dp),
            )

            Column(
                Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                Column(Modifier.padding(16.dp)) {
                    ProvideTextStyle(MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)) {
                        Text("Writing Your Third")
                        Text("Kotlin Compiler Plugin")
                    }
                }
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(16.dp),
                    ) {
                        JetBrainsEmployee(
                            name = "Brian Norman",
                            title = "Kotlin Compiler Developer",
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Mastodon(username = "bnorm@kotlin.social")
                        Spacer(Modifier.size(4.dp))
                        Bluesky(username = "@bnorm.dev")
                    }
                }
            }
        }
    }
}
