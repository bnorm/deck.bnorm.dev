package dev.bnorm.librettist.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.librettist.SlideGroupMarker
import dev.bnorm.librettist.SlideGroupScope

@Immutable
class SlideSection(
    val header: @Composable () -> Unit,
) {
    companion object {
        val Empty = SlideSection {}
    }
}

val LocalSlideSection = compositionLocalOf { SlideSection.Empty }

@SlideGroupMarker
fun SlideGroupScope.section(
    title: @Composable () -> Unit,
    block: SlideGroupScope.() -> Unit,
) {
    val upstream = this
    val section = SlideSection(title)

    object : SlideGroupScope {
        override fun slide(content: @Composable () -> Unit) {
            upstream.slide {
                CompositionLocalProvider(LocalSlideSection provides section) {
                    content()
                }
            }
        }
    }.block()
}
