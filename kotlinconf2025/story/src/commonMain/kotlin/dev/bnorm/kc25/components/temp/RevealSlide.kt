package dev.bnorm.kc25.components.temp

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.KodeeScaffold
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

const val BULLET_1 = " • "
const val BULLET_2 = " ◦ "
const val BULLET_3 = " ‣ "

fun StoryboardBuilder.RevealScene(vararg items: String) {
    scene(
        stateCount = items.size,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        KodeeScaffold { padding ->
            Column(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                RevealEach(frame.createChildTransition { it.toState() }) {
                    for (value in items) {
                        item { Text(value) }
                    }
                }
            }
        }
    }
}
