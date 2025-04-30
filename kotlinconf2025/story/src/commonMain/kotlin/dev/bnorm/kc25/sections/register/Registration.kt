package dev.bnorm.kc25.sections.register

import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Registration(sink: MutableList<CodeSample>) {
    val revealOrder = listOf(
        Component.CompilerPluginRegistrar,
        Component.FirExtensionRegistrar,
        Component.IrGenerationExtension,
        Component.FirDeclarationGenerationExtension,
        Component.FirStatusTransformerExtension,
        Component.FirSupertypeGenerationExtension,
        Component.FirAdditionalCheckersExtension,
    )

    RegistrarComponent(
        RegistrarComponentState(visible = emptySet()),
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 1).toSet(),
            focus = Component.CompilerPluginRegistrar,
            stages = setOf(CompilerStage.Resolve, CompilerStage.Analyze, CompilerStage.Transform),
        ),
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 2).toSet(),
            focus = Component.FirExtensionRegistrar,
            stages = setOf(CompilerStage.Resolve, CompilerStage.Analyze),
        ),
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 3).toSet(),
            focus = Component.IrGenerationExtension,
            stages = setOf(CompilerStage.Transform),
        ),
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 3).toSet(),
            focus = Component.CompilerPluginRegistrar,
            stages = setOf(CompilerStage.Resolve, CompilerStage.Analyze, CompilerStage.Transform),
        ),
        enterTransition = DetailsEnterTransition,
        exitTransition = DetailsExitTransition,
    )

    PluginRegistrar(sink)

    RegistrarComponent(
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 3).toSet(),
            focus = Component.CompilerPluginRegistrar,
            stages = setOf(CompilerStage.Resolve, CompilerStage.Analyze, CompilerStage.Transform),
        ),
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 4).toSet(),
            focus = Component.FirDeclarationGenerationExtension,
            stages = setOf(CompilerStage.Resolve),
        ),
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 5).toSet(),
            focus = Component.FirStatusTransformerExtension,
            stages = setOf(CompilerStage.Resolve),
        ),
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 6).toSet(),
            focus = Component.FirSupertypeGenerationExtension,
            stages = setOf(CompilerStage.Resolve),
        ),
        RegistrarComponentState(
            focus = Component.FirAdditionalCheckersExtension,
            stages = setOf(CompilerStage.Analyze),
        ),
        RegistrarComponentState(
            focus = Component.FirExtensionRegistrar,
            stages = setOf(CompilerStage.Resolve, CompilerStage.Analyze),
        ),
        enterTransition = DetailsEnterTransition,
        exitTransition = DetailsExitTransition,
    )

    FirRegistrar(sink)
}
