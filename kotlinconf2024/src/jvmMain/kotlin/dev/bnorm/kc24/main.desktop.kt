  package dev.bnorm.kc24

import dev.bnorm.librettist.DesktopSlideShow
import dev.bnorm.librettist.show.ShowBuilder

fun main() {
    DesktopSlideShow(
        title = "Kotlin + Power-Assert = Love",
        theme = { Theme.dark },
        builder = ShowBuilder::KotlinPlusPowerAssertEqualsLove
    )
}
