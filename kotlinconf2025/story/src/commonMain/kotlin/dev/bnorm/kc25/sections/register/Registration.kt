package dev.bnorm.kc25.sections.register

import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Registration(sink: MutableList<CodeSample>) {
    // TODO similar to stages, zoom in and out of components to see changes which need to be made
    //  - remove header to focus on layout
    //  - have a few bullet points to describe how the extension can be used

    // TODO what to put in the backend versus the frontend
    //  - put boxes around the frontend and the backend

    // TODO example showing template project and how
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
            visible = revealOrder.subList(fromIndex = 0, toIndex = 7).toSet(),
            focus = Component.FirAdditionalCheckersExtension,
            stages = setOf(CompilerStage.Analyze),
        ),
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 7).toSet(),
            focus = Component.FirExtensionRegistrar,
            stages = setOf(CompilerStage.Resolve, CompilerStage.Analyze),
        ),
    )

    FirRegistrar(sink)

    // TODO move IrGenerationExtension boiler plate here?

    RegistrarComponent(
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 7).toSet(),
            focus = Component.FirExtensionRegistrar,
            stages = setOf(CompilerStage.Resolve, CompilerStage.Analyze),
        ),
        RegistrarComponentState(
            visible = revealOrder.subList(fromIndex = 0, toIndex = 7).toSet(),
            focus = Component.FirDeclarationGenerationExtension,
            stages = setOf(CompilerStage.Resolve),
        ),
    )
}
