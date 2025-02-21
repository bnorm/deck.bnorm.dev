package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.evolved.sections.intro.BULLET_1
import dev.bnorm.evolved.sections.intro.BULLET_3
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.enter
import dev.bnorm.storyboard.easel.exit
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit

fun StoryboardBuilder.Oops() {
    slide(
        stateCount = 6,
    ) {
        HeaderAndBody {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProvideTextStyle(MaterialTheme.typography.h4) {
                    Text("What happened?")
                }
                ProvideTextStyle(MaterialTheme.typography.h5) {
                    val reveal = state.createChildTransition { it.toState() - 1 }
                    RevealEach(reveal) {
                        item { Text("$BULLET_1 We designed a general purpose language feature: Explain.") }
                        item { Text("$BULLET_1 We like where it is heading but think it is still immature.") }
                        item { Text("$BULLET_1 Goals remain the same, but we're focusing on Power-Assert.") }
                        item { Text("$BULLET_1 This will allow us quickly evolve and adapt to user feedback.") }
                        item { Text("$BULLET_1 We will continue to develop Explain to replace Power-Assert.") }
                    }
                }
            }
        }
    }
}
