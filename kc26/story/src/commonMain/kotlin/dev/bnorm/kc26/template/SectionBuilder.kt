package dev.bnorm.kc26.template

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.animateTextStyle
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.SceneContent
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement

// TODO cleanup on aisle section!

private val LocalSceneTitle = compositionLocalOf<SceneTitle?> { null }

@Immutable
class SceneTitle(
    val title: String,
) {
    companion object {
        val current: SceneTitle?
            @Composable
            get() = LocalSceneTitle.current
    }
}

fun StoryboardBuilder.sectioned(
    block: SectionBuilder.() -> Unit,
) {
    return SectionBuilder(this).block()
}

class SectionBuilder(
    private val upstream: StoryboardBuilder,
) : StoryboardBuilder {
    private var prevTitle: SceneTitle? = null
    private var builder = upstream

    override fun <T> scene(
        frames: List<T>,
        enterTransition: SceneEnterTransition,
        exitTransition: SceneExitTransition,
        content: SceneContent<T>,
    ): Scene<T> = builder.scene(frames, enterTransition, exitTransition, content)

    fun nextSection(next: String) {
        prevTitle?.let { prev ->
            SectionTitle(prev.title, animateFromHeader = true)
            SectionChange(prev.title, next)
        }
        SectionTitle(next, animateToHeader = true)

        val title = SceneTitle(next)
        val decorator = ContentDecorator { content ->
            CompositionLocalProvider(LocalSceneTitle provides title) {
                content()
            }
        }

        this.prevTitle = title
        this.builder = object : StoryboardBuilder {
            override fun <T> scene(
                frames: List<T>,
                enterTransition: SceneEnterTransition,
                exitTransition: SceneExitTransition,
                content: SceneContent<T>,
            ): Scene<T> {
                return this@SectionBuilder.upstream.scene(
                    frames = frames,
                    enterTransition = enterTransition,
                    exitTransition = exitTransition,
                    content = content.decorated(decorator)
                )
            }
        }
    }

    fun endSection() {
        prevTitle?.let { prev ->
            SectionTitle(prev.title, animateFromHeader = true)
        }
        clearSection()
    }

    fun clearSection() {
        prevTitle = null
        builder = upstream
    }
}

private fun StoryboardBuilder.SectionChange(
    start: String,
    end: String,
) {
    val prefix = start.zip(end) { a, b -> a.takeIf { it == b } }
        .takeWhile { it != null }
        .joinToString(separator = "") { it?.toString() ?: "" }
    val startSuffix = start.drop(prefix.length)
    val endSuffix = end.drop(prefix.length)

    scene(frameCount = 0) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val textStyle = MaterialTheme.typography.title
            val textHeight = with(LocalDensity.current) { textStyle.lineHeight.toDp() }
            val height = (maxHeight - textHeight) / 2 - 16.dp

            val headerTransition = transition.createChildTransition {
                when (it) {
                    Frame.Start -> startSuffix
                    Frame.End -> endSuffix
                    is Frame.Value<*> -> error("!")
                }
            }

            Box(
                modifier = Modifier
                    .sharedElement(rememberSharedContentState(key = SceneTitle))
                    .fillMaxSize()
                    .padding(top = height)
            ) {
                headerTransition.AnimatedContent(
                    transitionSpec = {
                        fun <T> animOut() = tween<T>(400, delayMillis = 250, easing = EaseOut)
                        fun <T> animIn(): TweenSpec<T> = tween(400, easing = EaseIn)
                        val direction = if (targetState == startSuffix) -1 else 1

                        val enter = fadeIn(animIn()) +
                                slideInVertically(animIn()) { -it * direction }
                        val exit = fadeOut(animOut()) +
                                slideOutVertically(animOut()) { it * direction }
                        enter togetherWith exit using SizeTransform(clip = false)
                    },
                ) { header ->
                    SectionHeader(lineFraction = 0f) {
                        ProvideTextStyle(textStyle) {
                            GradientText(
                                prefix,
                                modifier = Modifier.sharedElement(rememberSharedContentState(key = "header-shared"))
                            )
                            GradientText(header)
                        }
                    }
                }
            }
        }
    }
}

private fun StoryboardBuilder.SectionTitle(
    header: String,
    animateFromHeader: Boolean = false,
    animateToHeader: Boolean = false,
) {
    carouselScene {
        SectionTitle(
            showAsHeader = transition.createChildTransition {
                when (it) {
                    Frame.Start -> animateFromHeader
                    Frame.End -> animateToHeader
                    is Frame.Value -> false
                }
            },
            header = header,
        )
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun SectionTitle(
    showAsHeader: Transition<Boolean>,
    header: String,
) {
    val headerTextStyle = MaterialTheme.typography.header
    val titleTextStyle = MaterialTheme.typography.title

    val moveDuration = 300
    val lineDuration = 300

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val textStyle by showAsHeader.animateTextStyle(
            transitionSpec = {
                when (targetState) {
                    true -> tween(moveDuration, delayMillis = 0, EaseInOut)
                    false -> tween(moveDuration, lineDuration, EaseInOut)
                }
            },
            targetValueByState = { if (!it) titleTextStyle else headerTextStyle }
        )
        val height by showAsHeader.animateDp(
            label = "header top padding",
            transitionSpec = {
                when (targetState) {
                    true -> tween(moveDuration, delayMillis = 0, EaseInOut)
                    false -> tween(moveDuration, lineDuration, EaseInOut)
                }
            },
            targetValueByState = {
                when (it) {
                    false -> with(LocalDensity.current) { (maxHeight - titleTextStyle.lineHeight.toDp()) / 2 - 16.dp }
                    true -> 0.dp
                }
            },
        )

        val lineFraction by showAsHeader.animateFloat(
            label = "header line width",
            transitionSpec = {
                when (targetState) {
                    true -> tween(lineDuration, moveDuration, EaseInOut)
                    false -> tween(lineDuration, delayMillis = 0, EaseInOut)
                }
            },
            targetValueByState = { if (it) 1f else 0f },
        )

        Box(
            modifier = Modifier
                .sharedElement(rememberSharedContentState(key = SceneTitle))
                .fillMaxSize()
                .padding(top = height)
        ) {
            SectionHeader(lineFraction = lineFraction) {
                ProvideTextStyle(headerTextStyle + textStyle) {
                    GradientText(header)
                }
            }
        }
    }
}
