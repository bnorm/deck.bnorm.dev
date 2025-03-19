package dev.bnorm.kc25.components.temp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.sampleResource
import dev.bnorm.kc25.template.HeaderAndBody
import dev.bnorm.kc25.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit

fun StoryboardBuilder.SamplesSlide(files: List<String>) {
    slide(
        stateCount = files.size,
        enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
        exitTransition = SlideExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderAndBody {
            val samples = buildList {
                for (sample in files) {
                    add(sampleResource(sample).value.toCode())
                }
            }

            val verticalScrollState = rememberScrollState()
            Box(modifier = Modifier.Companion.verticalScroll(verticalScrollState)) {
                Text(samples[currentState], modifier = Modifier.Companion.fillMaxSize().padding(32.dp))
            }
        }
    }
}