package dev.bnorm.kc25.components.temp

import androidx.compose.animation.core.createChildTransition
import androidx.compose.material.Text
import dev.bnorm.kc25.template.Body
import dev.bnorm.kc25.template.Header
import dev.bnorm.kc25.template.KodeeScene
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach

const val BULLET_1 = " • "
const val BULLET_2 = " ◦ "
const val BULLET_3 = " ‣ "

fun StoryboardBuilder.RevealScene(vararg items: String) {
    KodeeScene(stateCount = items.size) {
        Header()
        Body {
            RevealEach(frame.createChildTransition { it.toState() }) {
                for (value in items) {
                    item { Text(value) }
                }
            }
        }
    }
}
