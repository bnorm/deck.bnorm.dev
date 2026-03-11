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
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Closing() {
    carouselScene {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.align(Alignment.TopStart).padding(32.dp)) {
                Mastodon(username = "bnorm@kotlin.social")
                Spacer(Modifier.size(4.dp))
                Bluesky(username = "@bnorm.dev")
            }
            Column(Modifier.align(Alignment.BottomStart).padding(32.dp)) {
                ProvideTextStyle(MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)) {
                    Text("Thank You,")
                    Text("and Don't")
                    Text("Forget to Vote")
                }
            }
        }
    }
}
