package dev.bnorm.kc25.sections.register

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.StageScaffold
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

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
        RegistrarComponentState(visible = revealOrder.subList(fromIndex = 0, toIndex = 1).toSet()),
        RegistrarComponentState(visible = revealOrder.subList(fromIndex = 0, toIndex = 2).toSet()),
        RegistrarComponentState(visible = revealOrder.subList(fromIndex = 0, toIndex = 3).toSet()),
    )

    // TODO highlight stages and the extension to show frontend vs backend

    PluginRegistrar(sink)

    RegistrarComponent(
        RegistrarComponentState(visible = revealOrder.subList(fromIndex = 0, toIndex = 3).toSet()),
        RegistrarComponentState(visible = revealOrder.subList(fromIndex = 0, toIndex = 4).toSet()),
        RegistrarComponentState(visible = revealOrder.subList(fromIndex = 0, toIndex = 5).toSet()),
        RegistrarComponentState(visible = revealOrder.subList(fromIndex = 0, toIndex = 6).toSet()),
        RegistrarComponentState(visible = revealOrder.subList(fromIndex = 0, toIndex = 7).toSet()),
    )

    // TODO highlight stages and the extension to show resolve vs analyze

    FirRegistrar(sink)
}

fun StoryboardBuilder.RegistrarComponentsFocus(first: Component?, second: Component?) {
    scene(
        states = listOf(
            RegistrarComponentState(Component.entries.toSet(), focus = first),
            RegistrarComponentState(Component.entries.toSet(), focus = second),
        ),
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        StageScaffold { padding ->
            RegistrarComponentTree(
                state = frame.createChildTransition { it.toState() },
                modifier = Modifier.padding(top = padding.calculateTopPadding() + 32.dp),
            )
        }
    }
}
