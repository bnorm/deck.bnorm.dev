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
    val dec by tag("declaration checkers")
    val cls by tag("class checkers")
    val chk by tag("buildable constructor checker")

    // TODO show example of other checker configuration?
    // TODO validate sample

    val base = """
        class BuildableFirAdditionalCheckersExtension(
          session: FirSession,
        ) : ${sup}FirAdditionalCheckersExtension(session)${sup}${dec} {
          override val declarationCheckers = object : DeclarationCheckers() {${cls}
            override val classCheckers: Set<FirClassChecker>
              get() = setOf(${chk}
                BuildableConstructorChecker,
              ${chk})
          ${cls}}
        }${dec}
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    base
        .then { base.hide(dec, cls, chk) }
        .then { focus(sup, scroll = false) }
        .then { reveal(dec).focus(dec, scroll = false) }
        .then { reveal(cls).focus(cls, scroll = false) }
        .then { reveal(chk).focus(chk, scroll = false) }
}

@Composable
internal fun validateCheckerExtensionSample() {
    validateSample(
        sample = VALIDATE_SAMPLES[0].string,
        file = "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirAdditionalCheckersExtension.kt@BuildableFirAdditionalCheckersExtension"
    )
}

private val SAMPLES = VALIDATE_SAMPLES.subList(fromIndex = 1, toIndex = VALIDATE_SAMPLES.size)

fun StoryboardBuilder.CheckerExtension(sink: MutableList<CodeSample>, start: Int = 0, endExclusive: Int = SAMPLES.size) {
    sink.addAll(SAMPLES)
    StageSampleScene(SAMPLES, CompilerStage.Analyze, start, endExclusive)
}
