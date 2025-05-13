package dev.bnorm.deck.kc25.companion.cards

import androidx.compose.animation.AnimatedVisibility
import dev.bnorm.deck.kc25.companion.TitleContent
import dev.bnorm.deck.kc25.companion.utils.Link
import dev.bnorm.storyboard.Storyboard

val CompilerPlugins = TitleContent("Compiler Plugins") { index ->
    // TODO add more compiler plugin links
    Link(
        text = "KSP",
        url = "https://kotlinlang.org/docs/ksp-overview.html",
    )

    AnimatedVisibility(index >= Storyboard.Index(5, 0)) {
        Link(
            text = "All-Open / Spring Boot",
            url = "https://kotlinlang.org/docs/all-open-plugin.html",
        )
    }
    AnimatedVisibility(index >= Storyboard.Index(6, 0)) {
        Link(
            text = "Serialization",
            url = "https://kotlinlang.org/docs/serialization.html",
        )
    }
    AnimatedVisibility(index >= Storyboard.Index(7, 0)) {
        Link(
            text = "Compose",
            url = "https://www.jetbrains.com/compose-multiplatform/",
        )
    }
    AnimatedVisibility(index >= Storyboard.Index(8, 0)) {
        Link(
            text = "DataFrame",
            url = "https://kotlin.github.io/dataframe/overview.html",
        )
    }
    AnimatedVisibility(index >= Storyboard.Index(9, 0)) {
        Link(
            text = "Power-Assert",
            url = "https://kotlinlang.org/docs/power-assert.html",
        )
    }
}
