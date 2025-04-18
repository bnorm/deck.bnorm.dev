package dev.bnorm.kc25.sections.write.analyze

import dev.bnorm.kc25.components.temp.SamplesScene
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.storyboard.StoryboardBuilder

val FirAdditionalCheckersExtension = listOf(
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirAdditionalCheckersExtension.kt@BuildableFirAdditionalCheckersExtension",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableConstructorChecker.kt@BuildableConstructorChecker",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableErrors.kt@BuildableErrors",
)

fun StoryboardBuilder.Checkers() {
    // TODO create a checker for no type parameters as another example?
    //  - or maybe leave this to the companion?

    SamplesScene(CompilerStage.Analyze, FirAdditionalCheckersExtension)
}
