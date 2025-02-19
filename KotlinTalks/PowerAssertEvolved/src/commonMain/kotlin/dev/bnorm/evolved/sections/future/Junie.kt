package dev.bnorm.evolved.sections.future

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit

fun StoryboardBuilder.Junie() {
    slide(
        stateCount = 1,
        enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
        exitTransition = SlideExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("Junie?")
            }
            RevealEach(state.createChildTransition { it.toState() - 1 }) {
                // TODO anything we want to add here?
            }
        }
    }
}