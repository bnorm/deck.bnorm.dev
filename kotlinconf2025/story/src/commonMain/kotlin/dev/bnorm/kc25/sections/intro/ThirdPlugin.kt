package dev.bnorm.kc25.sections.intro

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import dev.bnorm.deck.story.generated.resources.writing_your_second_kotlin_compiler_plugin
import dev.bnorm.kc25.components.ResourceImage
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.components.temp.BULLET_2
import dev.bnorm.kc25.template.Body
import dev.bnorm.kc25.template.Header
import dev.bnorm.kc25.template.KodeeScene
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.toState

private fun <T> tSpec(): @Composable Transition.Segment<Int>.() -> FiniteAnimationSpec<T> = { tween(300) }
private fun <T> spec(): FiniteAnimationSpec<T> = tween(300)
private fun vEnter(): EnterTransition = fadeIn(spec()) + slideInHorizontally(spec()) { it / 2 }
private fun vExit(): ExitTransition = fadeOut(spec()) + slideOutHorizontally(spec()) { it / 2 }

fun StoryboardBuilder.ThirdPlugin() {
    KodeeScene(
        stateCount = 9,
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.CenterEnd),
            end = SceneEnter(alignment = Alignment.CenterStart),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.CenterEnd),
            end = SceneExit(alignment = Alignment.CenterStart),
        ),
    ) {
        Header()

        val state = frame.createChildTransition { it.toState() }
        Box {
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier.fillMaxSize().padding(32.dp)
            ) {
                state.AnimatedVisibility(visible = { it >= 1 }, enter = vEnter(), exit = vExit()) {
                    val shift by state.animateDp(tSpec()) { if (it >= 7) 32.dp else if (it >= 4) 16.dp else 0.dp }
                    val tint by state.animateFloat(tSpec()) { if (it >= 7) 0.66f else if (it >= 4) 0.33f else 0.01f }
                    ResourceImage(
                        Res.drawable.writing_your_first_kotlin_compiler_plugin,
                        modifier = Modifier.padding(top = 0.dp, end = shift).width(320.dp).padding(end = shift),
                        contentScale = ContentScale.FillWidth,
                        colorFilter = ColorFilter.tint(Color.Black.copy(alpha = tint), blendMode = BlendMode.Darken)
                    )
                }
                state.AnimatedVisibility(visible = { it >= 4 }, enter = vEnter(), exit = vExit()) {
                    val shift by state.animateDp(tSpec()) { if (it >= 7) 16.dp else 0.dp }
                    val tint by state.animateFloat(tSpec()) { if (it >= 7) 0.33f else 0.01f }
                    ResourceImage(
                        // TODO take a new screen shot with the same aspect ratio as the others
                        Res.drawable.writing_your_second_kotlin_compiler_plugin,
                        modifier = Modifier.padding(top = 96.dp, end = shift).width(320.dp).padding(end = shift),
                        contentScale = ContentScale.FillWidth,
                        colorFilter = ColorFilter.tint(Color.Black.copy(alpha = tint), blendMode = BlendMode.Darken)
                    )
                }
                state.AnimatedVisibility(visible = { it >= 7 }, enter = vEnter(), exit = vExit()) {
                    ResourceImage(
                        Res.drawable.k2_compiler_plugins,
                        modifier = Modifier.padding(top = 192.dp, end = 0.dp).width(320.dp),
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }

            Body {
                state.RevealAfter(0) { Text("$BULLET_1 Writing Your First Kotlin Compiler Plugin") }
                state.RevealAfter(1) { Text("    $BULLET_2 KotlinConf 2018 - Kevin Most") }
                state.RevealAfter(2) { Text("    $BULLET_2 Project setup and JVM bytecode") }
                state.RevealAfter(3) { Text("$BULLET_1 Writing Your Second Kotlin Compiler Plugin") }
                state.RevealAfter(4) { Text("    $BULLET_2 Blog post in 2020 - Brian Norman") }
                state.RevealAfter(5) { Text("    $BULLET_2 Backend IR and testing") }
                state.RevealAfter(6) { Text("$BULLET_1 K2 Compiler Plugins") }
                state.RevealAfter(7) { Text("    $BULLET_2 KotlinConf 2023 - Mikhail Glukhikh") }
                state.RevealAfter(8) { Text("    $BULLET_2 Architecture and available extensions") }
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
