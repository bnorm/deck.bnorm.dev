package dev.bnorm.kc25.template

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.MagicCodeSample
import dev.bnorm.kc25.components.RightPanel
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

data class RightPanel(
    val sampleIndex: Int,
    val samples: List<CodeSample>,
    val show: Boolean,
) {
    fun show(): RightPanel = copy(show = true)
    fun showNext(): RightPanel = copy(sampleIndex = (sampleIndex + 1) % samples.size, show = true)
    fun next(): RightPanel = copy(sampleIndex = (sampleIndex + 1) % samples.size)
}

fun StoryboardBuilder.StageSampleScene(
    samples: List<CodeSample>,
    stage: CompilerStage?,
    start: Int = 0,
    endExclusive: Int = samples.size,
) {
    require(start < endExclusive) { "start=$start must be less than endExclusive=$endExclusive" }
    require(start >= 0) { "start=$start must be greater than or equal to 0" }
    require(endExclusive <= samples.size) { "end must be less than or equal to ${samples.size}" }

    scene(
        stateCount = endExclusive - start,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        val sample = frame.createChildTransition { samples[start + it.toState()] }

        StageScaffold(updateTransition(stage)) { padding ->
            ProvideTextStyle(MaterialTheme.typography.code1) {
                sample.MagicCodeSample(modifier = Modifier.padding(padding))
            }

            val sidePanel = sample.createChildTransition { (it.data as? RightPanel) }
            val sidePanelVisible = sidePanel.createChildTransition { it != null && it.show }
            val sidePanelSample =
                sidePanel.createChildTransition { it?.samples[it.sampleIndex] ?: CodeSample(AnnotatedString("")) }
            RightPanel(
                show = sidePanelVisible,
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
            ) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    Box(
                        modifier = Modifier.padding(top = 32.dp, start = 32.dp).width(500.dp).fillMaxHeight()
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.code1) {
                            sidePanelSample.MagicCodeSample()
                        }
                    }
                }
            }
        }
    }
}
