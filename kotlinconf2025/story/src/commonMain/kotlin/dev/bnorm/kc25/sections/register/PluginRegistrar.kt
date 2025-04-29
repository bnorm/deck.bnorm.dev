package dev.bnorm.kc25.sections.register

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.StageScaffold
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState

private val SAMPLES = buildCodeSamples {
    val sc by tag("super class")
    val cb by tag("class body")
    val k2 by tag("supportsK2 property")
    val k2b by tag("supportsK2 property getter")
    val re by tag("registerExtensions function")
    val reb by tag("registerExtensions function body")
    val fir by tag("FirExtensionRegistrarAdapter.registerExtension")
    val ir by tag("IrGenerationExtension.registerExtension")

    val baseSample = """
        @OptIn(ExperimentalCompilerApi::class)
        class MyCompilerPluginRegistrar : ${sc}CompilerPluginRegistrar${sc}() {${cb}
          ${k2}override val supportsK2: Boolean${k2b} get() = true${k2b}${k2}

          ${re}override fun ExtensionStorage.registerExtensions(
            configuration: CompilerConfiguration,
          )${reb} {
            ${fir}FirExtensionRegistrarAdapter.registerExtension(
              MyFirExtensionRegistrar()
            )${fir}${ir}
            IrGenerationExtension.registerExtension(
              MyIrGenerationExtension()
            )${ir}
          }${reb}${re}
        ${cb}}
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    // TODO is there a better way to do this transition?
    //  - maybe similar to FIR tree where it's part of the diagram?
    val name = CodeSample(AnnotatedString("CompilerPluginRegistrar"))
    name
        .then { baseSample.collapse(cb).hide(k2b, reb, ir) }
        .then { reveal(cb).focus(cb) }
        .then { focus(k2) }
        .then { reveal(k2b) }
        .then { focus(re) }
        .then { reveal(reb).focus(fir) }
        .then { reveal(ir).focus(ir) }
        .then { focus(re) }
        .then { name }
}

fun StoryboardBuilder.PluginRegistrar(sink: MutableList<CodeSample>, start: Int = 0, endExclusive: Int = SAMPLES.size) {
    require(start < endExclusive) { "start=$start must be less than endExclusive=$endExclusive" }
    require(start >= 0) { "start=$start must be greater than or equal to 0" }
    require(endExclusive <= SAMPLES.size) { "end must be less than or equal to ${SAMPLES.size}" }
    sink.addAll(SAMPLES)

    scene(
        stateCount = endExclusive - start,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        StageScaffold { padding ->
            Box(
                Modifier.padding(padding)
                    .padding(bottom = 32.dp)
                    .sharedElement(
                        rememberSharedContentState("box:${Component.CompilerPluginRegistrar}"),
                        boundsTransform = BoxMovementSpec,
                        zIndexInOverlay = -1f,
                    )
                    .fillMaxSize()
                    .border(2.dp, MaterialTheme.colors.secondary, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    val text = frame.createChildTransition {
                        SAMPLES[start + it.toState()].string.splitByTags()
                    }
                    MagicText(
                        text,
                        modifier = Modifier
                            .sharedBounds(
                                rememberSharedContentState("text:${Component.CompilerPluginRegistrar}"),
                                boundsTransform = TextMovementSpec,
                                zIndexInOverlay = -1f,
                            )
                    )
                }
            }
        }
    }
}
