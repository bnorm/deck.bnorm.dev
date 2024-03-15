package dev.bnorm.kc24

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import dev.bnorm.librettist.DEFAULT_SLIDE_SIZE
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.show.Advancement
import dev.bnorm.librettist.show.LocalShowState
import dev.bnorm.librettist.show.ShowState
import dev.bnorm.librettist.show.SlideScope

@Composable
fun PreviewSlide(
    scale: Float = 0.5f,
    theme: ShowTheme = Theme.dark,
    direction: Advancement.Direction = Advancement.Direction.Forward,
    content: @Composable SlideScope.() -> Unit,
) {
    val showState = remember { ShowState(emptyList(), direction) }
    CompositionLocalProvider(LocalShowState provides showState) {
        ShowTheme(theme) {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.requiredSize(DEFAULT_SLIDE_SIZE).scale(scale)) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        // TODO why is this box required for proper alignment?
                        Box(modifier = Modifier.fillMaxSize()) {
                            showState.content()
                        }
                    }
                }
            }
        }
    }
}
