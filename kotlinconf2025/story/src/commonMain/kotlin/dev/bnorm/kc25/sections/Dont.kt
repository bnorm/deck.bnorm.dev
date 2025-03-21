package dev.bnorm.kc25.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit

fun StoryboardBuilder.Dont() {
    slide(
        stateCount = 1,
        enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
        exitTransition = SlideExit(alignment = Alignment.CenterEnd),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            ProvideTextStyle(MaterialTheme.typography.h1.copy(fontWeight = FontWeight.SemiBold)) {
                Text("Don't!")
            }
        }
    }
}

fun StoryboardBuilder.Bueller() {
    slide(
        stateCount = 1,
        enterTransition = SlideEnter(alignment = Alignment.CenterStart),
        exitTransition = SlideExit(alignment = Alignment.CenterStart),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            ProvideTextStyle(MaterialTheme.typography.h1.copy(fontWeight = FontWeight.SemiBold)) {
                Text("Ferris Bueller GIF Here...")
            }
        }
    }
}
