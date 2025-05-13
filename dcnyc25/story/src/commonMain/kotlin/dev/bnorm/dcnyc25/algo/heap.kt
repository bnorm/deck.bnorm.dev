package dev.bnorm.dcnyc25.algo

class MinHeap<T : Comparable<T>>() : AbstractMutableCollection<T>() {
    private val heap = ArrayList<T>()

    override fun add(element: T): Boolean {
        heap.add(element)
        bubbleUp(heap.size - 1)
        return true
    }

    fun pop(): T? {
        if (isEmpty()) return null
        if (heap.size == 1) return heap.removeAt(0)

        val root = heap[0]
        heap[0] = heap.removeAt(heap.size - 1)
        bubbleDown(0)
        return root
    }

    fun peek(): T? = heap.firstOrNull()

    override val size: Int
        get() = heap.size

    override fun isEmpty(): Boolean = heap.isEmpty()

    private fun bubbleUp(index: Int) {
        var curr = index
        while (curr > 0) {
            val parent = (curr - 1) / 2
            if (heap[curr] < heap[parent]) {
                swap(curr, parent)
                curr = parent
            } else {
                break
            }
        }
    }

    private fun bubbleDown(index: Int) {
        var curr = index
        while (true) {
            val left = 2 * curr + 1
            val right = 2 * curr + 2
            var min = curr

            if (left < heap.size && heap[left] < heap[min]) {
                min = left
            }
            if (right < heap.size && heap[right] < heap[min]) {
                min = right
            }
            if (min != curr) {
                swap(curr, min)
                curr = min
            } else {
                break
            }
        }
    }

    private fun swap(i: Int, j: Int) {
        val temp = heap[i]
        heap[i] = heap[j]
        heap[j] = temp
    }

    override fun iterator(): MutableIterator<T> {
        TODO("Not yet implemented")
    }
}
