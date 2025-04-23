package dev.bnorm.kc25.sections.register

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.StageScaffold
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.Registration() {
    // TODO talk about the layers?
    //  - like the first layer are Extensions
    //  - the second layer are FirExtensions
    //  - or can we add boxes around things?

    // TODO don't make this section Buildable specific

    // TODO talk about more FirExtensions

    // TODO similar to stages, zoom in and out of components to see changes which need to be made
    //  - remove header to focus on layout
    //  - have a few bullet points to describe how the extension can be used

    // TODO what to put in the backend versus the frontend
    //  - put boxes around the frontend and the backend

    // TODO example showing template project and how

    RegistrarComponents(null, Component.CompilerPluginRegistrar)
    PluginRegistrar(endExclusive = REGISTRATION_START + 1)
    RegistrarComponents(Component.CompilerPluginRegistrar, Component.FirExtensionRegistrar)
    PluginRegistrar(start = REGISTRATION_START, endExclusive = REGISTRATION_FRONTEND_END)
    FirRegistrar(endExclusive = FIR_REGISTRATION_CONFIGURE_END)
    RegistrarComponents(Component.FirExtensionRegistrar, Component.FirDeclarationGenerationExtension)
    FirRegistrar(start = FIR_REGISTRATION_CONFIGURE_END, endExclusive = FIR_REGISTRATION_RESOLVE_END)
    RegistrarComponents(Component.FirDeclarationGenerationExtension, Component.FirAdditionalCheckersExtension)
    FirRegistrar(start = FIR_REGISTRATION_RESOLVE_END)
    RegistrarComponents(Component.FirAdditionalCheckersExtension, Component.IrGenerationExtension)
    PluginRegistrar(start = REGISTRATION_FRONTEND_END)
}

fun StoryboardBuilder.RegistrarComponentsFocus(first: Component?, second: Component?) {
    scene(
        states = setOf(first, second).toList(),
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        StageScaffold { padding ->
            RegistrarComponents(
                visible = updateTransition(Component.entries.last()),
                focus = frame.createChildTransition { it.toState() },
                modifier = Modifier.padding(top = padding.calculateTopPadding() + 32.dp),
            )
        }
    }
}
