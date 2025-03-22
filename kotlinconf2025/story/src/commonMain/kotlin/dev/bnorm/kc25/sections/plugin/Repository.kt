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
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.Repository() {
    slide(
        stateCount = 3,
        enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
        exitTransition = SlideExit(alignment = Alignment.CenterEnd),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("WARNING!", style = MaterialTheme.typography.h2)
                Spacer(Modifier.height(16.dp))

                state.AnimatedVisibility(
                    visible = { it.toState() >= 1 },
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("We're about to look at a lot of code.", style = MaterialTheme.typography.h4)
                        Spacer(Modifier.height(16.dp))
                    }
                }

                state.AnimatedVisibility(
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
                            val link = LinkAnnotation.Url(
                                "https://github.com/bnorm/buildable",
                                TextLinkStyles(SpanStyle(textDecoration = TextDecoration.Underline))
                            )
                            withLink(link) {
                                append("https://github.com/bnorm/buildable")
                            }
                        })
                    }
                }
            }
        }
    }
}