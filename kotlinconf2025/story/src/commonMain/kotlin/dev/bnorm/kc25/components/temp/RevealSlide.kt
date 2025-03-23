package dev.bnorm.kc25.components.temp

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.HeaderAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.scene
import dev.bnorm.storyboard.easel.template.RevealEach

const val BULLET_1 = " • "
const val BULLET_2 = " ◦ "
const val BULLET_3 = " ‣ "

fun StoryboardBuilder.RevealScene(vararg items: String) {
    scene(stateCount = items.size) {
        HeaderAndBody {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(32.dp),
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
