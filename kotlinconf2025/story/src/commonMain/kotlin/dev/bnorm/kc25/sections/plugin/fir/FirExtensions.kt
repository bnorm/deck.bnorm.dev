package dev.bnorm.kc25.sections.plugin.fir

import dev.bnorm.kc25.components.temp.SamplesScene
import dev.bnorm.kc25.sections.plugin.REGISTRATION_IR_CHECKPOINT
import dev.bnorm.kc25.sections.plugin.Registration
import dev.bnorm.kc25.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

val FirDeclarationGenerationExtensionSamples = listOf(
    // TODO better example of entire extension
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@BuildableFirDeclarationGenerationExtension",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@ANNOTATION_PREDICATE",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/BuildableNames.kt@BuildableNames",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@registerPredicates",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@classIds",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@builderClassIds",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@getNestedClassifiersNames",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@generateNestedClassLikeDeclaration",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/factory.kt@createBuilderClass",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/BuildableKey.kt@BuildableKey",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/factory.kt@copyTypeParameters",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/factory.kt@substituteOrSelf",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@getCallableNamesForClass",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@generateConstructors",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@generateProperties",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/factory.kt@createBuilderProperty",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/BuildableFirDeclarationGenerationExtension.kt@generateFunctions",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/declarations/factory.kt@createBuilderBuild",
)

val FirStatusTransformerExtension = listOf(
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirStatusTransformerExtension.kt@BuildableFirStatusTransformerExtension",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirStatusTransformerExtension.kt@needTransformStatus",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirStatusTransformerExtension.kt@transformStatus",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirStatusTransformerExtension.kt@originalVisibility",
)

val FirAdditionalCheckersExtension = listOf(
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/checkers/BuildableConstructorVisibilityChecker.kt@BuildableConstructorVisibilityChecker",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/checkers/BuildableErrors.kt@BuildableErrors",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/checkers/BuildableFirAdditionalCheckersExtension.kt@BuildableFirAdditionalCheckersExtension",
)

val FirExtensionRegistrar = listOf(
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirExtensionRegistrar.kt@BuildableFirExtensionRegistrar",
)

fun StoryboardBuilder.FirExtensions() {
    SectionAndTitle("FIR Extensions") {
        Registration(endExclusive = REGISTRATION_IR_CHECKPOINT)
        SamplesScene(FirExtensionRegistrar)

        SamplesScene(FirDeclarationGenerationExtensionSamples)
        SamplesScene(FirStatusTransformerExtension)
        SamplesScene(FirAdditionalCheckersExtension)
    }
}
