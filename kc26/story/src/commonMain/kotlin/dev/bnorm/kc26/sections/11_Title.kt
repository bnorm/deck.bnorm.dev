package dev.bnorm.kc26.sections

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.ResourceImage
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.conf_logo
import dev.bnorm.deck.story.generated.resources.conf_title
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.template.Kc26Colors
import dev.bnorm.kc26.template.gradientOverlay
import dev.bnorm.kc26.template.title
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Title() {
    scene(
        enterTransition = { fadeIn(animationSpec = tween(750)) },
        exitTransition = { fadeOut(animationSpec = tween(750)) },
    ) {
        Surface(
            border = BorderStroke(2.dp, Kc26Colors.colorGradient),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
        ) {
            TitleBackground()
            TitleScaffold {
                Box {
                    Text("Powering")
                    GradientText("Power")
                }
                Text("Up Your")
                Box {
                    Text("Assertions")
                    GradientText("Assert")
                }
            }
        }
    }
}

@Composable
fun TitleScaffold(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 64.dp)
        ) {
            ProvideTextStyle(MaterialTheme.typography.title) {
                Column(verticalArrangement = Arrangement.spacedBy((-8).dp)) {
                    title()
                }
            }
        }
    }
}


@Composable
fun TitleBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        ResourceImage(
            Res.drawable.conf_title,
            modifier = Modifier
                .align(Alignment.TopStart)
                .height(40.dp)
        )
        ResourceImage(
            Res.drawable.conf_logo,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .gradientOverlay()
        )
        JetBrainsEmployee(
            name = "Brian Norman",
            title = "Kotlin Compiler Developer",
            modifier = Modifier
                .align(Alignment.BottomStart)
        )
    }
}
