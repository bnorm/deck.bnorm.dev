package dev.bnorm.kc26.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.SceneContent
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import kotlin.experimental.ExperimentalTypeInference

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

interface FrameBuilder<T> {
    val current: T
    fun before(transform: T.() -> T)
    fun next(transform: T.() -> T)
}

@OptIn(ExperimentalTypeInference::class)
fun <T> StoryboardBuilder.carouselScene(
    start: T,
    @BuilderInference frames: FrameBuilder<T>.() -> Unit,
    enterTransition: SceneEnterTransition = SceneEnter(alignment = Alignment.CenterEnd),
    exitTransition: SceneExitTransition = SceneExit(alignment = Alignment.CenterEnd),
    content: SceneContent<T>,
): Scene<T> {
    return scene(
        frames = buildList {
            var frame = start
            add(frame)
            object : FrameBuilder<T> {
                override val current: T get() = frame
                override fun before(transform: T.() -> T) {
                    frame = frame.transform()
                }
                override fun next(transform: T.() -> T) {
                    frame = frame.transform()
                    add(frame)
                }
            }.frames()
        },
        enterTransition = enterTransition,
        exitTransition = exitTransition,
    ) {
        Box(Modifier.fillMaxSize()) { Render(content) }
    }
}
