package dev.bnorm.kc25.sections.write.analyze

import androidx.compose.runtime.Composable
import dev.bnorm.kc25.components.validateSample
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.StageSampleScene
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder

private val VALIDATE_SAMPLES = buildCodeSamples {
    val sup by tag("super class")
    val err by tag("")
    val map by tag("")
    val init by tag("")

    // TODO show example of other multi-parameter errors?
    // TODO validate sample

    val base = """
        object BuildableErrors : ${sup}BaseDiagnosticRendererFactory()${sup} {${err}
          val BUILDABLE_MULTIPLE_CONSTRUCTORS by error0<KtAnnotation>()${err}

          override val MAP = KtDiagnosticFactoryToRendererMap("Buildable").apply {${map}
            put(
              BUILDABLE_MULTIPLE_CONSTRUCTORS,
              "'@Buildable' cannot be applied to multiple constructors in the same class."
            )${map}
          }${init}

          init {
            RootDiagnosticRendererFactory.registerFactory(this)
          }${init}
        }
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    base
        .then {base.hide(err, map, init) }
        .then { focus(sup, scroll = false) }
        .then { reveal(init).focus(init, scroll = false) }
        .then { reveal(err).focus(err, scroll = false) }
        .then { reveal(map).focus(map, scroll = false) }
}

@Composable
internal fun validateErrorsSample() {
    validateSample(
        sample = VALIDATE_SAMPLES[0].string,
        file = "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableErrors.kt@BuildableErrors"
    )
}

private val SAMPLES = VALIDATE_SAMPLES.subList(fromIndex = 1, toIndex = VALIDATE_SAMPLES.size)

fun StoryboardBuilder.Errors(sink: MutableList<CodeSample>, start: Int = 0, endExclusive: Int = SAMPLES.size) {
    sink.addAll(SAMPLES)
    StageSampleScene(SAMPLES, CompilerStage.Analyze, start, endExclusive)
}
