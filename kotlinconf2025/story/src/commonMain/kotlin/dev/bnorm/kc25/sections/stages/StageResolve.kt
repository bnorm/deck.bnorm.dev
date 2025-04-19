package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.components.temp.BULLET_2
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.StageResolve() {
    // TODO improve details about resolve stage

    val items = listOf(
        "$BULLET_1 FIR is resolved in a sequence of phases.",
        "$BULLET_1 Each phase resolves a different part of the FIR structure.",
        "$BULLET_1 Order is extremely important, as phases build on each other.",
    )

    // TODO can we show an example of why types are resolved in a specific order?
    //  - super-type
    //  - return type and parameter types
    //  - local variables

    StageDetail(stateCount = items.size + 1, stage = CompilerStage.Resolve) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            RevealEach(frame.createChildTransition { it.toState() - 1 }) {
                for (value in items) {
                    item { Text(value) }
                }
            }
        }
    }
}
