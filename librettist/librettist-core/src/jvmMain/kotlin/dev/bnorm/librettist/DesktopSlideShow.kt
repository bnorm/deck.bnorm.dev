package dev.bnorm.librettist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import dev.bnorm.librettist.show.*

fun DesktopSlideShow(
    title: String,
    theme: SlideTheme,
    builder: ShowBuilder.() -> Unit,
) {
    // Pulled from Google Slides with 1 inch = 100 dp
    val slideSize = DpSize(1000.dp, 563.dp)

    val windowState = WindowState(size = DpSize(1000.dp, 800.dp))
    val showState = ShowState(builder)

    fun handleKeyEvent(event: KeyEvent): Boolean {
        // TODO rate-limit holding down the key?
        if (event.type == KeyEventType.KeyDown) {
            val advancement = when (event.key) {
                Key.DirectionRight,
                Key.Enter,
                Key.Spacebar,
                -> Advancement(direction = Advancement.Direction.Forward)

                Key.DirectionLeft,
                Key.Backspace,
                -> Advancement(direction = Advancement.Direction.Backward)

                else -> null
            }
            if (advancement != null) {
                showState.advance(advancement)
                return true
            }
        }

        // TODO handle some other keys?
        //  - navigating to specific slides?
        if (event.type == KeyEventType.KeyUp) {
            when (event.key) {
                Key.Escape -> if (windowState.placement == WindowPlacement.Fullscreen) {
                    windowState.placement = WindowPlacement.Floating
                    return true
                }

                Key.F -> if (windowState.placement != WindowPlacement.Fullscreen && event.isCtrlPressed && event.isMetaPressed) {
                    windowState.placement = WindowPlacement.Fullscreen
                    return true
                }
            }
        }

        return true
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = title,
            onPreviewKeyEvent = ::handleKeyEvent,
        ) {
            SlideShow(
                showState = showState,
                showOverview = windowState.placement != WindowPlacement.Fullscreen,
                theme = theme,
                targetSize = slideSize,
            )
        }
    }
}


@Composable
private fun SlideShow(
    showState: ShowState,
    showOverview: Boolean,
    theme: SlideTheme,
    targetSize: DpSize
) {
    CompositionLocalProvider(LocalShowState provides showState) {
        SlideTheme(theme) {
            Row(modifier = Modifier.fillMaxSize()) {
                val state = rememberLazyListState()
                if (showOverview) {
                    SlideShowOverview(
                        targetSize = targetSize,
                        showState = showState,
                        modifier = Modifier.weight(0.2f),
                        state = state
                    )
                }

                SlideShowDisplay(targetSize, showState.index, showState, Modifier.weight(0.8f))
            }
        }
    }
}

@Composable
private fun SlideShowDisplay(
    targetSize: DpSize,
    slideIndex: Int,
    showState: ShowState,
    modifier: Modifier = Modifier,
) {
    ScaledBox(
        targetSize = targetSize,
        modifier = modifier.fillMaxHeight().background(MaterialTheme.colors.background)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            // TODO why is this box required for proper alignment?
            Box(modifier = Modifier.fillMaxSize()) {
                key(slideIndex) {
                    val slide = showState.slides[slideIndex]
                    showState.slide()
                }
            }
        }
    }
}

@Composable
private fun ScaledBox(
    targetSize: DpSize,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var scale by remember { mutableStateOf(1f) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .onSizeChanged {
                val (w, h) = with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                scale = minOf(w / targetSize.width, h / targetSize.height)
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.requiredSize(targetSize).scale(scale)) {
            content()
        }
    }
}

@Composable
private fun SlideShowOverview(
    targetSize: DpSize,
    showState: ShowState,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
) {
    // TODO advancement never happens in the overview, but we need
    //  to provide a show state anyways so ListenAdvancement doesn't error.
    //  is there a better way to disable advancement?
    val constantShowState = remember { ShowState(emptyList()) }
    CompositionLocalProvider(LocalShowState provides constantShowState) {

        // TODO: use state.animateScrollToItem(selectedSlide) somehow to always keep selected slide visible (but not always at the top)
        LazyColumn(modifier = modifier, contentPadding = PaddingValues(8.dp), state = state) {
            items(showState.slides.size) { index ->
                val slide = remember(index) { showState.slides[index] }

                ScaledBox(
                    targetSize = targetSize,
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp)
                        .aspectRatio(targetSize.width / targetSize.height)
                        .background(MaterialTheme.colors.background)
                        .clickable { showState.index = index }
                        .then(if (index == showState.index) Modifier.border(2.dp, Color.Red) else Modifier)
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        // TODO why is this box required for proper alignment?
                        Box(modifier = Modifier.fillMaxSize()) {
                            constantShowState.slide()
                            }
                        }
                    }
            }
        }
    }
}
