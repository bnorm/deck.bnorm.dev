package dev.bnorm.kc26.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.storyboard.StoryboardBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job

fun StoryboardBuilder.Title() {
    carouselScene {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                Column(Modifier.padding(16.dp)) {
                    ProvideTextStyle(MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)) {
                        Text("Powering Up") // TODO lightning emoji?
                        Text("Your Assertions") // TODO magnifying glass emoji?
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
