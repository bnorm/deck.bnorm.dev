package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.template.SceneHalfHeight
import dev.bnorm.dcnyc25.template.SceneHeight
import dev.bnorm.dcnyc25.template.SceneWidth
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.github_review
import dev.bnorm.deck.story.generated.resources.intellij
import dev.bnorm.deck.story.generated.resources.keynote
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.Opening() {
    scene(
        stateCount = 5,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        val offset by transition.animateDp(transitionSpec = { tween(durationMillis = 750) }) {
            SceneHalfHeight * when (it.toState()) {
                0 -> 0
                else -> 1
            }
        }

        val questionHeight by transition.animateDp(transitionSpec = { tween(durationMillis = 750) }) {
            SceneHalfHeight * when (it.toState()) {
                0 -> 1f
                else -> 0.5f
            }
        }

        Column(
            Modifier
                .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                .offset(y = -offset)
        ) {
            TitleHeader()

            HowManyCode(questionHeight, yesPercent = 0.8f)

            Surface(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .width(SceneWidth)
                    .height(SceneHeight - questionHeight)
                    .sharedElement(rememberSharedContentState("opening-panels"))
            ) {
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize()) {
                    val padding = 16.dp
                    val eachWidth = (SceneWidth - padding * 5f) / 3f

                    Box(Modifier.width(eachWidth).fillMaxHeight().padding(vertical = padding)) {
                        transition.AnimatedVisibility(
                            visible = { it.toState() >= 2 },
                            enter = fadeIn(tween(750)), exit = fadeOut(tween(750)),
                        ) {
                            IntelliJPanel {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Text("Editor", style = MaterialTheme.typography.h2)
                                }
                            }
                        }
                    }
                    Box(Modifier.width(eachWidth).fillMaxHeight().padding(vertical = padding)) {
                        transition.AnimatedVisibility(
                            visible = { it.toState() >= 3 },
                            enter = fadeIn(tween(750)), exit = fadeOut(tween(750)),
                        ) {
                            GitHubPanel {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Text("Review", style = MaterialTheme.typography.h2)
                                }
                            }
                        }
                    }
                    Box(Modifier.width(eachWidth).fillMaxHeight().padding(vertical = padding)) {
                        transition.AnimatedVisibility(
                            visible = { it.toState() >= 4 },
                            enter = fadeIn(tween(750)), exit = fadeOut(tween(750)),
                        ) {
                            // TODO backgrounds
                            KeynotePanel {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Text("Slides", style = MaterialTheme.typography.h2)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // fade each to talk about pros and cons:
    // - context : familiar code for editor and review / unfamiliar for presentations
    // - format : can adjust code style for editor and review / no choice for presentations
    // - flow : have time to review for editor and review / no time to review for presentations

    // TODO use MagicText to change header instead of carousel?
    // TODO add a summary for each question to each panel
    Question(buildAnnotatedString {
        append("What has good ")
        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
            append("context")
        }
        append("?")
    }, editorPercent = 0.8f, reviewPercent = 0.6f, slidesPercent = 0.2f)

    Question(buildAnnotatedString {
        append("What has good ")
        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
            append("styling")
        }
        append("?")
    }, editorPercent = 0.8f, reviewPercent = 0.2f, slidesPercent = 0.2f)

    Question(buildAnnotatedString {
        append("What has sufficient ")
        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
            append("time")
        }
        append("?")
    }, editorPercent = 0.8f, reviewPercent = 0.8f, slidesPercent = 0.2f)

    // DIALOGUE
    //
    // Presentations are hard
    // because they need to show code
    // and often how it changes.
    //
    // This is why I started coding my presentations,
    // to try and solve these exact problems.
}

@Composable
private fun SceneScope<*>.HowManyCode(questionHeight: Dp, yesPercent: Float? = null) {

    Surface(
        color = MaterialTheme.colors.secondary,
        modifier = Modifier.width(SceneWidth).height(questionHeight)
    ) {
        VoteRow(transition.createChildTransition { it is Frame.State }, yesPercent)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text("How many of you look at code?", style = MaterialTheme.typography.h2)
        }
    }
}

