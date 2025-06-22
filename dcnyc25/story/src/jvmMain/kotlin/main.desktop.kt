package dev.bnorm.dcnyc25.story.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
 import dev.bnorm.dcnyc25.ScriptCaption
import dev.bnorm.dcnyc25.createStoryboard
import dev.bnorm.dcnyc25.template.COLORS
import dev.bnorm.dcnyc25.template.storyDecorator
import dev.bnorm.deck.shared.Laser
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.easel.DesktopStoryEasel
import dev.bnorm.storyboard.easel.ExperimentalStoryStateApi
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.template.SceneIndexDecorator
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    val state = StoryState()
    val laser = Laser(color = COLORS.secondary)

    val captions = persistentListOf(
        laser.caption,
        ScriptCaption(state),
    )

    application {
        remember {
            val decorator = SceneDecorator.from(
                storyDecorator(),
                laser.decorator,
                SceneIndexDecorator(state),
            )
            createStoryboard(
                decorator,
                includeTextFieldSamples = true,
            ).also { state.updateStoryboard(it) }
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
