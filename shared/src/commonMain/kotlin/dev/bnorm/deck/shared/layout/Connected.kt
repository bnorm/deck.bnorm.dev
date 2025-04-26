package dev.bnorm.deck.shared.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

interface ConnectedScope<T> {
    fun item(
        value: T,
        x: Dp,
        y: Dp,
        connections: List<T> = emptyList(),
        content: @Composable () -> Unit,
    )
}

// TODO how do we support shared transition layout?
@Composable
fun <T> Connected(
    modifier: Modifier = Modifier,
    connection: @Composable (start: T, startRect: Rect, end: T, endRect: Rect) -> Unit = { _, _, _, _ -> },
    content: ConnectedScope<T>.() -> Unit,
) {
    SubcomposeLayout(modifier) { constraints ->
        val items = mutableListOf<Item<T>>()
        val startingConnections = mutableListOf<Pair<T, T>>()
        object : ConnectedScope<T> {
            override fun item(
                value: T,
                x: Dp,
                y: Dp,
                connections: List<T>,
                content: @Composable () -> Unit,
            ) {
                require(value !in connections) { "Item cannot be connected to itself" }
                items.add(Item(value, x.roundToPx(), y.roundToPx(), content))
                for (end in connections) startingConnections.add(value to end)
            }
        }.content()

        val byValue = items.associateBy { it.value }

        val connections = startingConnections.mapNotNull {
            val start = byValue[it.first] ?: return@mapNotNull null
            val end = byValue[it.second] ?: return@mapNotNull null
            Connection(start, end, minOf(start.x, end.x), minOf(start.y, end.y))
        }

        val itemMeasurables = subcompose(ConnectedSubSlot.CONTENT) {
            for (item in items) {
                item.content()
            }
        }
        for ((index, measurable) in itemMeasurables.withIndex()) {
            items[index].placeable = measurable.measure(constraints.copyMaxDimensions())
        }

        val connectionMeasurables = subcompose(ConnectedSubSlot.CONNECTION) {
            for (connection in connections) {
                val start = connection.start
                val end = connection.end
                val offset = Offset(connection.x.toFloat(), connection.y.toFloat())
                connection(
                    start.value,
                    start.toRect().translate(-offset),
                    end.value,
                    end.toRect().translate(-offset)
                )
            }
        }
        for ((index, measurable) in connectionMeasurables.withIndex()) {
            val connection = connections[index]
            val start = connection.start
            val end = connection.end

            val width = minOf(start.x + start.placeable.width, end.x + start.placeable.width) - minOf(start.x, end.x)
            val height = minOf(start.y + start.placeable.height, end.y + start.placeable.height) - minOf(start.y, end.y)
            connection.placeable = measurable.measure(Constraints.fixed(width, height))
        }

        val width = items.maxOfOrNull { it.x + it.placeable.width } ?: 0
        val height = items.maxOfOrNull { it.y + it.placeable.height } ?: 0
        layout(maxOf(width, constraints.minWidth), maxOf(height, constraints.minHeight)) {
            for (connection in connections) {
                connection.placeable.place(x = connection.x, y = connection.y)
            }
            for (item in items) {
                item.placeable.place(x = item.x, y = item.y)
            }
        }
    }
}

private enum class ConnectedSubSlot {
    CONTENT,
    CONNECTION,
}

private class Item<T>(
    val value: T,
    val x: Int,
    val y: Int,
    val content: @Composable () -> Unit,
) {
    lateinit var placeable: Placeable

    fun toRect(): Rect = Rect(
        left = x.toFloat(),
        top = y.toFloat(),
        right = (x + placeable.width).toFloat(),
        bottom = (y + placeable.height).toFloat(),
    )
}

private class Connection<T>(
    val start: Item<T>,
    val end: Item<T>,
    val x: Int,
    val y: Int,
) {
    lateinit var placeable: Placeable
}

fun connectionPath(
    startRect: Rect,
    startPercent: Float,
    endRect: Rect,
    endPercent: Float,
): Path {
    // TODO ideas
    //  - can probably do this a bit more dynamically; determining closest edges?
    //  - can we add an arrow support, to indicate direction?

    // TODO overload using alignment?
    require(startPercent in 0f..1f && endPercent in 0f..1f)

    fun anchor(rect: Rect, percent: Float): Offset {
        return when {
            percent < 0.25f -> Offset(
                x = rect.left + percent * (rect.right - rect.left) / 0.25f,
                y = rect.top
            )

            percent < 0.50f -> Offset(
                x = rect.right,
                y = rect.top + (percent - 0.25f) * (rect.bottom - rect.top) / 0.25f
            )

            percent < 0.75f -> Offset(
                x = rect.right - (percent - 0.50f) * (rect.right - rect.left) / 0.25f,
                y = rect.bottom
            )

            else -> Offset(
                x = rect.left,
                y = rect.bottom - (percent - 0.75f) * (rect.bottom - rect.top) / 0.25f
            )
        }
    }

    val startOffset = anchor(startRect, startPercent)
    val endOffset = anchor(endRect, endPercent)

    val middle = Offset((startOffset.x + endOffset.x) / 2, (startOffset.y + endOffset.y) / 2)
    fun middle(rect: Rect, percent: Float): Offset {
        return when {
            percent < 0.25f -> Offset(
                x = rect.left + percent * (rect.right - rect.left) / 0.25f,
                y = middle.y
            )

            percent < 0.50f -> Offset(
                x = middle.x,
                y = rect.top + (percent - 0.25f) * (rect.bottom - rect.top) / 0.25f
            )

            percent < 0.75f -> Offset(
                x = rect.right - (percent - 0.50f) * (rect.right - rect.left) / 0.25f,
                y = middle.y
            )

            else -> Offset(
                x = middle.x,
                y = rect.bottom - (percent - 0.75f) * (rect.bottom - rect.top) / 0.25f
            )
        }
    }

    val startNormal = middle(startRect, startPercent)
    val endNormal = middle(endRect, endPercent)

    val path = Path()
    path.moveTo(startOffset.x, startOffset.y)
    path.cubicTo(
        x1 = startNormal.x,
        y1 = startNormal.y,
        x2 = endNormal.x,
        y2 = endNormal.y,
        x3 = endOffset.x,
        y3 = endOffset.y,
    )
    return path
}
