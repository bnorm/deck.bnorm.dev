package dev.bnorm.kc25.sections.plugin

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.buildable_repo_qr
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.Repository() {
    scene(
        stateCount = 3,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("WARNING!", style = MaterialTheme.typography.h2)
                Spacer(Modifier.height(16.dp))

                frame.AnimatedVisibility(
                    visible = { it.toState() >= 1 },
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("We're about to look at a lot of code.", style = MaterialTheme.typography.h4)
                        Spacer(Modifier.height(16.dp))
                    }
                }

                frame.AnimatedVisibility(
                    visible = { it.toState() >= 2 },
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(Res.drawable.buildable_repo_qr),
                            contentDescription = "",
                            modifier = Modifier.size(200.dp, 200.dp).background(Color.White),
                        )
                        Spacer(Modifier.height(16.dp))

                        Text(buildAnnotatedString {
                            val url = "https://github.com/bnorm/buildable"
                            val linkStyles = TextLinkStyles(
                                style = SpanStyle(textDecoration = TextDecoration.Underline),
                            )
                            withLink(LinkAnnotation.Url(url, linkStyles)) {
                                append(url)
                            }
                        })
                    }
                }
            }
        }
    }
}