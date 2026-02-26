package dev.bnorm.kc25.story.desktop

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.application
import dev.bnorm.deck.shared.Laser
import dev.bnorm.deck.shared.SceneIndexDecorator
import dev.bnorm.kc25.broadcast.Broadcast
import dev.bnorm.kc25.components.validateAllSamples
import dev.bnorm.kc25.createStoryboard
import dev.bnorm.kc25.template.DARK_COLORS
import dev.bnorm.kc25.template.storyDecorator
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.Animatic
import dev.bnorm.storyboard.easel.DesktopEasel
import dev.bnorm.storyboard.easel.assist.rememberAssistantWindow
import dev.bnorm.storyboard.easel.rememberAnimatic
import kotlinx.collections.immutable.persistentListOf

fun main() {
    val broadcast = Broadcast()
    val laser = Laser(color = DARK_COLORS.secondary)

    val captions = persistentListOf(
        laser.caption,
        broadcast.caption,
    )

    val currentIndex = mutableStateOf(Storyboard.Index(0, 0))

    application {
        validateAllSamples()
        val infiniteTransition = rememberInfiniteTransition()

        val animatic = rememberAnimatic {
            val decorator = ContentDecorator.from(
                laser.decorator,
                SceneIndexDecorator(currentIndex),
                storyDecorator(infiniteTransition),
                broadcast.decorator,
            )
            createStoryboard(decorator)
        }
        AttachAnimatic(animatic, broadcast, currentIndex)

        MaterialTheme(colors = darkColors()) {
            DesktopEasel(
                animatic = animatic,
                overlay = {},
                windows = listOf(
                    rememberAssistantWindow(animatic, captions)
                ),
            )
        }
    }
}

@Composable
private fun AttachAnimatic(
    animatic: Animatic,
    broadcast: Broadcast,
    currentIndex: MutableState<Storyboard.Index>,
) {
    LaunchedEffect(animatic) {
        broadcast.attach(animatic)
    }
    LaunchedEffect(animatic.currentIndex) {
        currentIndex.value = animatic.currentIndex
    }
}
