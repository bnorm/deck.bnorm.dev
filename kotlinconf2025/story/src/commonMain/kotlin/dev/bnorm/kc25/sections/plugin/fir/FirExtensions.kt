package dev.bnorm.kc25.sections.plugin.fir

import dev.bnorm.kc25.components.temp.SamplesScene
import dev.bnorm.kc25.sections.plugin.REGISTRATION_IR_CHECKPOINT
import dev.bnorm.kc25.sections.plugin.Registration
import dev.bnorm.kc25.template.SectionAndTitle
import dev.bnorm.storyboard.StoryboardBuilder

val FirDeclarationGenerationExtensionSamples = listOf(
    // TODO better example of entire extension
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@BuildableFirDeclarationGenerationExtension",

     "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@NAMES",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@BUILDABLE_PREDICATE",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@HAS_BUILDABLE_PREDICATE",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@registerPredicates",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@getNestedClassifiersNames",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@generateNestedClassLikeDeclaration",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/keys.kt@BuilderClassKey", // TODO side panel?

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@getCallableNamesForClass",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/keys.kt@BuildableKey", // TODO side panel?
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@generateConstructors",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@generateProperties",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@generateFunctions",
)

val FirAdditionalCheckersExtension = listOf(
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableConstructorChecker.kt@BuildableConstructorChecker",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableErrors.kt@BuildableErrors",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirAdditionalCheckersExtension.kt@BuildableFirAdditionalCheckersExtension",
)

val FirExtensionRegistrar = listOf(
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirExtensionRegistrar.kt@BuildableFirExtensionRegistrar",
)

fun StoryboardBuilder.FirExtensions() {
    SectionAndTitle("FIR Extensions") {
        Registration(endExclusive = REGISTRATION_IR_CHECKPOINT)
        SamplesScene(FirExtensionRegistrar)

        // TODO can have a nice aside showing some code for all the annotation predicate types
        //  and what each of the different types would match

        // TODO create a checker for no type parameters as another example?
        //  - or maybe leave this to the companion?

        Generation()
        SamplesScene(FirAdditionalCheckersExtension)
    }
}
