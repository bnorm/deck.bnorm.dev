package dev.bnorm.kc25.sections.plugin.fir

import dev.bnorm.kc25.components.temp.SamplesScene
import dev.bnorm.kc25.sections.plugin.REGISTRATION_IR_CHECKPOINT
import dev.bnorm.kc25.sections.plugin.Registration
import dev.bnorm.kc25.template.SectionAndTitle
import dev.bnorm.storyboard.StoryboardBuilder

val FirDeclarationGenerationExtensionSamples = listOf(
    // TODO better example of entire extension
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@BuildableFirDeclarationGenerationExtension",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@ANNOTATION_PREDICATE",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/BuildableNames.kt@BuildableNames",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@registerPredicates",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@classIds",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@builderClassIds",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@getNestedClassifiersNames",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@generateNestedClassLikeDeclaration",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/BuildableKey.kt@BuildableKey",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@getCallableNamesForClass",
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

        // TODO remove support for type parameters
        //  - avoids talking about type substitution
        //  - create a checker as a gentle introduction to those
        SamplesScene(FirDeclarationGenerationExtensionSamples)
        SamplesScene(FirAdditionalCheckersExtension)
    }
}
