package dev.bnorm.dcnyc25.old.kc24

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class AnimationSequence<T>(
    val start: T,
    val end: T,
    val sequence: Sequence<T>,
) {
    fun toList(): ImmutableList<T> {
        return sequence.deduplicate().toImmutableList()
    }
}

fun <T> startAnimation(start: T): AnimationSequence<T> {
    val sequence = sequence { yield(start) }
    return AnimationSequence(start, start, sequence)
}

fun <T> Sequence<T>.deduplicate(): Sequence<T> {
    val upstream = this
    return sequence {
        var prev: Any? = Any()
        for (it in upstream) {
            if (it != prev) {
                yield(it)
            }
            prev = it
        }
    }
}
