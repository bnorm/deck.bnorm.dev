package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.background_building
import dev.bnorm.deck.story.generated.resources.droidcon_newyork
import dev.bnorm.deck.story.generated.resources.twitter
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.LocalStoryboard
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.toDpSize
import dev.bnorm.storyboard.toState
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.Opening() {
    scene(
        stateCount = 3,
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {
        val offset = transition.createChildTransition { it.toState() }

        val scrollState = rememberScrollState()
        offset.animateScroll(scrollState, transitionSpec = { tween(durationMillis = 750) }) {
            with(LocalDensity.current) { (SceneHalfHeight * it).roundToPx() }
        }

        Column(Modifier.verticalScroll(scrollState, enabled = false)) {
            Horizontal(MaterialTheme.colors.primary) {
                TitleHeader()
            }

            Horizontal(MaterialTheme.colors.secondary) {
                TitleContent()
            }

            Full(MaterialTheme.colors.primary) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(Res.drawable.twitter),
                        contentDescription = "IntelliJ",
                        alignment = Alignment.TopStart,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.clip(RoundedCornerShape(16.dp))
                    )
                }
            }
        }
    }

    scene(
        stateCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {
        Full(MaterialTheme.colors.secondary) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val storyboard = buildAnnotatedString {
                        append("and created ")
                        withStyle(SpanStyle(fontWeight = EmphasisWeight)) { append("Storyboard") }
                        append("!")
                    }

                    Row {
                        OutlinedText("Well…", style = MaterialTheme.typography.h2)
                        Reveal(step = 1) {
                            OutlinedText(" I did it anyways", style = MaterialTheme.typography.h2)
                        }
                    }

                    Reveal(step = 2) {
                        OutlinedText(storyboard, style = MaterialTheme.typography.h2)
                    }

                    Reveal(step = 3) {
                        OutlinedText("(and it's not really that interesting)", style = MaterialTheme.typography.h5)
                    }
                }
            }
        }
    }

    scene(
        stateCount = 3,
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.BottomCenter),
            end = SceneEnter(alignment = Alignment.CenterEnd),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.BottomCenter),
            end = SceneExit(alignment = Alignment.CenterEnd),
        ),
    ) {
        Full(MaterialTheme.colors.primary) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val interesting = buildAnnotatedString {
                        append("But what ")
                        withStyle(SpanStyle(fontWeight = EmphasisWeight)) { append("is") }
                        append(" interesting")
                    }
                    val move = buildAnnotatedString {
                        append("is a Compose ")
                        withStyle(SpanStyle(fontWeight = EmphasisWeight)) { append("Magic Move") }
                        append("!")
                    }
                    val text = buildAnnotatedString {
                        append("(well, more like ")
                        withStyle(SpanStyle(fontWeight = EmphasisWeight)) { append("MagicText") }
                        append(")")
                    }

                    Reveal(step = 0) {
                        OutlinedText(interesting, style = MaterialTheme.typography.h2)
                    }
                    Reveal(step = 1) {
                        OutlinedText(move, style = MaterialTheme.typography.h2)
                    }
                    Reveal(step = 2) {
                        OutlinedText(text, style = MaterialTheme.typography.h5)
                    }
                }
            }
        }
    }
}

@Composable
fun TitleHeader() {
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

@Composable
private fun TitleContent() {
    Row {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier.fillMaxHeight().weight(1f).padding(24.dp)
        ) {
            OutlinedText(
                text = buildAnnotatedString {
                    append("re")
                    withStyle(SpanStyle(fontWeight = EmphasisWeight)) {
                        append("creating\nmagic")
                    }
                    append("move\nwith compose")
                },
                style = MaterialTheme.typography.h2,
            )
        }
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxHeight().weight(1f).padding(24.dp),
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

@Composable
context(scope: SceneScope<Int>)
private fun ColumnScope.Reveal(step: Int, content: @Composable () -> Unit) {
    scope.transition.AnimatedVisibility(
        visible = { it.toState() >= step },
        enter = expandVertically(tween(750), clip = false, expandFrom = Alignment.Top) + fadeIn(tween(750)),
        exit = shrinkVertically(tween(750), clip = false, shrinkTowards = Alignment.Top) + fadeOut(tween(750)),
    ) {
        content()
    }
}

@Composable
context(scope: SceneScope<Int>)
private fun RowScope.Reveal(step: Int, content: @Composable () -> Unit) {
    scope.transition.AnimatedVisibility(
        visible = { it.toState() >= step },
        enter = expandHorizontally(tween(750), clip = false, expandFrom = Alignment.Start) + fadeIn(tween(750)),
        exit = shrinkHorizontally(tween(750), clip = false, shrinkTowards = Alignment.Start) + fadeOut(tween(750)),
    ) {
        content()
    }
}
