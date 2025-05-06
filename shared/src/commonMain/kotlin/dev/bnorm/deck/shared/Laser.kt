@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalTime::class)

package dev.bnorm.deck.shared

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.easel.assist.Caption
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.time.ExperimentalTime

 class Laser(color: Color = Color.Red) {
    private val state = LaserState()

    val caption: Caption = LaserCaption(state)
    // TODO add overlay controls?
    val decorator: SceneDecorator = LaserDecorator(state, color)
}

internal class LaserPath(
    val points: ImmutableList<Offset>,
)

class LaserState {
    internal var enabled by mutableStateOf(false)
        private set

    internal var dot by mutableStateOf<Offset?>(null)
        private set

    internal var active: SnapshotStateList<Offset>? = null
        private set

    private val _paths = mutableStateListOf<LaserPath>()
    internal val paths: List<LaserPath> get() = _paths

    fun toggle() {
        if (enabled) disable() else enable()
    }

    fun enable() {
        enabled = true
    }

    fun disable() {
        enabled = false
        exit()
        release()
    }

    fun clear() {
        _paths.clear()
    }

    internal fun onEvent(event: PointerEvent) {
        if (!enabled) return
        when (event.type) {
            PointerEventType.Press -> press(event)
            PointerEventType.Release -> release()
            PointerEventType.Move -> move(event)
            PointerEventType.Exit -> exit()
        }
    }

    private fun press(event: PointerEvent) {
        if (event.button != PointerButton.Primary && !event.buttons.isPrimaryPressed) return
        val change = event.changes.first()
        val path = mutableStateListOf<Offset>()
        path.add(change.position)
        active = path
    }

    private fun release() {
        val points = active?.toImmutableList() ?: return
        active = null
        _paths.add(LaserPath(points))
    }

    private fun move(event: PointerEvent) {
        val change = event.changes.first()
        dot = change.position
        active?.add(change.position)

        if (active != null && !change.pressed) {
            release()
        }
    }

    private fun exit() {
        dot = null
    }
}

fun LaserCaption(
    laser: LaserState,
): Caption = Caption {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Laser:", style = MaterialTheme.typography.h5)
        Spacer(Modifier.size(8.dp))
        IconButton(onClick = { laser.toggle() }) {
            Icon(
                imageVector = MaterialSymbolsLightStylusLaserPointer,
                contentDescription = if (laser.enabled) "Enable" else "Disable",
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                tint = if (laser.enabled) LocalContentColor.current else Color.DarkGray,
            )
        }
        // TODO slider for size?
        if (laser.paths.isNotEmpty()) {
            Spacer(Modifier.size(8.dp))
            IconButton(onClick = { laser.clear() }) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Clear",
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                )
            }
        }
    }
}

fun LaserDecorator(
    laserState: LaserState = LaserState(),
    laserColor: Color = Color.Red,
): SceneDecorator {
    return SceneDecorator { content ->
        content()
        Laser(laserState, laserColor)
    }
}

@Composable
fun Laser(laserState: LaserState, laserColor: Color) {
    // TODO share this state and only launch once
    val color = remember { Animatable(laserColor) }
    LaunchedEffect(laserState.dot) {
        color.snapTo(laserColor)
        color.animateTo(
            targetValue = Color.Transparent,
            animationSpec = tween(15_000, easing = LinearEasing)
        )
        laserState.clear()
    }

    fun DrawScope.drawLaserPoints(points: List<Offset>, color: Color) {
        drawPoints(
            points = points.map { Offset(it.x, it.y) },
            pointMode = PointMode.Polygon,
            color = color,
            strokeWidth = 2.dp.toPx()
        )
    }

    fun Modifier.laserInputModifier(): Modifier {
        // Don't cover scenes with clickable content if the laser isn't enabled.
        // This allows other interactive content to still work.
        if (!(laserState.enabled)) return this

        return pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    laserState.onEvent(awaitPointerEvent(PointerEventPass.Main))
                }
            }
        }
    }

    Canvas(
        Modifier.fillMaxSize()
            .clipToBounds()
            .laserInputModifier()
    ) {
        laserState.dot?.let { dot ->
            drawPoints(
                points = listOf(dot),
                pointMode = PointMode.Points,
                color = laserColor,
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
        laserState.active?.let { drawLaserPoints(it, laserColor) }
        for (path in laserState.paths) {
            drawLaserPoints(path.points, color.value)
        }
    }
}

val MaterialSymbolsLightStylusLaserPointer: ImageVector
    get() {
        if (_MaterialSymbolsLightStylusLaserPointer != null) {
            return _MaterialSymbolsLightStylusLaserPointer!!
        }
        _MaterialSymbolsLightStylusLaserPointer = materialIcon(name = "MaterialSymbolsLightStylusLaserPointer") {
            materialPath {
                moveTo(9.002f, 21.02f)
                quadToRelative(-1.04f, 0f, -1.771f, -0.73f)
                reflectiveQuadTo(6.5f, 18.523f)
                reflectiveQuadToRelative(0.729f, -1.772f)
                reflectiveQuadToRelative(1.769f, -0.73f)
                reflectiveQuadToRelative(1.771f, 0.728f)
                reflectiveQuadToRelative(0.731f, 1.769f)
                reflectiveQuadToRelative(-0.728f, 1.772f)
                reflectiveQuadToRelative(-1.77f, 0.73f)
                moveToRelative(4.261f, -3.475f)
                quadToRelative(-0.188f, -0.875f, -0.717f, -1.598f)
                reflectiveQuadToRelative(-1.29f, -1.137f)
                lineToRelative(3.246f, -3.213f)
                horizontalLineTo(6.4f)
                quadToRelative(-0.608f, 0f, -1.004f, -0.43f)
                quadTo(5f, 10.733f, 5f, 10.106f)
                quadToRelative(0f, -0.391f, 0.195f, -0.704f)
                reflectiveQuadToRelative(0.505f, -0.53f)
                lineTo(16.696f, 2.02f)
                quadToRelative(0.277f, -0.16f, 0.585f, -0.08f)
                reflectiveQuadToRelative(0.467f, 0.348f)
                reflectiveQuadToRelative(0.09f, 0.571f)
                quadToRelative(-0.07f, 0.306f, -0.322f, 0.465f)
                lineTo(8.058f, 9.5f)
                horizontalLineTo(17.6f)
                quadToRelative(0.59f, 0f, 0.995f, 0.405f)
                reflectiveQuadTo(19f, 10.9f)
                quadToRelative(0f, 0.359f, -0.074f, 0.707f)
                quadToRelative(-0.074f, 0.349f, -0.322f, 0.597f)
                close()
            }
        }
        return _MaterialSymbolsLightStylusLaserPointer!!
    }

private var _MaterialSymbolsLightStylusLaserPointer: ImageVector? = null
