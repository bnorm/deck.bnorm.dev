package dev.bnorm.kc25.story.desktop

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import dev.bnorm.deck.shared.SceneIndexDecorator
import dev.bnorm.kc25.broadcast.BroadcastCaption
import dev.bnorm.kc25.broadcast.ReactionListener
import dev.bnorm.kc25.components.validateAllSamples
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.kc25.template.KodeeReactionDecorator
import dev.bnorm.kc25.template.LocalInfiniteTransition
import dev.bnorm.kc25.template.THEME_DECORATOR
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.easel.DesktopStoryEasel
import dev.bnorm.storyboard.easel.ExperimentalStoryStateApi
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.deck.shared.LaserCaption
import dev.bnorm.deck.shared.LaserDecorator
import dev.bnorm.deck.shared.LaserState
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    val state = StoryState()
    val laser = LaserState()
    val reactionListener = mutableStateOf<ReactionListener?>(null)

    val captions = persistentListOf(
        LaserCaption(laser),
        BroadcastCaption(state, reactionListener),
    )

    application {
        validateAllSamples()

        remember {
            val decorator = SceneDecorator.from(
                LaserDecorator(laser),
                THEME_DECORATOR,
                KodeeReactionDecorator(reactionListener),
                SceneIndexDecorator(state),
            )
            createStoryboard(decorator).also { state.updateStoryboard(it) }
        }

        MaterialTheme(colors = darkColors()) {
            CompositionLocalProvider(LocalInfiniteTransition provides rememberInfiniteTransition()) {
                DesktopStoryEasel(
                    storyState = state,
                    overlay = {},
                    captions = captions
                )
            }
        }
    }
}
