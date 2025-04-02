package dev.bnorm.kc25.template

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.DefaultCornerKodee
import dev.bnorm.deck.shared.SharedKodee
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.Scene
import dev.bnorm.storyboard.core.SceneContent
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

fun <T> StoryboardBuilder.KodeeScene(
    states: List<T>,
    enterTransition: (AdvanceDirection) -> EnterTransition = SceneEnter(alignment = Alignment.CenterEnd),
    exitTransition: (AdvanceDirection) -> ExitTransition = SceneExit(alignment = Alignment.CenterEnd),
    content: SceneContent<T>,
): Scene<T> = scene(states, enterTransition, exitTransition) {
    Column(Modifier.fillMaxSize()) {
        content()
    }

    SharedKodee {
        DefaultCornerKodee(Modifier.size(50.dp))
    }
}

fun StoryboardBuilder.KodeeScene(
    stateCount: Int = 1,
    enterTransition: (AdvanceDirection) -> EnterTransition = SceneEnter(alignment = Alignment.CenterEnd),
    exitTransition: (AdvanceDirection) -> ExitTransition = SceneExit(alignment = Alignment.CenterEnd),
    content: SceneContent<Int>,
): Scene<Int> = scene(stateCount, enterTransition, exitTransition) {
    Column(Modifier.fillMaxSize()) {
        content()
    }

    SharedKodee {
        DefaultCornerKodee(Modifier.size(50.dp))
    }
}
