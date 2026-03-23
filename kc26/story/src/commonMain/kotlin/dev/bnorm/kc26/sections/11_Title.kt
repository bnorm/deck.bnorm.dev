package dev.bnorm.kc26.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.ResourceImage
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.conf_logo
import dev.bnorm.deck.story.generated.resources.conf_title
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Title() {
    carouselScene {
        Box(Modifier.fillMaxSize()) {
            ResourceImage(
                Res.drawable.conf_title,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(vertical = 42.dp, horizontal = 39.dp)
                    .height(40.dp)
            )
            ResourceImage(
                Res.drawable.conf_logo,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(vertical = 40.dp, horizontal = 32.dp)
            )
            Column(
                Modifier.fillMaxWidth()
                    .padding(32.dp)
                    .align(Alignment.BottomStart)
            ) {
                Column(Modifier.padding(0.dp)) {
                    ProvideTextStyle(
                        MaterialTheme.typography.h2.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 60.sp,
                        )
                    ) {
                        Text("Powering\nUp Your\nAssertions")
                    }
                    JetBrainsEmployee(
                        name = "Brian Norman",
                        title = "Kotlin Compiler Developer",
                    )
                }
            }
        }
    }
}
