package dev.bnorm.kc26.story.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.application
import dev.bnorm.deck.shared.SceneIndexDecorator
import dev.bnorm.kc26.createStoryboard
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.Animatic
import dev.bnorm.storyboard.easel.DesktopEasel
import dev.bnorm.storyboard.easel.rememberAnimatic

fun main() {
    val currentIndex = mutableStateOf(Storyboard.Index(0, 0))

    application {
        val animatic = rememberAnimatic("animatic") {
            createStoryboard(
                decorator = ContentDecorator.from(
                    SceneIndexDecorator(currentIndex),
                ),
            )
        }
        AttachAnimatic(animatic, currentIndex)

        MaterialTheme(colors = darkColors()) {
            DesktopEasel(animatic)
        }
    }
}

@Composable
private fun AttachAnimatic(
    animatic: Animatic,
    currentIndex: MutableState<Storyboard.Index>,
) {
    LaunchedEffect(animatic.currentIndex) {
        currentIndex.value = animatic.currentIndex
    }
}
