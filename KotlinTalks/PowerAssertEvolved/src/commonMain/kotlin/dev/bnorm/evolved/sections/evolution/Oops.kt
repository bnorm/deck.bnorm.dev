package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.unit.dp
import dev.bnorm.evolved.sections.intro.BULLET_1
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.scene
import dev.bnorm.storyboard.easel.template.RevealEach

fun StoryboardBuilder.Oops() {
    scene(
        stateCount = 6,
    ) {
        HeaderAndBody {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProvideTextStyle(MaterialTheme.typography.h4) {
                    Text("What happened?")
                }
                ProvideTextStyle(MaterialTheme.typography.h5) {
                    val reveal = frame.createChildTransition { it.toState() - 1 }
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
