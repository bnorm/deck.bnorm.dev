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

fun StoryboardBuilder.StageGenerate() {
    // TODO improve details about generate stage

    val items = listOf(
        "$BULLET_1 Converts IR into platform-specific representation.",
        "    $BULLET_2 JVM ByteCode",
        "    $BULLET_2 JavaScript",
        "    $BULLET_2 LLVM IR",
        "    $BULLET_2 Wasm",
        "    $BULLET_2 KLIB", // TODO should i include this?
    )

    StageDetail(stateCount = items.size + 1, stage = CompilerStage.Generate) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            RevealEach(frame.createChildTransition { it.toState() - 1 }) {
                for (value in items) {
                    item { Text(value) }
                }
            }
        }
    }
}
