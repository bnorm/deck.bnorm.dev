package dev.bnorm.dcnyc25.algo

import androidx.compose.ui.unit.IntOffset
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.abs

typealias SearchPath = PersistentList<IntOffset>

interface SearchCallbacks {
    fun onHead(path: SearchPath) {}
    fun onRight(path: SearchPath) {}
    fun onDown(path: SearchPath) {}
    fun onDiag(path: SearchPath) {}

    object NoOp : SearchCallbacks
}

private enum class SearchDirection {
    Right, Down, Diagonal
}

fun <T> myers(
    start: List<T>,
    end: List<T>,
    callbacks: SearchCallbacks = SearchCallbacks.NoOp,
): SearchPath {
    val target = IntOffset(start.size, end.size)
    val initial = IntOffset(0, 0)

    class SearchNode(
        val path: SearchPath,
        val cost: Int,
        val direction: SearchDirection? = null,
    ) : Comparable<SearchNode> {
        val distance = (target - path.last()).let { abs(it.x) + abs(it.y) }

        override fun compareTo(other: SearchNode): Int {
            return compareValues(cost, other.cost)
        }

        fun right(): SearchNode? {
            val last = path.last()
            if (last.x == start.size) return null
            val extra = if (direction == SearchDirection.Down) 1 else 0
            return SearchNode(
                path = path.add(last.let { IntOffset(it.x + 1, it.y) }),
                cost = cost + 1 + extra,
                direction = SearchDirection.Right
            )
        }

        fun down(): SearchNode? {
            val last = path.last()
            if (last.y == end.size) return null
            val extra = if (direction == SearchDirection.Right) 1 else 0
            return SearchNode(
                path = path.add(last.let { IntOffset(it.x, it.y + 1) }),
                cost = cost + 1 + extra,
                direction = SearchDirection.Down
            )
        }

        fun diag(): SearchNode? {
            val last = path.last()
            if (last.x == start.size || last.y == end.size) return null
            if (start[last.x] != end[last.y]) return null
            return SearchNode(
                path = path.add(last.let { IntOffset(it.x + 1, it.y + 1) }),
                cost = cost,
                direction = SearchDirection.Diagonal
            )
        }

        override fun toString(): String {
            return "$cost $distance $path"
        }
    }

    val queue = MinHeap<SearchNode>()
    queue.add(SearchNode(persistentListOf(initial), 0))

    val best = mutableMapOf<IntOffset, SearchNode>()

    while (true) {
        val head = queue.pop() ?: error("!")
        callbacks.onHead(head.path)
        if (head.path.last() == target) return head.path


        head.diag()?.also { diag ->
            callbacks.onDiag(diag.path)
            val last = diag.path.last()
            if (diag.cost < (best[last]?.cost ?: Int.MAX_VALUE)) {
                best[last] = diag
                queue.add(diag)
            }
        }

        head.right()?.also { right ->
            callbacks.onRight(right.path)
            val last = right.path.last()
            if (right.cost < (best[last]?.cost ?: Int.MAX_VALUE)) {
                best[last] = right
                queue.add(right)
            }
        }

        head.down()?.also { down ->
            callbacks.onDown(down.path)
            val last = down.path.last()
            if (down.cost < (best[last]?.cost ?: Int.MAX_VALUE)) {
                best[last] = down
                queue.add(down)
            }
        }
    }
}
