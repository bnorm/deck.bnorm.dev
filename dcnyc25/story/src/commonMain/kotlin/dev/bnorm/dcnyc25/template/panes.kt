package dev.bnorm.dcnyc25.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.easel.LocalStoryboard
import kotlin.jvm.JvmName

sealed class Pane<T> protected constructor(
    val content: SceneContent<T>,
) {
    class Quarter<T>(content: SceneContent<T>) : Pane<T>(content = {
        Box(Modifier.fillMaxHeight().width(SceneHalfWidth)) {
            Render(content)
        }
    })

    class Horizontal<T>(content: SceneContent<T>) : Pane<T>(content = {
        Box(Modifier.fillMaxWidth().height(SceneHalfHeight)) {
            Render(content)
        }
    }) {
        constructor(first: Quarter<T>, second: Quarter<T>) : this(content = {
            Row {
                Render(first.content)
                Render(second.content)
            }
        })
    }

    class Vertical<T>(content: SceneContent<T>) : Pane<T>(content = {
        Box(Modifier.fillMaxHeight().width(SceneHalfWidth)) {
            Render(content)
        }
    }) {
        constructor(first: Quarter<T>, second: Quarter<T>) : this(content = {
            Column {
                Render(first.content)
                Render(second.content)
            }
        })
    }
}

@JvmName("HorizontalPanes")
@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun SceneScope<Int>.Panes(
    panes: List<Pane.Horizontal<Int>>,
    background: Color = MaterialTheme.colors.secondary,
) {
    val state = rememberScrollState()
    transition.animateScroll(state, transitionSpec = { tween(750, easing = EaseInOut) }) {
        it.toState() * with(LocalDensity.current) { SceneHalfWidth.roundToPx() }
    }

    Column(Modifier.fillMaxSize().verticalScroll(state, enabled = false).background(background)) {
        for (pane in panes) {
            Render(pane.content)
        }
    }
}

@JvmName("VerticalPanes")
@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun SceneScope<Int>.Panes(
    panes: List<Pane.Vertical<Int>>,
    background: Color = MaterialTheme.colors.secondary,
) {
    val state = rememberScrollState()
    transition.animateScroll(state, transitionSpec = { tween(750, easing = EaseInOut) }) {
        it.toState() * with(LocalDensity.current) { SceneHalfWidth.roundToPx() }
    }

    Row(Modifier.fillMaxSize().horizontalScroll(state, enabled = false).background(background)) {
        for (pane in panes) {
            Render(pane.content)
        }
    }
}

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

inline val SceneHalfWidth: Dp
    @Composable get() {
        val format = LocalStoryboard.current!!.format
        return with(format.density) { format.size.width.toDp() / 2 }
    }

inline val SceneHalfHeight: Dp
    @Composable get() {
        val format = LocalStoryboard.current!!.format
        return with(format.density) { format.size.height.toDp() / 2 }
    }
