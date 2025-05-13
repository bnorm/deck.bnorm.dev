package dev.bnorm.deck.kc25.companion.cards

import androidx.compose.animation.AnimatedVisibility
import dev.bnorm.deck.kc25.companion.TitleContent
import dev.bnorm.deck.kc25.companion.utils.Link
import dev.bnorm.storyboard.Storyboard

val Stages = TitleContent("Stages") { index ->
    AnimatedVisibility(index >= Storyboard.Index(13, 1)) {
        Link(
            text = "FIR",
            url = "https://github.com/JetBrains/kotlin/blob/v2.2.0-Beta1/docs/fir/fir-basics.md",
        )
    }
}
