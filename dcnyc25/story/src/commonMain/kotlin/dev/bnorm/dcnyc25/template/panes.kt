package dev.bnorm.dcnyc25.template

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.bnorm.storyboard.easel.LocalStoryboard

@Composable
fun <T> Transition<T>.animateScroll(
    scrollState: ScrollState,
    transitionSpec: @Composable Transition.Segment<T>.() -> FiniteAnimationSpec<Int> = { spring() },
    label: String = "ScrollAnimation",
    targetValueByState: @Composable (state: T) -> Int,
) {
    val scrollPosition by animateInt(transitionSpec, label, targetValueByState)
    scrollState.dispatchRawDelta((scrollPosition - scrollState.value).toFloat())
}

@Composable
fun Full(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier.width(SceneWidth).height(SceneHeight), color = color) {
        content()
    }
}

@Composable
fun Vertical(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier.width(SceneHalfWidth).height(SceneHeight), color = color) {
        content()
    }
}

@Composable
fun Horizontal(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier.width(SceneWidth).height(SceneHalfHeight), color = color) {
        content()
    }
}

@Composable
fun Quarter(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier.width(SceneHalfWidth).height(SceneHalfHeight), color = color) {
        content()
    }
}

inline val SceneWidth: Dp
    @Composable get() {
        val format = LocalStoryboard.current!!.format
        return with(format.density) { format.size.width.toDp() }
    }

inline val SceneHeight: Dp
    @Composable get() {
        val format = LocalStoryboard.current!!.format
        return with(format.density) { format.size.height.toDp() }
    }

inline val SceneHalfWidth: Dp
    @Composable get() = SceneWidth / 2f

inline val SceneHalfHeight: Dp
    @Composable get() = SceneHeight / 2f
