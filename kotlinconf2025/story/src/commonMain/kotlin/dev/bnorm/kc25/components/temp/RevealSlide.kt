package dev.bnorm.kc25.components.temp

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.HeaderScaffold
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

const val BULLET_1 = " • "
const val BULLET_2 = " ◦ "
const val BULLET_3 = " ‣ "

fun StoryboardBuilder.RevealScene(vararg items: String) {
    RevealScene(items.map { AnnotatedString(it) })
}

fun StoryboardBuilder.RevealScene(vararg items: AnnotatedString) {
    RevealScene(items.asList())
}

fun StoryboardBuilder.RevealScene(items: List<AnnotatedString>) {
    scene(
        stateCount = items.size,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderScaffold { padding ->
            Column(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                RevealEach(transition.createChildTransition { it.toState() }) {
                    for (value in items) {
                        item { Text(value) }
                    }
                }
            }
        }
    }
}
