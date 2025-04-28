package dev.bnorm.kc25.story.desktop

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import dev.bnorm.deck.shared.Laser
import dev.bnorm.kc25.broadcast.Broadcast
import dev.bnorm.kc25.components.validateAllSamples
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.kc25.template.DARK_COLORS
import dev.bnorm.kc25.template.storyDecorator
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.easel.DesktopStoryEasel
import dev.bnorm.storyboard.easel.ExperimentalStoryStateApi
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.template.SceneIndexDecorator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    val state = StoryState()
    val broadcast = Broadcast(state, coroutineScope = CoroutineScope(Dispatchers.IO))
    val laser = Laser(color = DARK_COLORS.secondary)

    val captions = persistentListOf(
        laser.caption,
        broadcast.caption,
    )

    application {
        validateAllSamples()
        val infiniteTransition = rememberInfiniteTransition()

        remember {
            val decorator = SceneDecorator.from(
                laser.decorator,
                SceneIndexDecorator(state),
                storyDecorator(infiniteTransition),
                broadcast.decorator,
            )
            createStoryboard(decorator).also { state.updateStoryboard(it) }
        }

        MaterialTheme(colors = darkColors()) {
            DesktopStoryEasel(
                storyState = state,
                overlay = {},
                captions = captions
            )
        }
    }
}
