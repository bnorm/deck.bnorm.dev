package dev.bnorm.dcnyc25.story.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import dev.bnorm.dcnyc25.broadcast.VoteTally
import dev.bnorm.dcnyc25.createStoryboard
import dev.bnorm.dcnyc25.template.COLORS
import dev.bnorm.dcnyc25.template.SampleCaption
import dev.bnorm.dcnyc25.template.storyDecorator
import dev.bnorm.deck.shared.Laser
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
    val tally = VoteTally(coroutineScope = CoroutineScope(Dispatchers.IO))
    val laser = Laser(color = COLORS.secondary)

    val captions = persistentListOf(
        laser.caption,
        tally.caption,
        SampleCaption()
    )

    application {
        remember {
            val decorator = SceneDecorator.from(
                storyDecorator(tally = tally),
                laser.decorator,
                SceneIndexDecorator(state),
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
