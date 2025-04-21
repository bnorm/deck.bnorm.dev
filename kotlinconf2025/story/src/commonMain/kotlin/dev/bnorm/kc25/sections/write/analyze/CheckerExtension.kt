package dev.bnorm.kc25.sections.write.analyze

import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.StageSampleScene
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder

private val SAMPLES = buildCodeSamples {
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

    base.hide(dec, cls, chk)
        .then { focus(sup, scroll = false) }
        .then { reveal(dec).focus(dec, scroll = false) }
        .then { reveal(cls).focus(cls, scroll = false) }
        .then { reveal(chk).focus(chk, scroll = false) }
}

fun StoryboardBuilder.CheckerExtension(start: Int = 0, endExclusive: Int = SAMPLES.size) {
    StageSampleScene(SAMPLES, CompilerStage.Analyze, start, endExclusive)
}
