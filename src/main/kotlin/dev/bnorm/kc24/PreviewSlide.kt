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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.SlideTheme
import dev.bnorm.librettist.show.Advancement
import dev.bnorm.librettist.show.LocalShowState
import dev.bnorm.librettist.show.ShowState
import dev.bnorm.librettist.show.SlideScope

@Composable
fun PreviewSlide(
    scale: Float = 0.5f,
    theme: SlideTheme = Theme.dark,
    direction: Advancement.Direction = Advancement.Direction.Forward,
    content: @Composable SlideScope.() -> Unit,
) {
    val showState = remember { ShowState(emptyList(), direction) }
    CompositionLocalProvider(LocalShowState provides showState) {
        SlideTheme(theme) {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.requiredSize(DpSize(1000.dp, 563.dp)).scale(scale)) {
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