private fun StoryboardBuilder.Question(
    question: AnnotatedString,
    editorPercent: Float? = null,
    reviewPercent: Float? = null,
    slidesPercent: Float? = null,
) {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Column {
            Surface(
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.width(SceneWidth).height(SceneHeight * 0.25f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        question,
                        style = MaterialTheme.typography.h2,
                    )
                }
            }

            Surface(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .width(SceneWidth)
                    .height(SceneHeight * 0.75f)
                    .sharedElement(rememberSharedContentState("opening-panels"))
            ) {
                // TODO backgrounds
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize()) {
                    val padding = 16.dp
                    val eachWidth = (SceneWidth - padding * 5f) / 3f

                    Box(Modifier.width(eachWidth).fillMaxHeight().padding(vertical = padding)) {
                        IntelliJPanel {
                            VoteColumn(transition.createChildTransition { it is Frame.State }, editorPercent)
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text("Editor", style = MaterialTheme.typography.h2)
                            }
                        }
                    }
                    Box(Modifier.width(eachWidth).fillMaxHeight().padding(vertical = padding)) {
                        GitHubPanel {
                            VoteColumn(transition.createChildTransition { it is Frame.State }, reviewPercent)
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text("Review", style = MaterialTheme.typography.h2)
                            }
                        }
                    }
                    Box(Modifier.width(eachWidth).fillMaxHeight().padding(vertical = padding)) {
                        KeynotePanel {
                            VoteColumn(transition.createChildTransition { it is Frame.State }, slidesPercent)
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text("Slides", style = MaterialTheme.typography.h2)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IntelliJPanel(content: @Composable () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.intellij),
            contentDescription = "IntelliJ",
            alignment = Alignment.TopStart,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.alpha(0.5f)
        )
        content()
    }
}

@Composable
private fun GitHubPanel(content: @Composable () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.github_review),
            contentDescription = "GitHub Review",
            alignment = Alignment.TopStart,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.alpha(0.5f)
        )
        content()
    }
}

@Composable
private fun KeynotePanel(content: @Composable () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.keynote),
            contentDescription = "Keynote",
            alignment = Alignment.TopStart,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.alpha(0.5f)
        )
        content()
    }
}

private val YesColor = Color.White
private val NoColor = Color.Black

@Composable
private fun VoteRow(visible: Transition<Boolean>, percent: Float?) {
    Box(Modifier.fillMaxSize().alpha(0.5f)) {
        visible.AnimatedVisibility(
            visible = { it && percent != null },
            enter = fadeIn(tween(750)),
            exit = fadeOut(tween(750))
        ) {
            val percent by animateFloatAsState(percent ?: 0.5f)
            Row(Modifier.fillMaxSize()) {
                if (percent > 0f) {
                    Box(
                        Modifier.fillMaxHeight().weight(percent).background(
                            Brush.horizontalGradient(
                                0f to YesColor.copy(alpha = 0f),
                                1f to YesColor.copy(alpha = 1f),
                            )
                        )
                    )
                }
                if (percent < 1f) {
                    Box(
                        Modifier.fillMaxHeight().weight(1f - percent).background(
                            Brush.horizontalGradient(
                                0f to NoColor.copy(alpha = 1f),
                                1f to NoColor.copy(alpha = 0f),
                            )
                        )
                    )
                }
            }
            Row(Modifier.fillMaxSize()) {
                Box(Modifier.fillMaxHeight().weight(1f), contentAlignment = Alignment.CenterStart) {
                    Text("Yes", modifier = Modifier.padding(12.dp))
                }
                Box(Modifier.fillMaxHeight().weight(1f), contentAlignment = Alignment.CenterEnd) {
                    Text("No", modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

@Composable
private fun VoteColumn(visible: Transition<Boolean>, percent: Float?) {
    Box(Modifier.fillMaxSize().alpha(0.5f)) {
        visible.AnimatedVisibility(
            visible = { it && percent != null },
            enter = fadeIn(tween(750)),
            exit = fadeOut(tween(750))
        ) {
            val percent by animateFloatAsState(percent ?: 0.5f)
            Column(Modifier.fillMaxSize()) {
                if (percent < 1f) {
                    Box(
                        Modifier.fillMaxWidth().weight(1f - percent).background(
                            Brush.verticalGradient(
                                0f to NoColor.copy(alpha = 0f),
                                1f to NoColor.copy(alpha = 1f),
                            )
                        )
                    )
                }
                if (percent > 0f) {
                    Box(
                        Modifier.fillMaxWidth().weight(percent).background(
                            Brush.verticalGradient(
                                0f to YesColor.copy(alpha = 1f),
                                1f to YesColor.copy(alpha = 0f),
                            )
                        )
                    )
                }
            }
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.TopCenter) {
                    Text("No", modifier = Modifier.padding(12.dp))
                }
                Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.BottomCenter) {
                    Text("Yes", modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}
