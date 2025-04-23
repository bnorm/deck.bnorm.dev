package dev.bnorm.kc25.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import dev.chrisbanes.haze.*

private enum class SceneContent {
    Header,
    Body,
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun KodeeScaffold(
    header: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    body: @Composable BoxScope.(PaddingValues) -> Unit,
) {
    val hazeState = remember { HazeState() }
    val style = HazeDefaults.style(
        backgroundColor = Color.Transparent,
        tint = HazeTint(color = Color(0xFF1D002E)),
        blurRadius = 16.dp,
        noiseFactor = 0f,
    )

    val contentPadding = remember {
        object : PaddingValues {
            var paddingHolder by mutableStateOf(PaddingValues(0.dp))

            override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
                paddingHolder.calculateLeftPadding(layoutDirection)

            override fun calculateTopPadding(): Dp = paddingHolder.calculateTopPadding()

            override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
                paddingHolder.calculateRightPadding(layoutDirection)

            override fun calculateBottomPadding(): Dp = paddingHolder.calculateBottomPadding()
        }
    }

    SubcomposeLayout(modifier) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val headerPlaceables = subcompose(SceneContent.Header) {
            Box(
                modifier = Modifier
                    .hazeEffect(state = hazeState, style = style) {
                        progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
                    }
            ) {
                header()
            }
        }.fastMap { it.measure(looseConstraints) }

        val headerHeight = headerPlaceables.fastMaxBy { it.height }?.height ?: 0

        contentPadding.paddingHolder = PaddingValues(
            top = headerHeight.toDp(),
            bottom = 0.dp,
            start = 32.dp,
            end = 32.dp,
        )

        val bodyPlaceables = subcompose(SceneContent.Body) {
            Box(Modifier.fillMaxSize().hazeSource(state = hazeState)) {
                body(contentPadding)
            }
        }.fastMap { it.measure(looseConstraints) }

        layout(layoutWidth, layoutHeight) {
            bodyPlaceables.fastForEach { it.place(0, 0) }
            headerPlaceables.fastForEach { it.place(0, 0) }
        }
    }
}