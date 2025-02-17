package dev.bnorm.evolved.template

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.SlideContent
import dev.bnorm.storyboard.core.StoryboardBuilder

interface StateScope<T> {
    fun next(generator: (previous: T) -> T)
}

fun <T> states(initial: T, block: StateScope<T>.() -> Unit): List<T> = buildList {
    var last = initial
    block(object : StateScope<T> {
        override fun next(generator: (T) -> T) {
            val next = generator(last)
            add(next)
            last = next
        }
    })
}

fun <T> StoryboardBuilder.slide(
    initial: T,
    block: StateScope<T>.() -> Unit,
    enterTransition: (AdvanceDirection) -> EnterTransition = { EnterTransition.None },
    exitTransition: (AdvanceDirection) -> ExitTransition = { ExitTransition.None },
    content: SlideContent<T>,
) = slide(
    states = states(initial, block),
    enterTransition = enterTransition,
    exitTransition = exitTransition,
    content = content,
)
