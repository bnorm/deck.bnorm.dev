package dev.bnorm.kc26.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.ContentDecorator

private val SceneCoordinatesLocal = compositionLocalOf<State<LayoutCoordinates?>> { error("!") }

fun gradientDecorator(): ContentDecorator = ContentDecorator { content ->
    val coordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }
    Box(modifier = Modifier.fillMaxSize().onPlaced { coordinates.value = it }) {
        CompositionLocalProvider(SceneCoordinatesLocal provides coordinates) {
            Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)).background(Kc26Colors.colorGradient)) {
                content()
            }
        }
    }
}

// TODO should there be a text version of this that adds a subtle border?
@Composable
fun Modifier.gradientOverlay(): Modifier {
    val sceneCoordinates = SceneCoordinatesLocal.current
    val start = remember { mutableStateOf(Offset.Zero) }
    val end = remember { mutableStateOf(Offset.Zero) }
    return onPlaced {
        val scene = sceneCoordinates.value ?: return@onPlaced
        val position = scene.localPositionOf(it)
        start.value = Offset(0f, scene.size.height.toFloat()) - position
        end.value = Offset(scene.size.width.toFloat(), 0f) - position
    }
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithCache {
            val brush = Brush.linearGradient(
                colors = Kc26Colors.gradientColors,
                start = start.value,
                end = end.value,
            )

            onDrawWithContent {
                drawContent()
                drawRect(brush, blendMode = BlendMode.SrcIn)
            }
        }
}

@Composable
fun Modifier.gradientBackground(): Modifier {
    val sceneCoordinates = SceneCoordinatesLocal.current
    val start = remember { mutableStateOf(Offset.Zero) }
    val end = remember { mutableStateOf(Offset.Zero) }
    return onPlaced {
        val scene = sceneCoordinates.value ?: return@onPlaced
        val position = scene.localPositionOf(it)
        start.value = Offset(0f, scene.size.height.toFloat()) - position
        end.value = Offset(scene.size.width.toFloat(), 0f) - position
    }
        .drawWithCache {
            val brush = Brush.linearGradient(
                colors = Kc26Colors.gradientColors,
                start = start.value,
                end = end.value,
            )

            onDrawBehind {
                drawRect(brush)
            }
        }
}

@Composable
fun LightBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .gradientBackground()
            .background(Color.White.copy(alpha = 0.9f))
    ) {
        content()
    }
}

@Composable
fun DarkBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .gradientBackground()
            .background(Color.Black.copy(alpha = 0.9f))
    ) {
        content()
    }
}
