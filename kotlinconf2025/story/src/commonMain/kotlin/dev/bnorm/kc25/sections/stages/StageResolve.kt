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
        "$BULLET_1 Phases",
        "    $BULLET_2 Resolution is performed in a sequence of phases.",
        "    $BULLET_2 Each phase is responsible for resolving a different part of the FIR structure.",
        "    $BULLET_2 For example, the `FirResolvePhase.SUPER_TYPES` phase resolves super types of all classes.",
        "    $BULLET_2 Order is extremely important, as phases build on each other to completely resolve Kotlin code.",
        "    $BULLET_2 For example, function parameter types need to be resolved before call arguments types can be resolved.",
    )

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
