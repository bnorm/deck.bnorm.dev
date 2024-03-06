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
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

fun DesktopSlideShow(
    title: String,
    theme: SlideTheme,
    builder: SlideGroupScope.() -> Unit,
) {
    // Pulled from Google Slides with 1 inch = 100 dp
    val windowState = WindowState(size = DpSize(1000.dp, 800.dp))
    val slideSize = DpSize(1000.dp, 563.dp)
    val advancementState = AdvancementState()

    fun handleKeyEvent(event: KeyEvent): Boolean {
        // TODO rate-limit holding down the key?
        if (event.type == KeyEventType.KeyDown) {
            val advancement = when (event.key) {
                Key.DirectionRight,
                Key.Enter,
                Key.Spacebar,
                -> Advancement(forward = true)

                Key.DirectionLeft,
                Key.Backspace,
                -> Advancement(forward = false)

                else -> null
            }
            if (advancement != null) {
                advancementState.handlers.reversed().any { it(advancement) }
                advancementState.lastAdvancement = advancement
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

    singleWindowApplication(
        state = windowState,
        title = title,
        onPreviewKeyEvent = ::handleKeyEvent,
    ) {
        SlideShow(
            advancementState = advancementState,
            showOverview = windowState.placement != WindowPlacement.Fullscreen,
            theme = theme,
            targetSize = slideSize,
            builder = builder,
        )
    }
}

@Composable
private fun SlideShow(
    advancementState: AdvancementState,
    showOverview: Boolean,
    theme: SlideTheme,
    targetSize: DpSize,
    builder: SlideGroupScope.() -> Unit
) {
    CompositionLocalProvider(LocalAdvancementState provides advancementState) {
        val slides: List<@Composable () -> Unit> = remember(builder) {
            buildList {
                object : SlideGroupScope {
                    override fun slide(content: @Composable () -> Unit) {
                        add(content)
                    }
                }.builder()
            }
        }
        var slide by rememberAdvancementIndex(slides.size)

        SlideTheme(theme) {
            Row(modifier = Modifier.fillMaxSize()) {
                val state = rememberLazyListState()
                if (showOverview) {
                    SlideShowOverview(
                        targetSize = targetSize,
                        selectedSlide = slide,
                        slides = slides,
                        onSlideSelected = { slide = it },
                        modifier = Modifier.weight(0.2f),
                        state = state
                    )
                }

                SlideShowDisplay(targetSize, slide, slides, Modifier.weight(0.8f))
            }
        }
    }
}

@Composable
private fun SlideShowDisplay(
    targetSize: DpSize,
    slide: Int,
    slides: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
) {
    ScaledBox(
        targetSize = targetSize,
        modifier = modifier.fillMaxHeight().background(MaterialTheme.colors.background)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            // TODO why is this box required for proper alignment?
            Box(modifier = Modifier.fillMaxSize()) {
                key(slide) {
                    slides[slide]()
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
    selectedSlide: Int,
    slides: List<@Composable () -> Unit>,
    onSlideSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(8.dp), state = state) {
        items(slides.size) { index ->
            Box(modifier = Modifier.padding(8.dp).clickable { onSlideSelected(index) }) {
                ScaledBox(
                    targetSize = targetSize,
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(targetSize.width / targetSize.height)
                        .background(MaterialTheme.colors.background)
                        .then(if (index == selectedSlide) Modifier.border(2.dp, Color.Red) else Modifier)
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        // TODO why is this box required for proper alignment?
                        Box(modifier = Modifier.fillMaxSize()) {
                            CompositionLocalProvider(LocalAdvancementState provides AdvancementState()) {
                                slides[index]()
                            }
                        }
                    }
                }
            }
        }
    }
}
