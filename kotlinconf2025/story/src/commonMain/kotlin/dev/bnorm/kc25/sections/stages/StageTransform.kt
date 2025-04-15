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

fun StoryboardBuilder.StageTransform() {
    // TODO improve details about transform stage

    val items = listOf(
        "$BULLET_1 Semantics",
        "    $BULLET_2 IR is a tree-based representation of the *semantics* of Kotlin code.",
        "    $BULLET_2 IR helps to convert high-level concepts into low-level process.",
        "    $BULLET_2 For example, suspend functions need to be lowered into a state machine.",
        "$BULLET_1 Lowerings",
        "    $BULLET_2 Performed in phases that each reduce or optimize some part of Kotlin code.",
    )

    StageDetail(stateCount = items.size + 1, stage = CompilerStage.Transform) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            RevealEach(frame.createChildTransition { it.toState() - 1 }) {
                for (value in items) {
                    item { Text(value) }
                }
            }
        }
    }
}
