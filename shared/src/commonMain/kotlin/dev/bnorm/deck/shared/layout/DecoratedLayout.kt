package dev.bnorm.deck.shared.layout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced

sealed class DecoratedScope {
    abstract fun Modifier.decorate(key: Any): Modifier

    // TODO this must be used at the top level
    //  - does this need a DslMarker or something similar?
    @Composable
    abstract fun Decoration(
        modifier: Modifier = Modifier,
        onDraw: DrawScope.(DecoratedLayout) -> Unit,
    )
}

interface DecoratedLayout {
    fun getBoundingBox(key: Any): Rect?
}

@Composable
fun DecoratedLayout(
    modifier: Modifier = Modifier,
    content: @Composable DecoratedScope.() -> Unit,
) {
    val root = remember { mutableStateOf<LayoutCoordinates?>(null) }
    Box(modifier.onPlaced { root.value = it }) {
        val container = remember { DecoratedContainer(root, this) }
        container.content()
    }
}

private class DecoratedContainer(
    private val root: State<LayoutCoordinates?>,
    private val boxScope: BoxScope,
) : DecoratedScope(), DecoratedLayout {
    val elements = mutableStateMapOf<Any, LayoutCoordinates>()

    override fun Modifier.decorate(key: Any): Modifier {
        // TODO on detach, clear element
        return onPlaced { elements.put(key, it) }
    }

    @Composable
    override fun Decoration(modifier: Modifier, onDraw: DrawScope.(DecoratedLayout) -> Unit) {
        Canvas(with(boxScope) { modifier.matchParentSize() }) {
            onDraw(this@DecoratedContainer)
        }
    }

    override fun getBoundingBox(key: Any): Rect? {
        val coordinates = elements[key] ?: return null
        return root.value?.localBoundingBoxOf(coordinates)
    }
}
