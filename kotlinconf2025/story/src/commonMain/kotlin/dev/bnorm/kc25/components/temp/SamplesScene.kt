package dev.bnorm.kc25.components.temp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.kc25.components.sampleResource
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.KodeeScaffold
import dev.bnorm.kc25.template.code.toCode
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.SamplesScene(files: List<String>) {
    scene(
        stateCount = files.size,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        KodeeScaffold { padding ->
            val samples = buildList {
                for (sample in files) {
                    add(sampleResource(sample).value)
                }
            }.map { rememberSaveable(it) { it.toCode(INTELLIJ_DARK_CODE_STYLE) } }

            val verticalScrollState = rememberScrollState()
            Box(Modifier.verticalScroll(verticalScrollState).padding(padding)) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    Text(samples[frame.currentState.toState()], modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}