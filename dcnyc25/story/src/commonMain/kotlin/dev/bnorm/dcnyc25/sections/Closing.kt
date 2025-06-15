package dev.bnorm.dcnyc25.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.dcnyc25.template.Full
import dev.bnorm.dcnyc25.template.OutlinedText
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

fun StoryboardBuilder.Closing() {
    scene(
        stateCount = 2,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Full(MaterialTheme.colors.primary) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedText("Fin", style = MaterialTheme.typography.h1)

                    Conclusion(step = 1) {
                        OutlinedText(
                            text = "(github.com/bnorm/storyboard)",
                            style = MaterialTheme.typography.h5.copy(fontFamily = JetBrainsMono)
                        )
                    }
                }
            }
        }
    }
}
