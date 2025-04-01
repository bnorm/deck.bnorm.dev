package dev.bnorm.kc25.sections.intro

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.k2_compiler_plugins
import dev.bnorm.deck.story.generated.resources.writing_your_first_kotlin_compiler_plugin
import dev.bnorm.deck.story.generated.resources.writing_your_second_kotlin_compiler_plugin_medium
import dev.bnorm.kc25.components.ResourceImage
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.components.temp.BULLET_2
import dev.bnorm.kc25.template.HeaderAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.enter
import dev.bnorm.storyboard.easel.exit
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

fun StoryboardBuilder.ThirdPlugin() {
    scene(
        stateCount = 9,
        enterTransition = enter(end = SceneEnter(alignment = Alignment.CenterStart)),
        exitTransition = exit(end = SceneExit(alignment = Alignment.CenterStart)),
    ) {
        HeaderAndBody {
            val state = frame.createChildTransition { it.toState() }
            Box(Modifier.fillMaxSize().padding(start = 64.dp, top = 32.dp, end = 32.dp)) {
                Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxSize()) {
                    // TODO shift previous images to the left as new ones appear?
                    state.AnimatedVisibility(
                        visible = { it >= 1 },
                        enter = fadeIn() + slideInHorizontally { it / 2 }, exit = fadeOut() + slideOutHorizontally { it / 2 },
                        modifier = Modifier.padding(top = 0.dp, end = 64.dp),
                    ) {
                        val tint by state.animateFloat { if (it >= 7) 0.66f else if (it >= 4) 0.33f else 0.01f }
                        ResourceImage(
                            Res.drawable.writing_your_first_kotlin_compiler_plugin,
                            modifier = Modifier.width(360.dp),
                            contentScale = ContentScale.FillWidth,
                            colorFilter = ColorFilter.tint(Color.Black.copy(alpha = tint), blendMode = BlendMode.Darken)
                        )
                    }
                    state.AnimatedVisibility(
                        visible = { it >= 4 },
                        enter = fadeIn() + slideInHorizontally { it / 2 }, exit = fadeOut() + slideOutHorizontally { it / 2 },
                        modifier = Modifier.padding(top = 64.dp, end = 32.dp),
                    ) {
                        val tint by state.animateFloat { if (it >= 7) 0.33f else 0.01f }
                        ResourceImage(
                            Res.drawable.writing_your_second_kotlin_compiler_plugin_medium,
                            modifier = Modifier.width(360.dp),
                            contentScale = ContentScale.FillWidth,
                            colorFilter = ColorFilter.tint(Color.Black.copy(alpha = tint), blendMode = BlendMode.Darken)
                        )
                    }
                    state.AnimatedVisibility(
                        visible = { it >= 7 },
                        enter = fadeIn() + slideInHorizontally { it / 2 }, exit = fadeOut() + slideOutHorizontally { it / 2 },
                        modifier = Modifier.padding(top = 128.dp, end = 0.dp),
                    ) {
                        ResourceImage(
                            Res.drawable.k2_compiler_plugins,
                            modifier = Modifier.width(360.dp),
                            contentScale = ContentScale.FillWidth,
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    state.RevealAfter(0) { Text("$BULLET_1 Writing Your First Kotlin Compiler Plugin") }
                    state.RevealAfter(1) { Text("    $BULLET_2 KotlinConf 2018 - Kevin Most") }
                    state.RevealAfter(2) { Text("    $BULLET_2 Project setup and JVM bytecode manipulation") }
                    state.RevealAfter(3) { Text("$BULLET_1 Writing Your Second Kotlin Compiler Plugin") }
                    state.RevealAfter(4) { Text("    $BULLET_2 Blog post in 2020 - Brian Norman") }
                    state.RevealAfter(5) { Text("    $BULLET_2 Backend IR manipulation and testing") }
                    state.RevealAfter(6) { Text("$BULLET_1 K2 Compiler Plugins") }
                    state.RevealAfter(7) { Text("    $BULLET_2 KotlinConf 2023 - Mikhail Glukhikh") }
                    state.RevealAfter(8) { Text("    $BULLET_2 Architecture and available extensions") }
                }
            }
        }
    }
}

@Composable
fun <T : Comparable<T>> Transition<T>.RevealAfter(
    value: T,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = { it >= value },
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        content()
    }
}
