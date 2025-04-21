package dev.bnorm.kc25.sections.write.transform

import androidx.compose.runtime.Composable
import dev.bnorm.kc25.components.validateSample
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.StageSampleScene
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder

private val VALIDATE_SAMPLES = buildCodeSamples {
    val sup by tag("super class")
    val body by tag("class body")
    val gen by tag("generate function")

    val base = """
        class BuildableIrGenerationExtension : ${sup}IrGenerationExtension${sup}${body} {
          override fun generate(
            moduleFragment: IrModuleFragment,
            pluginContext: IrPluginContext,
          ) {${gen}
            val visitor = BuildableIrVisitor(pluginContext)
            moduleFragment.acceptChildrenVoid(visitor)
          ${gen}}
        }${body}
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    val start = base.hide(body).collapse(gen)

    base
        .then { start }
        .then { focus(sup) }
        .then { reveal(body).focus(body, scroll = false) }
        .then { reveal(gen).focus(gen, scroll = false) }
        .then { unfocus() }
}

@Composable
internal fun validateIrGenerationSample() {
    validateSample(
        sample = VALIDATE_SAMPLES[0].string,
        file = "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrGenerationExtension.kt@BuildableIrGenerationExtension"
    )
}

private val SAMPLES = VALIDATE_SAMPLES.subList(fromIndex = 1, toIndex = VALIDATE_SAMPLES.size)

fun StoryboardBuilder.Generation(start: Int = 0, endExclusive: Int = SAMPLES.size) {
    StageSampleScene(SAMPLES, CompilerStage.Transform, start, endExclusive)
}
