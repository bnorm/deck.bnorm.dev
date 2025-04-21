package dev.bnorm.kc25.sections.write.analyze

import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.StageSampleScene
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder

private val SAMPLES = buildCodeSamples {
    val sup by tag("super class")
    val err by tag("")
    val map by tag("")
    val init by tag("")

    // TODO show example of other multi-parameter errors?
    // TODO validate sample

    val base = """
        object BuildableErrors : ${sup}BaseDiagnosticRendererFactory()${sup} {${err}
          val BUILDABLE_MULTIPLE_CONSTRUCTORS by error0<KtAnnotation>(
            positioningStrategy = SourceElementPositioningStrategies.DEFAULT,
          )${err}

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

    base.hide(err, map, init)
        .then { focus(sup, scroll = false) }
        .then { reveal(init).focus(init, scroll = false) }
        .then { reveal(err).focus(err, scroll = false) }
        .then { reveal(map).focus(map, scroll = false) }
}

fun StoryboardBuilder.Errors(start: Int = 0, endExclusive: Int = SAMPLES.size) {
    StageSampleScene(SAMPLES, CompilerStage.Analyze, start, endExclusive)
}
