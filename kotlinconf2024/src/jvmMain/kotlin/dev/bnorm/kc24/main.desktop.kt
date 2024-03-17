package dev.bnorm.kc24

import androidx.compose.ui.window.application
import dev.bnorm.librettist.DesktopSlideShow
import dev.bnorm.librettist.show.ShowBuilder

fun main() {
    application {
        DesktopSlideShow(
            title = "Kotlin + Power-Assert = Love",
            theme = Theme.dark,
            builder = ShowBuilder::KotlinPlusPowerAssertEqualsLove
        )
    }
}
