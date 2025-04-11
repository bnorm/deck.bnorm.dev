package dev.bnorm.kc25.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
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
import dev.bnorm.deck.shared.DefaultCornerKodee
import dev.bnorm.deck.shared.SharedKodee
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.SceneSection
import dev.chrisbanes.haze.*

fun <T> StoryboardBuilder.KodeeScene(
    states: List<T>,
    enterTransition: (AdvanceDirection) -> EnterTransition = SceneEnter(alignment = Alignment.CenterEnd),
    exitTransition: (AdvanceDirection) -> ExitTransition = SceneExit(alignment = Alignment.CenterEnd),
    content: SceneContent<T>,
): Scene<T> = scene(states, enterTransition, exitTransition) {
    Column(Modifier.fillMaxSize()) {
        Render(content)
    }

    SharedKodee {
        DefaultCornerKodee(Modifier.size(50.dp))
    }
}

fun StoryboardBuilder.KodeeScene(
    stateCount: Int = 1,
    enterTransition: (AdvanceDirection) -> EnterTransition = SceneEnter(alignment = Alignment.CenterEnd),
    exitTransition: (AdvanceDirection) -> ExitTransition = SceneExit(alignment = Alignment.CenterEnd),
    content: SceneContent<Int>,
): Scene<Int> = scene(stateCount, enterTransition, exitTransition) {
    Column(Modifier.fillMaxSize()) {
        Render(content)
    }

    SharedKodee {
        DefaultCornerKodee(Modifier.size(50.dp))
    }
}

private enum class KodeeSceneContent {
    Header,
    Body,
    Kodee,
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun KodeeScaffold(
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
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

    val section = when (header) {
        null -> SceneSection.current
        else -> remember(header) { SceneSection(header) }
    }

    SubcomposeLayout(modifier) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val headerPlaceables = subcompose(KodeeSceneContent.Header) {
            Header(
                Modifier.sharedElement(rememberSharedContentState(key = section))
                    .hazeEffect(state = hazeState, style = style) {
                        progressive = HazeProgressive.verticalGradient(
                            startIntensity = 1f,
                            endIntensity = 0f,
                        )
                    }
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)

            ) {
                ProvideTextStyle(MaterialTheme.typography.h3) {
                    section.title()
                }
            }
        }.fastMap { it.measure(looseConstraints) }

        val headerHeight = headerPlaceables.fastMaxBy { it.height }?.height ?: 0

        val kodeePlaceables = subcompose(KodeeSceneContent.Kodee) {
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

        val bodyPlaceables = subcompose(KodeeSceneContent.Body) {
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
