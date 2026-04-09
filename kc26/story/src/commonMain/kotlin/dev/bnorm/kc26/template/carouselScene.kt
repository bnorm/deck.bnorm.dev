package dev.bnorm.kc26.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.SceneContent
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit

fun <T> StoryboardBuilder.carouselScene(
    frames: List<T>,
    enterTransition: SceneEnterTransition = SceneEnter(alignment = Alignment.CenterEnd),
    exitTransition: SceneExitTransition = SceneExit(alignment = Alignment.CenterEnd),
    content: SceneContent<T>,
): Scene<T> = scene(frames, enterTransition, exitTransition) {
    Box(Modifier.fillMaxSize()) { Render(content) }
}

fun StoryboardBuilder.carouselScene(
    frameCount: Int = 1,
    enterTransition: SceneEnterTransition = SceneEnter(alignment = Alignment.CenterEnd),
    exitTransition: SceneExitTransition = SceneExit(alignment = Alignment.CenterEnd),
    content: SceneContent<Int>,
): Scene<Int> = scene(frameCount, enterTransition, exitTransition) {
    Box(Modifier.fillMaxSize()) { Render(content) }
}
