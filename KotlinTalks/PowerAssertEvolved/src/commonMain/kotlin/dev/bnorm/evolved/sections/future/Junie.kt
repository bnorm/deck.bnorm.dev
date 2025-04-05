package dev.bnorm.evolved.sections.future

import androidx.compose.animation.core.createChildTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

fun StoryboardBuilder.Junie() {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("Junie?")
            }
            RevealEach(frame.createChildTransition { it.toState() - 1 }) {
                // TODO anything we want to add here?
            }
        }
    }
}