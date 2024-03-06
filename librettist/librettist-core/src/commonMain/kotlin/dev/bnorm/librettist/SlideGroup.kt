package dev.bnorm.librettist

import androidx.compose.runtime.*

// TODO to better support jump-to-slide and an overview, we probably cannot support sub-groups as we are right now,
//  since we will need the entire list of slides to show them all and be able to jump to the right one.

@DslMarker
annotation class SlideGroupMarker

@SlideGroupMarker
interface SlideGroupScope {
    @SlideGroupMarker
    fun slide(content: @Composable () -> Unit)
}
