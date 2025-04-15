package dev.bnorm.kc25.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import dev.bnorm.deck.shared.SharedKodee
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.chrisbanes.haze.*

// TODO add a 'Panel' between body and Kodee
private enum class StageSceneContent {
    Stages,
    Body,
    Kodee,
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun StageScaffold(
    currentStage: Transition<CompilerStage?>,
    modifier: Modifier = Modifier,
    kodee: @Composable () -> Unit = { DefaultReactionKodee() },
    body: @Composable BoxScope.(PaddingValues) -> Unit,
) {
    val hazeState = remember { HazeState() }
    val style = HazeDefaults.style(
        backgroundColor = Color.Transparent,
        tint = HazeTint(color = Color(0xFF1D002E)),
        blurRadius = 16.dp,
        noiseFactor = 0f,
    )

    val contentPadding = remember {
        object : PaddingValues {
            var paddingHolder by mutableStateOf(PaddingValues(0.dp))

            override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
                paddingHolder.calculateLeftPadding(layoutDirection)

            override fun calculateTopPadding(): Dp = paddingHolder.calculateTopPadding()

            override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
                paddingHolder.calculateRightPadding(layoutDirection)

            override fun calculateBottomPadding(): Dp = paddingHolder.calculateBottomPadding()
        }
    }

    SubcomposeLayout(modifier) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val headerPlaceables = subcompose(StageSceneContent.Stages) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .hazeEffect(state = hazeState, style = style) {
                        progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
                    }
                    .sharedElement(rememberSharedContentState(key = StageSceneContent.Stages))
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .offset(y = (-16).dp)
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    for (stage in CompilerStage.entries) {
                        // TODO how do we animate color across scenes?
                        //  - will it require a transition scene?
                        val borderColor by currentStage.animateColor(
                            transitionSpec = { tween(500, easing = EaseOut) },
                        ) {
                            when (it) {
                                stage -> MaterialTheme.colors.secondary
                                else -> MaterialTheme.colors.primary
                            }
                        }

                        CompilerStateBox(stage, borderColor)
                    }
                }
            }
        }.fastMap { it.measure(looseConstraints) }

        val headerHeight = headerPlaceables.fastMaxBy { it.height }?.height ?: 0

        val kodeePlaceables = subcompose(StageSceneContent.Kodee) {
            SharedKodee {
                kodee()
            }
        }.fastMap { it.measure(looseConstraints) }

        val kodeeWidth = kodeePlaceables.fastMaxBy { it.width }?.width ?: 0
        val kodeeHeight = kodeePlaceables.fastMaxBy { it.height }?.height ?: 0
        val kodeeLeftOffset = layoutWidth - kodeeWidth
        val kodeeTopOffset = layoutHeight - kodeeHeight

        contentPadding.paddingHolder = PaddingValues(
            top = headerHeight.toDp(),
            bottom = 0.dp,
            start = 32.dp,
            end = 32.dp,
        )

        val bodyPlaceables = subcompose(StageSceneContent.Body) {
            Box(Modifier.fillMaxSize().hazeSource(state = hazeState)) {
                body(contentPadding)
            }
        }.fastMap { it.measure(looseConstraints) }

        layout(layoutWidth, layoutHeight) {
            bodyPlaceables.fastForEach { it.place(0, 0) }
            headerPlaceables.fastForEach { it.place(0, 0) }
            kodeePlaceables.fastForEach { it.place(kodeeLeftOffset, kodeeTopOffset) }
        }
    }
}

@Composable
private fun CompilerStateBox(
    state: CompilerStage,
    borderColor: Color,
) {
    Box(Modifier.size(156.dp, 69.dp)) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, borderColor, RoundedCornerShape(16.dp))
        ) {
            Text(
                state.name,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )
        }
    }
}
