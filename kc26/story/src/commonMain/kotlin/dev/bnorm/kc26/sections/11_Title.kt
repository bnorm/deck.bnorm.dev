package dev.bnorm.kc26.sections

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.ResourceImage
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.conf_logo
import dev.bnorm.deck.story.generated.resources.conf_title
import dev.bnorm.kc26.template.gradientBackground
import dev.bnorm.kc26.template.gradientOverlay
import dev.bnorm.kc26.template.title
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement

private enum class SharedTitleElements {
    Power,
    Assert,
}

private const val FadeDuration = 500
private const val TransformDuration = 500

private fun FadeSceneEnterTransition(): SceneEnterTransition = { _ ->
    fadeIn(
        tween(
            durationMillis = FadeDuration,
            delayMillis = 0,
            easing = EaseInOut
        )
    )
}

private fun FadeSceneExitTransition(): SceneExitTransition = {
    fadeOut(
        tween(
            durationMillis = FadeDuration,
            delayMillis = 0,
            easing = EaseInOut,
        )
    )
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun SharedPower(modifier: Modifier = Modifier) {
    Text(
        text = "Power",
        modifier = modifier.sharedElement(
            sharedContentState = rememberSharedContentState(SharedTitleElements.Power),
            boundsTransform = { _, _ ->
                tween(
                    durationMillis = TransformDuration,
                    delayMillis = FadeDuration,
                    easing = EaseInOut
                )
            },
        ).gradientOverlay(),
    )
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun SharedAssert(modifier: Modifier = Modifier) {
    Text(
        text = "Assert",
        modifier = modifier.sharedElement(
            sharedContentState = rememberSharedContentState(SharedTitleElements.Assert),
            boundsTransform = { _, _ ->
                tween(
                    durationMillis = TransformDuration,
                    delayMillis = FadeDuration,
                    easing = EaseInOut
                )
            },
        ).gradientOverlay(),
    )
}

fun StoryboardBuilder.Title() {
    scene(
        frameCount = 1,
        enterTransition = FadeSceneEnterTransition(),
        exitTransition = FadeSceneExitTransition(),
    ) {
        val alpha = transition.animateFloat(
            transitionSpec = { tween(durationMillis = 750, easing = EaseInOut) },
        ) {
            when (it.toValue()) {
                0 -> 1f
                else -> 0.25f
            }
        }

        TitleBackground(Modifier.graphicsLayer { this.alpha = alpha.value })
        TitleScaffold(Modifier.graphicsLayer { this.alpha = alpha.value }) {
            Row { Text("Powering") }
            Text("Up Your")
            Row { Text("Assertions") }
        }
        TitleScaffold {
            Row { SharedPower() }
            Text("")
            Row { SharedAssert() }
        }
    }

//    scene(
//        frameCount = 0,
//        enterTransition = FadeSceneEnterTransition(),
//        exitTransition = FadeSceneExitTransition(),
//    ) {
//        HeaderScaffold(intro = true)
//    }
//
//    scene(
//        frames = PowerAssertSamples,
//        enterTransition = FadeSceneEnterTransition(),
//        exitTransition = FadeSceneExitTransition(),
//    ) {
//        HeaderScaffold {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
//            ) {
//                ProvideTextStyle(MaterialTheme.typography.code1) {
//                    MagicText(transition.createChildTransition { it.toValue().string })
//                }
//            }
//        }
//    }
}

private object Header

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun SceneScope<*>.HeaderScaffold(
    intro: Boolean = false,
    outro: Boolean = false,
    content: @Composable () -> Unit = {},
) {
    val lineDuration = 300
    Box(Modifier.fillMaxSize()) {
        content()

        Column(
            Modifier
                .align(Alignment.BottomStart)
                .sharedElement(rememberSharedContentState(Header), zIndexInOverlay = -1f)
        ) {
            val lineFraction by transition.animateFloat(
                label = "title line width",
                transitionSpec = { tween(lineDuration, easing = EaseInOut) },
            ) {
                when (it) {
                    Frame.Start -> if (intro) 0f else 1f
                    is Frame.Value<*> -> 1f
                    Frame.End -> if (outro) 0f else 1f
                }
            }

            Spacer(Modifier.fillMaxWidth(lineFraction).requiredHeight(2.dp).gradientBackground())
            Row(
                Modifier.padding(start = 16.dp, top = 4.dp)
            ) {
                ProvideTextStyle(MaterialTheme.typography.title) {
                    SharedPower()
                    Text("-")
                    SharedAssert()
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
