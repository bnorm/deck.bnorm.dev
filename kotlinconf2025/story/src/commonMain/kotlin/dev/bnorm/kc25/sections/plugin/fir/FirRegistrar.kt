package dev.bnorm.kc25.sections.plugin.fir

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.KodeeScaffold
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState

enum class SampleCheckpoint {
    DeclarationGenerationExtension,
    AdditionalCheckersExtension,
}

private val SAMPLES = buildCodeSamples {
    val name by tag("class name")
    val sup by tag("super class")
    val body by tag("class body")
    val sig by tag("configurePlugin signature")
    val dge by tag("declaration generation extension")
    val ace by tag("additional checkers extension")

    val baseSample = """
        class ${name}BuildableFirExtensionRegistrar${name} : ${sup}FirExtensionRegistrar()${sup} {${body}
          ${sig}override fun ExtensionRegistrarContext.configurePlugin()${sig} {${dge}
            +::BuildableFirDeclarationGenerationExtension${dge}${ace}
            +::BuildableFirAdditionalCheckersExtension${ace}
          }
        ${body}}
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    // TODO validate against plugin project
    baseSample.collapse(body).hide(dge, ace).focus(name)
        .then { focus(sup) }
        .then { reveal(body).focus(sig) }
        .then { reveal(dge).focus(dge).attach(SampleCheckpoint.DeclarationGenerationExtension) }
        .then { reveal(ace).focus(ace).attach(SampleCheckpoint.AdditionalCheckersExtension) }
}

val FIR_REGISTRATION_DGE_CHECKPOINT = 1 +
        SAMPLES.indexOfFirst { it.data == SampleCheckpoint.DeclarationGenerationExtension }

fun StoryboardBuilder.FirRegistrar(start: Int = 0, endExclusive: Int = SAMPLES.size) {
    require(start < endExclusive) { "start=$start must be less than endExclusive=$endExclusive" }
    require(start >= 0) { "start=$start must be greater than or equal to 0" }
    require(endExclusive <= SAMPLES.size) { "end must be less than or equal to ${SAMPLES.size}" }

    scene(
        stateCount = endExclusive - start,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        KodeeScaffold { padding ->
            Box(Modifier.padding(padding)) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    val text = frame.createChildTransition {
                        SAMPLES[start + it.toState()].string.splitByTags()
                    }
                    MagicText(text)
                }
            }
        }
    }
}
