package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.StageParse() {
    // TODO improve details about parse stage

    val items = listOf(
        "$BULLET_1 When Kotlin code is parsed it is converted into FIR.",
        "$BULLET_1 FIR is a tree-based representation of the *structure* of Kotlin code.",
    )

    StageDetail(stateCount = items.size + 1 + 14, stage = CompilerStage.Parse) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            RevealEach(frame.createChildTransition { it.toState() - 1 }) {
                for (value in items) {
                    item { Text(value) }
                }
            }
            if (frame.currentState.toState() > items.size) {
                frame.createChildTransition { it.toState() - items.size - 1 }.FirTree()
            }
        }
    }
}
