package dev.bnorm.dcnyc25

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.dcnyc25.template.storyDecorator
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.background_building
import dev.bnorm.deck.story.generated.resources.droidcon_newyork
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState
import org.jetbrains.compose.resources.painterResource

fun createStoryboard(
    decorator: SceneDecorator = storyDecorator(),
): Storyboard = Storyboard.build(
    title = "(Re)creating Magic(Move) with Compose",
    description = """
        Presentation software is extremely powerful, full of creative styling, powerful animations, and plenty of
        other things. Yet every time I wrote a new presentation, I found myself repeating something with almost
        every slide; whether that be the content, styling, or animations. Wanting the torment to end (and a
        different kind of torment to begin), I wrote my own presentation framework with Compose!

        But wanting to be like all the cool presenters, I needed something like Keynote’s Magic Move for all my code
        examples! Compose has SharedTransitionLayout, how hard could this be? So I built my own version of Magic
        Move. And rebuilt it. And rebuilt it again. And Again. (And probably again between submitting and actually
        giving this talk) Let’s look at all those different iterations, the improvements each brought, and why
        diffing algorithms are hard.
    """.trimIndent(),
    decorator = decorator,
) {
    Title()
}

private fun StoryboardBuilder.Title() {
    scene(
        states = listOf(
            FontWeight.ExtraLight.weight,
            FontWeight.SemiBold.weight,
        ),
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
            ) {
                Image(
                    painter = painterResource(Res.drawable.droidcon_newyork),
                    contentDescription = "title",
                    modifier = Modifier.width(Storyboard.DEFAULT_SIZE.width / 2).padding(32.dp)
                )
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(Res.drawable.background_building),
                    contentDescription = "build",
                    modifier = Modifier
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.secondary)
                    .padding(24.dp)
            ) {
                CompositionLocalProvider(LocalContentColor provides Color(0xFFF8F8F8)) {
                    val base = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.ExtraLight)
                    val fontWeight by transition.animateInt(transitionSpec = { tween(500) }) { it.toState() }
                    Column {
                        Row {
                            Text("re", style = base)
                            Text("creating", style = base.copy(fontWeight = FontWeight(fontWeight)))
                        }
                        Row {
                            Text("magic", style = base.copy(fontWeight = FontWeight(fontWeight)))
                            Text("move", style = base)
                        }
                        Text("with compose", style = base)
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                    ) {
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
    }
}

@Composable
fun <S> Transition<S>.animateTextStyle(
    transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Float> = { spring() },
    label: String = "TextStyleAnimation",
    targetValueByState: @Composable (state: S) -> TextStyle,
): State<TextStyle> {
    val fontSize = animateFloat(transitionSpec, "$label:fontSize") {
        targetValueByState(it).fontSize.value
    }
    val lineHeight = animateFloat(transitionSpec, "$label:lineHeight") {
        targetValueByState(it).lineHeight.value
    }
    val letterSpacing = animateFloat(transitionSpec, "$label:letterSpacing") {
        targetValueByState(it).letterSpacing.value
    }
    val fontWeight = animateFloat(transitionSpec, "$label:letterSpacing") {
        targetValueByState(it).fontWeight?.weight?.toFloat() ?: 0f
    }
    return derivedStateOf {
        TextStyle(
            fontSize = fontSize.value.sp,
            lineHeight = lineHeight.value.sp,
            letterSpacing = letterSpacing.value.sp,
            fontWeight = FontWeight(fontWeight.value.toInt()),
        )
    }
}
