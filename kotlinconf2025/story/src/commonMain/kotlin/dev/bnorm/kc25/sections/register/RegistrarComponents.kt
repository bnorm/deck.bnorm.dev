package dev.bnorm.kc25.sections.register

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.layout.Connected
import dev.bnorm.deck.shared.layout.connectionPath
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.StageScaffold
import dev.bnorm.kc25.template.code1
import dev.bnorm.kc25.template.code3
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.animateEnterExit
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.toState

enum class Component {
    CompilerPluginRegistrar,

    FirExtensionRegistrar,
    IrGenerationExtension,

    FirDeclarationGenerationExtension,
    FirSupertypeGenerationExtension,
    FirStatusTransformerExtension,
    FirAdditionalCheckersExtension,
}

class RegistrarComponentState(
    val visible: Set<Component> = Component.entries.toSet(),
    val focus: Component? = null,
    val stages: Set<CompilerStage> = emptySet(),
)

private fun fadeInSpec(): EnterTransition = fadeIn(tween(300, easing = EaseIn))
val BoxMovementSpec: BoundsTransform = BoundsTransform { _, _ -> tween(500, delayMillis = 300, easing = EaseInOut) }
private fun fadeOutSpec(): ExitTransition = fadeOut(tween(300, easing = EaseOut))

val DetailsEnterTransition: (AdvanceDirection) -> EnterTransition = { _ -> fadeIn(tween(300, delayMillis = 800, easing = EaseIn)) }
val DetailsExitTransition: (AdvanceDirection) -> ExitTransition = { _ -> fadeOutSpec() }

fun StoryboardBuilder.RegistrarComponent(
    vararg states: RegistrarComponentState,
    enterTransition: (AdvanceDirection) -> EnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition,
) {
    scene(
        states = states.toList(),
        enterTransition = enterTransition,
        exitTransition = exitTransition,
    ) {
        val state = transition.createChildTransition { it.toState() }
        StageScaffold(state.createChildTransition { it.stages }) { padding ->
            RegistrarComponentTree(
                state = state,
                modifier = Modifier.padding(top = padding.calculateTopPadding() + 32.dp),
            )
        }
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun RegistrarComponentTree(
    state: Transition<RegistrarComponentState>,
    modifier: Modifier = Modifier,
) {
    val pad = 16.dp
    val space = 128.dp

    val height = 48.dp
    val top = 0.dp

    val width = 288.dp
    val start = (Storyboard.DEFAULT_SIZE.width - width) / 2

    @Composable
    fun Content(component: Component, style: TextStyle) {
        val borderColor by state.animateColor(transitionSpec = { tween(300, easing = EaseIn) }) {
            when (it.focus) {
                component -> MaterialTheme.colors.secondary
                else -> MaterialTheme.colors.primary
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
                .sharedElement(
                    rememberSharedContentState("box:$component"),
                    boundsTransform = BoxMovementSpec,
                    zIndexInOverlay = -1f,
                )
                .border(2.dp, borderColor, RoundedCornerShape(8.dp))
        ) {
            Text(
                text = component.name,
                style = style,
                modifier = Modifier
                    .animateEnterExit(
                        enter = fadeInSpec(),
                        exit = fadeOutSpec(),
                    )
            )
        }
    }

    @Composable
    fun Item(component: Component, style: TextStyle) {
        Box(Modifier.size(width = width, height = height)) {
            state.AnimatedVisibility(
                visible = { component in it.visible },
                enter = fadeInSpec(),
                exit = fadeOutSpec(),
            ) {
                Content(component, style)
            }
        }
    }

    val color = MaterialTheme.colors.primary
    val stroke = with(LocalDensity.current) { Stroke(width = 2.dp.toPx(), cap = StrokeCap.Square) }

    Connected(
        modifier = modifier.fillMaxSize(),
        connection = { start, startRect, end, endRect ->
            Box(Modifier.fillMaxSize()) {
                state.AnimatedVisibility(
                    visible = { start in it.visible && end in it.visible },
                    enter = fadeInSpec(),
                    exit = fadeOutSpec(),
                ) {
                    Canvas(Modifier.fillMaxSize()) {
                        val path = connectionPath(startRect, 0.625f, endRect, 0.125f)
                        drawPath(path, color, style = stroke)
                    }
                }
            }
        }
    ) {
        item(
            value = Component.CompilerPluginRegistrar,
            x = start, y = top,
            connections = listOf(
                Component.FirExtensionRegistrar,
                Component.IrGenerationExtension,
            )
        ) {
            Item(Component.CompilerPluginRegistrar, MaterialTheme.typography.code1)
        }

        val topFir = top + space
        item(
            value = Component.FirExtensionRegistrar,
            x = start - width / 2 - pad, y = topFir,
            connections = listOf(
                Component.FirDeclarationGenerationExtension,
                Component.FirSupertypeGenerationExtension,
                Component.FirStatusTransformerExtension,
                Component.FirAdditionalCheckersExtension,
            )
        ) {
            Item(Component.FirExtensionRegistrar, MaterialTheme.typography.code1)
        }

        item(
            Component.IrGenerationExtension,
            x = start + width / 2 + pad, y = top + space,
        ) {
            Item(Component.IrGenerationExtension, MaterialTheme.typography.code1)
        }

        item(
            Component.FirDeclarationGenerationExtension,
            x = start - width - pad * 2, y = topFir + space,
        ) {
            Item(Component.FirDeclarationGenerationExtension, MaterialTheme.typography.code3)
        }

        item(
            Component.FirStatusTransformerExtension,
            x = start - width / 2 - pad, y = topFir + space + height + pad,
        ) {
            Item(Component.FirStatusTransformerExtension, MaterialTheme.typography.code3)
        }

        item(
            Component.FirSupertypeGenerationExtension,
            x = start + width / 2 + pad, y = topFir + space + height + pad,
        ) {
            Item(Component.FirSupertypeGenerationExtension, MaterialTheme.typography.code3)
        }

        item(
            Component.FirAdditionalCheckersExtension,
            x = start + width + pad * 2, y = topFir + space,
        ) {
            Item(Component.FirAdditionalCheckersExtension, MaterialTheme.typography.code3)
        }
    }
}
