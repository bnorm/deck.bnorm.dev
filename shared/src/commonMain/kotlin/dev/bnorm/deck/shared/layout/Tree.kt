package dev.bnorm.deck.shared.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.*
import kotlin.math.abs

private enum class TreeSubSlot {
    CONTENT,
    CONNECTION,
}

private class TreeNode<T>(
    val value: T,
    val depth: Int,
    val children: List<TreeNode<T>>,
) {
    var parent: TreeNode<T>? = null
    var curve: Placeable? = null

    lateinit var placeable: Placeable
    var x = 0
    var y = 0

    var minHeight = 0
}

@Composable
fun <T> HorizontalTree(
    root: T,
    getChildren: (node: T) -> Collection<T>,
    modifier: Modifier = Modifier,

    // How the nodes are spaced out horizontally.
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(64.dp),
    // How child nodes are aligned with each other.
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    // Ensures the size of the tree will - at minimum - support this spacing between horizontal nodes.
    // Useful when combined with non-SpacedBy arrangements.
    // TODO is this actually useful?
    horizontalMinimumSpacing: Dp = 0.dp,

    // How nodes are spaced out vertically.
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    // How parent nodes are aligned to their children.
    // Also, how children with a taller parent are aligned.
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    // Ensures the size of the tree will - at minimum - support this spacing between horizontal nodes.
    // Useful when combined with non-SpacedBy arrangements.
    // TODO is this actually useful?
    verticalMinimumSpacing: Dp = 0.dp,

    // TODO support packing?
    //  - support top/bottom packing nodes at the same depth?
    //  - support left/right pack nodes against their parent? null horizontalArrangement?
    //  - these may need to be mutually-exclusive?

    // TODO support staggering nodes at the same depth?
    //  - more important in a top-down tree

    connection: @Composable (parent: T, parentRect: IntRect, child: T, childRect: IntRect) -> Unit = { _, _, _, _ -> },
    content: @Composable (node: T) -> Unit,
) {
    SubcomposeLayout(modifier) { constraints ->
        fun collect(sink: MutableList<TreeNode<T>>, value: T, depth: Int): TreeNode<T> {
            val children = getChildren(value).map { collect(sink, it, depth + 1) }
            val node = TreeNode(value, depth, children)
            for (child in children) child.parent = node

            sink.add(node)
            return node
        }

        val nodes = mutableListOf<TreeNode<T>>()
        val root = collect(nodes, root, 0)

        val byDepth = Array<MutableList<TreeNode<T>>>(nodes.maxOf { it.depth } + 1) { mutableListOf() }
        for (node in nodes) {
            byDepth[node.depth].add(node)
        }

        subcompose(TreeSubSlot.CONTENT) {
            for (node in nodes) {
                content(node.value)
            }
        }.forEachIndexed { index, measurable ->
            nodes[index].placeable = measurable.measure(Constraints())
        }

        // ====================================
        // Horizontal arrangement and alignment
        // ====================================
        val xSpacing = maxOf(horizontalArrangement.spacing, horizontalMinimumSpacing).roundToPx()

        val xSizes = IntArray(byDepth.size) { byDepth[it].maxOf { it.placeable.width } }
        val minWidth = maxOf(xSizes.sumOf { it + xSpacing } - xSpacing, constraints.minWidth)

        val xPositions = IntArray(byDepth.size)
        with(horizontalArrangement) { arrange(minWidth, xSizes, layoutDirection, xPositions) }
        for ((i, nodes) in byDepth.withIndex()) {
            val size = xSizes[i]
            for (node in nodes) {
                node.x = xPositions[i] + horizontalAlignment.align(node.placeable.width, size, layoutDirection)
            }
        }

        // ==================================
        // Vertical arrangement and alignment
        // ==================================
        // TODO there's still some weird things go on here:
        //  - SpacedBetween looks weird
        //  - SpaceEvenly results in double space between cousins
        // TODO do we need to use arrangement within minHeight calculation?

        val ySpacing = maxOf(verticalArrangement.spacing, verticalMinimumSpacing).roundToPx()

        fun heightUp(node: TreeNode<T>): Int {
            var childHeight = -ySpacing
            for (node in node.children) {
                childHeight += heightUp(node) + ySpacing
            }
            node.minHeight = maxOf(node.placeable.height, childHeight)
            return node.minHeight
        }

        fun heightDown(node: TreeNode<T>, height: Int) {
            val original = node.minHeight
            node.minHeight = height

            val childSpacing = ySpacing * (node.children.size - 1)
            for (child in node.children) {
                val childHeight = (child.minHeight - childSpacing) * height / original + childSpacing
                child.minHeight = childHeight
                heightDown(child, childHeight)
            }
        }

        fun alignChildren(children: List<TreeNode<T>>, yOffset: Int, height: Int) {
            if (children.isEmpty()) return

            val ySizes = IntArray(children.size) { children[it].minHeight }
            val yPositions = IntArray(children.size)
            with(verticalArrangement) { arrange(height, ySizes, yPositions) }

            val childrenOffset = if (verticalArrangement.spacing.value > 0f) {
                var min = height
                var max = 0
                for (i in children.indices) {
                    min = minOf(min, yPositions[i])
                    max = maxOf(max, yPositions[i] + ySizes[i])
                }
                verticalAlignment.align(max - min, height)
            } else {
                0
            }

            for (i in yPositions.indices) {
                val child = children[i]
                val yPosition = yOffset + yPositions[i]

                val childOffset = if (child.placeable.height < child.minHeight) {
                    verticalAlignment.align(child.placeable.height, child.minHeight)
                } else {
                    0
                }

                child.y = yPosition + childrenOffset + childOffset
                alignChildren(child.children, yPosition, ySizes[i])
            }
        }

        var height = heightUp(root)
        if (verticalArrangement.spacing.value <= 0f) {
            height = maxOf(height, constraints.minHeight)
            heightDown(root, height)
        }

        alignChildren(listOf(root), 0, height)

        subcompose(TreeSubSlot.CONNECTION) {
            // Only the last node should not have a parent.
            for (child in nodes) {
                val parent = child.parent ?: break

                val offset = IntOffset(parent.x, minOf(parent.y, child.y))
                val parentRect = IntRect(
                    left = parent.x,
                    top = parent.y,
                    right = parent.x + parent.placeable.width,
                    bottom = parent.y + parent.placeable.height,
                )
                val childRect = IntRect(
                    left = child.x,
                    top = child.y,
                    right = child.x + child.placeable.width,
                    bottom = child.y + child.placeable.height,
                )
                connection(parent.value, parentRect.translate(-offset), child.value, childRect.translate(-offset))
            }
        }.forEachIndexed { index, measurable ->
            val child = nodes[index]
            val parent = child.parent!!

            val xStart = parent.x
            val xEnd = child.x + child.placeable.width
            val yStart = minOf(parent.y, child.y)
            val yEnd = maxOf(parent.y + parent.placeable.height, child.y + child.placeable.height)
            val constraints = Constraints.fixed(width = abs(xEnd - xStart), height = abs(yEnd - yStart))
            nodes[index].curve = measurable.measure(constraints)
        }

        layout(minWidth, height) {
            for (node in nodes) {
                node.placeable.place(x = node.x, y = node.y)

                val parent = node.parent ?: continue
                val curve = node.curve!!
                curve.place(x = parent.x, y = minOf(parent.y, node.y))
            }
        }
    }
}
