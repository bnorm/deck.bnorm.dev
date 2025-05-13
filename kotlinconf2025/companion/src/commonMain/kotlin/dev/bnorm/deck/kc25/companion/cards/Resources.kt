package dev.bnorm.deck.kc25.companion.cards

import androidx.compose.animation.AnimatedVisibility
import dev.bnorm.deck.kc25.companion.TitleContent
import dev.bnorm.deck.kc25.companion.utils.Link
import dev.bnorm.storyboard.Storyboard

val Resources = TitleContent("Resources") { index ->
    AnimatedVisibility(index >= Storyboard.Index(2, 0)) {
        Link(
            text = "Writing Your First Kotlin Compiler Plugin",
            url = "https://www.youtube.com/watch?v=w-GMlaziIyo",
        )
    }
    AnimatedVisibility(index >= Storyboard.Index(2, 3)) {
        Link(
            text = "Writing Your Second Kotlin Compiler Plugin",
            url = "https://blog.bnorm.dev/writing-your-second-compiler-plugin-part-1",
        )
    }
    AnimatedVisibility(index >= Storyboard.Index(2, 6)) {
        Link(
            text = "K2 Compiler Plugins",
            url = "https://www.youtube.com/watch?v=Pl-89n9wDqo",
        )
    }
}
