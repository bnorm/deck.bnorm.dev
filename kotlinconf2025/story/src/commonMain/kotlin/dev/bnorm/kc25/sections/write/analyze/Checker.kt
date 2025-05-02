package dev.bnorm.kc25.sections.write.analyze

import androidx.compose.runtime.Composable
import dev.bnorm.kc25.components.validateSample
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.StageSampleScene
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder

private val SAMPLES = buildCodeSamples {
    val sup by tag("super class")
    val cid by tag("")
    val sig by tag("")
    val find by tag("")
    val report by tag("")

    val base = """
        object BuildableConstructorChecker : ${sup}FirClassChecker(MppCheckerKind.Common)${sup} {

          ${cid}private val BUILDABLE_CLASS_ID = ClassId.topLevel(
            FqName("dev.bnorm.buildable.Buildable")
          )${cid}

          ${sig}override fun check(
            declaration: FirClass,
            context: CheckerContext,
            reporter: DiagnosticReporter,
          )${sig} {
            ${find}val annotations = buildList {
              val scope = declaration.symbol.declaredMemberScope(context)
              scope.processDeclaredConstructors { constructor ->
                for (annotation in constructor.resolvedAnnotationsWithClassIds) {
                  if (annotation.resolvedType.classId == BUILDABLE_CLASS_ID) {
                    add(annotation)
                  }
                }
              }
            }${find}

            ${report}if (annotations.size > 1) {
              for (annotation in annotations) {
                reporter.reportOn(
                  source = annotation.source,
                  factory = BuildableErrors.BUILDABLE_MULTIPLE_CONSTRUCTORS,
                  context = context
                )
              }
            }${report}
          }
        }
    """.trimIndent().toCodeSample()

    base
        .then { focus(sup) }
        .then { focus(cid, scroll = false) }
        .then { focus(sig, scroll = false) }
        .then { focus(find) }
        .then { focus(report) }
}

@Composable
internal fun validateCheckerSample() {
    validateSample(
        sample = SAMPLES[0].string,
        file = "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableConstructorChecker.kt@BuildableConstructorChecker"
    )
}

fun StoryboardBuilder.Checker(sink: MutableList<CodeSample>, start: Int = 0, endExclusive: Int = SAMPLES.size) {
    sink.addAll(SAMPLES)
    StageSampleScene(SAMPLES, CompilerStage.Analyze, start, endExclusive)
}
