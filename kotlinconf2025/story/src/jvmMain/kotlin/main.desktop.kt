package dev.bnorm.kc25.story.desktop

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.window.application
import dev.bnorm.kc25.broadcast.*
import dev.bnorm.kc25.components.validateAllSamples
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.kc25.template.LocalInfiniteTransition
import dev.bnorm.deck.shared.SceneIndexDecorator
import dev.bnorm.kc25.template.THEME_DECORATOR
import dev.bnorm.storyboard.easel.DesktopStoryEasel
import dev.bnorm.storyboard.easel.ExperimentalStoryStateApi
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.assist.Caption
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import dev.bnorm.storyboard.plus
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    val state = StoryState()
    var storyBroadcaster by mutableStateOf<StoryBroadcaster?>(null)
    var reactionListener by mutableStateOf<ReactionListener?>(null)

    @Composable
    fun StoryOverlayScope.DesktopOverlay() {
        OverlayNavigation(state)
        OverlayBroadcasting(
            enabled = storyBroadcaster != null,
            onClick = {
                storyBroadcaster = if (storyBroadcaster == null) StoryBroadcaster() else null
                reactionListener = if (reactionListener == null) ReactionListener() else null
            },
        )
    }

    application {
        validateAllSamples()
        val infiniteTransition = rememberInfiniteTransition()

        Broadcast(state, storyBroadcaster)

        remember {
            val decorator = THEME_DECORATOR + SceneIndexDecorator(state)
            createStoryboard(decorator).also { state.updateStoryboard(it) }
        }

        MaterialTheme(colors = darkColors()) {
            CompositionLocalProvider(
                LocalReactionListener provides reactionListener,
                LocalInfiniteTransition provides infiniteTransition,
            ) {
                DesktopStoryEasel(
                    storyState = state,
                    overlay = { DesktopOverlay() },
                    captions = persistentListOf(
                        Caption { ReactionGraph(reactionListener) }
                    )
                )
            }
        }
    }
}
