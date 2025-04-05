package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.unit.dp
import dev.bnorm.evolved.sections.intro.BULLET_1
import dev.bnorm.evolved.sections.intro.BULLET_3
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach

fun StoryboardBuilder.Goals() {
    scene(
        stateCount = 9,
    ) {
        HeaderAndBody {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProvideTextStyle(MaterialTheme.typography.h5) {
                    val reveal = frame.createChildTransition { it.toState() - 1 }
                    RevealEach(reveal) {
                        item { Text("$BULLET_1 Power-Assert Goals") }
                        item { Text("    $BULLET_3 Shorten feedback loop for tests.") }
                        item { Text("    $BULLET_3 Simplify assertion usage.") }
                        item { Text("    $BULLET_3 Enhance usability for newcomers.") }
                        item { Text("$BULLET_1 Evolution Goals") }
                        item { Text("    $BULLET_3 Simplify onboarding experience.") }
                        item { Text("    $BULLET_3 Improve brittle nature of function call transformation.") }
                        item { Text("    $BULLET_3 Add integration with IDEs.") }
                    }
                }
            }
        }
    }
}

