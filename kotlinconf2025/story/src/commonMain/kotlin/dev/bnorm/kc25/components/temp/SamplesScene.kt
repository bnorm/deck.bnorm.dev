package dev.bnorm.kc25.components.temp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.kc25.components.sampleResource
import dev.bnorm.kc25.template.Body
import dev.bnorm.kc25.template.Header
import dev.bnorm.kc25.template.KodeeScene
import dev.bnorm.kc25.template.code.toCode
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.currentState
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

fun StoryboardBuilder.SamplesScene(files: List<String>) {
    KodeeScene(
        stateCount = files.size,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Header()
        Body {
            val samples = buildList {
                for (sample in files) {
                    add(sampleResource(sample).value.toCode())
                }
            }

            val verticalScrollState = rememberScrollState()
            ProvideTextStyle(MaterialTheme.typography.code1) {
                Box(modifier = Modifier.verticalScroll(verticalScrollState)) {
                    Text(samples[currentState], modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}