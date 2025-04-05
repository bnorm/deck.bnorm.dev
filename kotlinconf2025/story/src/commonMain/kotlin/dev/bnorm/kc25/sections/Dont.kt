package dev.bnorm.kc25.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

fun StoryboardBuilder.Dont() {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            ProvideTextStyle(MaterialTheme.typography.h1.copy(fontWeight = FontWeight.SemiBold)) {
                Text("Don't!")
            }
        }
    }
}

fun StoryboardBuilder.Bueller() {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterStart),
        exitTransition = SceneExit(alignment = Alignment.CenterStart),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            ProvideTextStyle(MaterialTheme.typography.h1.copy(fontWeight = FontWeight.SemiBold)) {
                Text("Ferris Bueller GIF Here...")
            }
        }
    }
}
