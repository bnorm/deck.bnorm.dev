package dev.bnorm.librettist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.librettist.show.Advancement
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.ShowState

@OptIn(ExperimentalComposeUiApi::class)
fun WebSlideShow(
    canvasElementId: String,
    theme: @Composable () -> ShowTheme,
    builder: ShowBuilder.() -> Unit,
) {
    // Pulled from Google Slides with 1 inch = 100 dp
    val slideSize = DpSize(1000.dp, 563.dp)

    val showState = ShowState(builder)

    fun handleKeyEvent(event: KeyEvent): Boolean {
        println(event)

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

        return false
    }

    CanvasBasedWindow(canvasElementId = canvasElementId) {
        val focusRequester = remember { FocusRequester() }
        Box(modifier = Modifier.focusRequester(focusRequester).focusTarget().onKeyEvent(::handleKeyEvent)) {
            SlideShow(
                showState = showState,
                showOverview = false,
                theme = theme(),
                targetSize = slideSize,
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
