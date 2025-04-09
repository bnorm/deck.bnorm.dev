package dev.bnorm.kc25.sections.plugin

import androidx.compose.animation.core.createChildTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import dev.bnorm.kc25.template.*
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState

private const val FILE = "buildable/compiler-plugin/dev/bnorm/buildable/plugin/BuildableCompilerPluginRegistrar.kt"

private val SAMPLES = buildCodeSamples {
    val sc by tag("super class")
    val cb by tag("class body")
    val k2 by tag("supportsK2 property")
    val k2b by tag("supportsK2 property getter")
    val re by tag("registerExtensions function")
    val reb by tag("registerExtensions function body")
    val ir by tag("IrGenerationExtension.registerExtension")

    val baseSample = """
        @OptIn(ExperimentalCompilerApi::class)
        class BuildableCompilerPluginRegistrar : ${sc}CompilerPluginRegistrar()${sc} {${cb}
          ${k2}override val supportsK2: Boolean${k2b} get() = true${k2b}${k2}

          ${re}override fun ExtensionStorage.registerExtensions(
            configuration: CompilerConfiguration,
          )${reb} {
            FirExtensionRegistrarAdapter.registerExtension(
              BuildableFirExtensionRegistrar()
            )${ir}
            IrGenerationExtension.registerExtension(
              BuildableIrGenerationExtension()
            )${ir}
          }${reb}${re}
        ${cb}}
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

//    CodeSample {
//        validateSampleResource(baseSample, "$FILE@BuildableCompilerPluginRegistrar")
//            .toCode()
//    }
    baseSample.collapse(cb).hide(k2b, reb, ir)
        .then { focus(sc) }
        .then { reveal(cb).focus(cb) }
        .then { focus(k2) }
        .then { reveal(k2b) }
        .then { focus(re) }
        .then { reveal(reb) }
        .then { unfocus() }
        .then { focus(re) }
        .then { reveal(ir) }
        .then { unfocus() }
}

const val REGISTRATION_IR_CHECKPOINT = 7

fun StoryboardBuilder.Registration(start: Int = 0, endExclusive: Int = SAMPLES.size) {
    require(start < endExclusive) { "start=$start must be less than endExclusive=$endExclusive" }
    require(start >= 0) { "start=$start must be greater than or equal to 0" }
    require(endExclusive <= SAMPLES.size) { "end must be less than or equal to ${SAMPLES.size}" }

    KodeeScene(stateCount = endExclusive - start) {
        Header()
        Body {
            ProvideTextStyle(MaterialTheme.typography.code1) {
                val text = frame.createChildTransition {
                    SAMPLES[start + it.toState()].string.splitByTags()
                }
                MagicText(text)
            }
        }
    }
}
