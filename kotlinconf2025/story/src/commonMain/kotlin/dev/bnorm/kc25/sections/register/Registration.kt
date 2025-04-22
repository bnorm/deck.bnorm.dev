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
    // TODO can we come up with an improved architecture diagram which includes setup?
    //  - build some kind of tree to show configuration?

    // TODO talk about the layers?
    //  - like the first layer are Extensions
    //  - the second layer are FirExtensions
    //  - or can we add boxes around things?

    // TODO similar to stages, zoom in and out of components to see changes which need to be made
    //  - remove header to focus on layout

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

fun StoryboardBuilder.RegistrarComponentsFocus(first: Component, second: Component) {
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
