package dev.bnorm.kc25.sections.plugin.fir

import dev.bnorm.kc25.components.temp.SamplesScene
import dev.bnorm.kc25.sections.CompilerStage
import dev.bnorm.kc25.sections.plugin.REGISTRATION_IR_CHECKPOINT
import dev.bnorm.kc25.sections.plugin.PluginRegistrar
import dev.bnorm.kc25.template.SectionAndTitle
import dev.bnorm.storyboard.StoryboardBuilder

val FirAdditionalCheckersExtension = listOf(
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableConstructorChecker.kt@BuildableConstructorChecker",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableErrors.kt@BuildableErrors",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirAdditionalCheckersExtension.kt@BuildableFirAdditionalCheckersExtension",
)

fun StoryboardBuilder.FirExtensions() {
    SectionAndTitle("FIR Extensions") {
        PluginRegistrar(endExclusive = REGISTRATION_IR_CHECKPOINT)
        FirRegistrar(endExclusive = FIR_REGISTRATION_DGE_CHECKPOINT)

        // TODO can have a nice aside showing some code for all the annotation predicate types
        //  and what each of the different types would match

        // TODO create a checker for no type parameters as another example?
        //  - or maybe leave this to the companion?

        Generation()
        SamplesScene(CompilerStage.Analyse, FirAdditionalCheckersExtension)
    }
}
