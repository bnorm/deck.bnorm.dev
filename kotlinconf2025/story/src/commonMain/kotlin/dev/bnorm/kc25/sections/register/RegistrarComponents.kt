package dev.bnorm.kc25.sections.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import dev.bnorm.kc25.template.StageScaffold
import dev.bnorm.kc25.template.code1
import dev.bnorm.kc25.template.code3
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

enum class Component {
    CompilerPluginRegistrar,

    // TODO other FIR extensions?
    FirExtensionRegistrar,
    FirDeclarationGenerationExtension,
    FirAdditionalCheckersExtension,

    // TODO other compiler extensions?
    IrGenerationExtension,
}

fun StoryboardBuilder.RegistrarComponents(first: Component?, second: Component) {
    scene(
        states = setOf(first, second).toList(),
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        StageScaffold { padding ->
            RegistrarComponents(
                visible = frame.createChildTransition { it.toState() },
                modifier = Modifier.padding(top = padding.calculateTopPadding() + 32.dp),
            )
        }
    }
}

@Composable
fun RegistrarComponents(
    visible: Transition<Component?>,
    focus: Transition<Component?> = visible,
    modifier: Modifier = Modifier,
) {
    val pad = 16.dp
    val space = 128.dp

    val height = 48.dp
    val top = 0.dp

    val width = 288.dp
    val start = (Storyboard.DEFAULT_SIZE.width - width) / 2

    @Composable
    fun Item(component: Component, style: TextStyle) {
        Box(Modifier.size(width = width, height = height)) {
            visible.AnimatedVisibility(
                visible = { component.ordinal <= (it?.ordinal ?: -1) },
                enter = fadeIn(tween(300, easing = EaseIn)),
                exit = fadeOut(tween(300, easing = EaseOut)),
            ) {
                val borderColor by focus.animateColor(transitionSpec = { tween(300, easing = EaseIn) }) {
                    when (it) {
                        component -> MaterialTheme.colors.secondary
                        else -> MaterialTheme.colors.primary
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                        .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                ) {
                    Text(component.name, style = style)
                }
            }
        }
    }

    val color = MaterialTheme.colors.primary
    val stroke = with(LocalDensity.current) { Stroke(width = 2.dp.toPx(), cap = StrokeCap.Square) }

    Connected(
        modifier = modifier.fillMaxSize(),
        connection = { start, startRect, end, endRect ->
            Box(Modifier.fillMaxSize()) {
                visible.AnimatedVisibility(
                    visible = { end.ordinal <= (it?.ordinal ?: -1) },
                    enter = fadeIn(tween(300, easing = EaseIn)),
                    exit = fadeOut(tween(300, easing = EaseOut)),
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

        val startFir = start - width / 2 - pad
        val topFir = top + space
        item(
            value = Component.FirExtensionRegistrar,
            x = startFir, y = topFir,
            connections = listOf(
                Component.FirDeclarationGenerationExtension,
                Component.FirAdditionalCheckersExtension
            )
        ) {
            Item(Component.FirExtensionRegistrar, MaterialTheme.typography.code1)
        }

        item(
            Component.FirDeclarationGenerationExtension,
            x = startFir - width / 2 - pad, y = topFir + space,
        ) {
            Item(Component.FirDeclarationGenerationExtension, MaterialTheme.typography.code3)
        }

        item(
            Component.FirAdditionalCheckersExtension,
            x = startFir, y = topFir + space + height + pad,
        ) {
            Item(Component.FirAdditionalCheckersExtension, MaterialTheme.typography.code3)
        }

        item(
            Component.IrGenerationExtension,
            x = start + width / 2 + pad, y = top + space,
        ) {
            Item(Component.IrGenerationExtension, MaterialTheme.typography.code1)
        }
    }
}
