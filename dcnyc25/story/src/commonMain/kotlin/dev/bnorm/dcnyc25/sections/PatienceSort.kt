package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.bnorm.dcnyc25.template.Vertical
import dev.bnorm.deck.story.generated.resources.*
import dev.bnorm.storyboard.Scene
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.animateEnterExit
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.toState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource

fun StoryboardBuilder.PatienceSort(cards: List<Card>): Scene<*> {
    val states = computeStates(cards)
    return scene(
        states = states,
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.BottomCenter),
            end = SceneEnter(alignment = Alignment.TopCenter),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.BottomCenter),
            end = SceneExit(alignment = Alignment.TopCenter),
        ),
    ) {
        Row {
            Vertical(MaterialTheme.colors.primary) {
                PatienceInfo(
                    updateTransition(3),
                    modifier = Modifier.sharedElement(rememberSharedContentState("patience-info")),
                )
            }

            Vertical(MaterialTheme.colors.secondary) {
                PatienceSort(
                    transition.createChildTransition { it.toState() },
                    modifier = Modifier.padding(32.dp)
                )
            }
        }
    }
}

enum class Card(
    val image: DrawableResource,
) {
    Ace(Res.drawable.card_spades_A),
    Two(Res.drawable.card_spades_02),
    Three(Res.drawable.card_spades_03),
    Four(Res.drawable.card_spades_04),
    Five(Res.drawable.card_spades_05),
    Six(Res.drawable.card_spades_06),
    Seven(Res.drawable.card_spades_07),
    Eight(Res.drawable.card_spades_08),
    Nine(Res.drawable.card_spades_09),
    Ten(Res.drawable.card_spades_10),
    Jack(Res.drawable.card_spades_J),
    Queen(Res.drawable.card_spades_Q),
    King(Res.drawable.card_spades_K),
}

private class StackItem(
    val card: Card,
    val backRef: StackItem?,
)

private sealed class SortState {
    abstract val solution: PersistentList<Card>

    data class Setup(
        val before: PersistentList<Card>,
        val after: PersistentList<Card>,
        val link: Boolean = false,
        override val solution: PersistentList<Card> = persistentListOf(),
        val beforeZIndex: Map<Card, Float>,
        val afterZIndex: Map<Card, Float>,
    ) : SortState()

    data class Algo(
        val queue: PersistentList<Card>,
        val stacks: PersistentList<PersistentList<StackItem>>,
        override val solution: PersistentList<Card> = persistentListOf(),
        val zIndex: Map<Card, Float>,
    ) : SortState()
}

private fun computeStates(elements: List<Card>): List<SortState> = sequence {
    fun SortState.Algo.addToStacks(card: Card): PersistentList<PersistentList<StackItem>> {
        val index = stacks.indexOfFirst { it.last().card > card }
        if (index == -1) {
            val backRef = stacks.lastOrNull()?.last()
            return stacks.add(persistentListOf(StackItem(card, backRef)))
        } else {
            val backRef = stacks.getOrNull(index - 1)?.last()
            return stacks.set(index, stacks[index].add(StackItem(card, backRef)))
        }
    }

    val elements = elements.toPersistentList()
    val beforeZIndex = Card.entries.withIndex().associate { (i, c) -> c to Card.entries.size + i.toFloat() }
    val afterZIndex = elements.withIndex().associate { (i, c) -> c to i.toFloat() }

    val start = SortState.Setup(
        before = Card.entries.toPersistentList(),
        after = persistentListOf(),
        beforeZIndex = beforeZIndex,
        afterZIndex = afterZIndex,
    )
    yield(start)
    yield(start.copy(after = elements))
    yield(start.copy(after = elements, link = true))

    val first = SortState.Algo(
        queue = elements,
        stacks = persistentListOf(),
        zIndex = afterZIndex,
    )
    var current = first
    yield(current)

    val queue = ArrayDeque(elements)
    while (queue.isNotEmpty()) {
        val next = queue.removeFirst()

        current = SortState.Algo(
            queue = queue.toPersistentList(),
            stacks = current.addToStacks(next),
            zIndex = afterZIndex,
        )
        yield(current)
    }

    val solution = generateSequence(current.stacks.lastOrNull()?.lastOrNull()) { it.backRef }
        .map { it.card }
        .toList().asReversed().toPersistentList()
    yield(current.copy(solution = solution))
    yield(first.copy(solution = solution))

    yield(start.copy(after = elements, link = true, solution = solution))
}.toList()

@Composable
private fun PatienceSort(state: Transition<SortState>, modifier: Modifier = Modifier) {
    SharedTransitionLayout(modifier.fillMaxSize()) {
        state.AnimatedContent(
            transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
            modifier = Modifier.fillMaxSize(),
        ) {
            when (it) {
                is SortState.Setup -> PatienceSetup(it, state)
                is SortState.Algo -> PatienceAlgo(it, state)
            }
        }
    }
}

private val CardHeight = 80.dp
private fun <T> boundsSpec(): TweenSpec<T> = tween(300, delayMillis = 300, easing = EaseInOut)
private fun <T> pathEnterSpec(): TweenSpec<T> = tween(300, delayMillis = 300 + 300, easing = EaseIn)
private fun <T> pathExitSpec(): TweenSpec<T> = tween(300, easing = EaseOut)

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun CardImage(card: Card, transition: Transition<SortState>, modifier: Modifier = Modifier) {
    val color by transition.animateColor(transitionSpec = { tween(750) }) {
        val color = MaterialTheme.colors.primary
        when {
            card in it.solution -> color
            else -> color.copy(alpha = 0f)
        }
    }
    val alpha by transition.animateColor(transitionSpec = { tween(750) }) {
        when {
            card !in it.solution && it.solution.isNotEmpty() -> Color.Gray.copy(alpha = 0.5f)
            else -> Color.White
        }
    }

    Image(
        imageResource(card.image),
        contentDescription = null,
        colorFilter = ColorFilter.tint(alpha, BlendMode.Darken),
        modifier = modifier
            .border(4.dp, color)
            .height(CardHeight)
    )
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun PatienceSetup(state: SortState.Setup, transition: Transition<SortState>) {
    var rootLayoutCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val beforeCardBoundingBoxes = remember { mutableStateMapOf<Card, Rect>() }
    val afterCardBoundingBoxes = remember { mutableStateMapOf<Card, Rect>() }

    Box(Modifier.onPlaced { rootLayoutCoordinates = it }) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize(),
        ) {
            Box {
                if (state.after.isEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy((-52).dp)) {
                        for (card in state.before) {
                            val zIndex = state.afterZIndex.getValue(card)
                            CardImage(
                                card,
                                transition,
                                modifier = Modifier
                                    .onPlaced {
                                        afterCardBoundingBoxes[card] = rootLayoutCoordinates!!.localBoundingBoxOf(it)
                                    }
                                    .sharedElement(
                                        rememberSharedContentState(card),
                                        boundsTransform = { _, _ -> boundsSpec() },
                                        zIndexInOverlay = zIndex,
                                    )
                                    .zIndex(zIndex)
                            )
                        }
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy((-52).dp)) {
                    for (card in state.before) {
                        val zIndex = state.beforeZIndex.getValue(card)
                        CardImage(
                            card,
                            transition,
                            modifier = Modifier
                                .onPlaced {
                                    beforeCardBoundingBoxes[card] = rootLayoutCoordinates!!.localBoundingBoxOf(it)
                                }
                                .animateEnterExit(
                                    enter = fadeIn(pathEnterSpec()),
                                    exit = fadeOut(pathExitSpec()),
                                )
                                .sharedElement(
                                    rememberSharedContentState("before:$card"),
                                    boundsTransform = { _, _ -> boundsSpec() },
                                    zIndexInOverlay = zIndex,
                                )
                                .zIndex(zIndex)
                        )
                    }
                }
            }
            if (state.after.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy((-52).dp)) {
                    for (card in state.after) {
                        val zIndex = state.afterZIndex.getValue(card)
                        CardImage(
                            card,
                            transition,
                            modifier = Modifier
                                .onPlaced {
                                    afterCardBoundingBoxes[card] = rootLayoutCoordinates!!.localBoundingBoxOf(it)
                                }
                                .sharedElement(
                                    rememberSharedContentState(card),
                                    boundsTransform = { _, _ -> boundsSpec() },
                                    zIndexInOverlay = zIndex,
                                )
                                .zIndex(zIndex)
                        )
                    }
                }
            }
        }

        if (state.link) {
            for (card in state.before) {
                if (state.solution.isNotEmpty() && card !in state.solution) continue

                key(card) {
                    val color by transition.animateColor(transitionSpec = { tween(750) }) {
                        when {
                            card in it.solution -> MaterialTheme.colors.primary
                            it.solution.isNotEmpty() -> Color.Gray
                            else -> Color.White
                        }
                    }

                    val thickness by transition.animateDp(transitionSpec = { tween(750) }) {
                        when {
                            card in it.solution -> 4.dp
                            it.solution.isNotEmpty() -> 1.dp
                            else -> 2.dp
                        }
                    }

                    val stroke = with(LocalDensity.current) { Stroke(width = thickness.toPx()) }

                    Connection(
                        parent = { beforeCardBoundingBoxes.getValue(card) },
                        child = { afterCardBoundingBoxes.getValue(card) },
                        color = color,
                        stroke = stroke,
                        modifier = Modifier.matchParentSize()
                            .animateEnterExit(
                                enter = fadeIn(pathEnterSpec()),
                                exit = fadeOut(pathExitSpec()),
                            )
                            .sharedElement(rememberSharedContentState("${card}-${card}"))
                    )
                }
            }
        }
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun PatienceAlgo(state: SortState.Algo, transition: Transition<SortState>) {
    Column(verticalArrangement = Arrangement.spacedBy(48.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy((-32).dp), modifier = Modifier.height(CardHeight)) {
            for (card in state.queue) {
                val zIndex = state.zIndex.getValue(card)
                CardImage(
                    card = card,
                    transition = transition,
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(card),
                            boundsTransform = { _, _ -> boundsSpec() },
                            zIndexInOverlay = zIndex
                        )
                        .zIndex(zIndex)
                )
            }
        }

        var rootLayoutCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
        val cardBoundingBoxes = remember { mutableStateMapOf<Card, Rect>() }

        Box(Modifier.onPlaced { rootLayoutCoordinates = it }) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                for (stack in state.stacks) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        for (item in stack) {
                            val zIndex = state.zIndex.getValue(item.card)
                            CardImage(
                                card = item.card,
                                transition = transition,
                                modifier = Modifier
                                    .onPlaced {
                                        cardBoundingBoxes[item.card] = rootLayoutCoordinates!!.localBoundingBoxOf(it)
                                    }
                                    .sharedElement(
                                        rememberSharedContentState(item.card),
                                        boundsTransform = { _, _ -> boundsSpec() },
                                        zIndexInOverlay = zIndex
                                    )
                                    .zIndex(zIndex)
                            )
                        }
                    }
                }
            }

            for (stack in state.stacks) {
                for (item in stack) {
                    val parentItem = item.backRef ?: continue
                    key(item) {
                        val color by transition.animateColor(transitionSpec = { tween(750) }) {
                            when {
                                item.card in it.solution -> MaterialTheme.colors.primary
                                it.solution.isNotEmpty() -> Color.Gray
                                else -> Color.White
                            }
                        }

                        val thickness by transition.animateDp(transitionSpec = { tween(750) }) {
                            when {
                                item.card in it.solution -> 4.dp
                                it.solution.isNotEmpty() -> 1.dp
                                else -> 2.dp
                            }
                        }
                        val stroke = with(LocalDensity.current) { Stroke(width = thickness.toPx()) }

                        Connection(
                            parent = { cardBoundingBoxes.getValue(parentItem.card) },
                            child = { cardBoundingBoxes.getValue(item.card) },
                            color = color,
                            stroke = stroke,
                            modifier = Modifier.matchParentSize()
                                .animateEnterExit(
                                    enter = fadeIn(pathEnterSpec()),
                                    exit = fadeOut(pathExitSpec()),
                                )
                                .sharedElement(rememberSharedContentState("${item.card}-${parentItem.card}"))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Connection(
    parent: () -> Rect,
    child: () -> Rect,
    color: Color,
    stroke: Stroke,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier) {
        val parentRect = parent()
        val childRect = child()

        val startX = when (layoutDirection) {
            LayoutDirection.Ltr -> parentRect.right
            LayoutDirection.Rtl -> parentRect.left
        }
        val startY = parentRect.top + (parentRect.bottom - parentRect.top) / 2f

        val endX = when (layoutDirection) {
            LayoutDirection.Ltr -> childRect.left
            LayoutDirection.Rtl -> childRect.right
        }
        val endY = childRect.top + (childRect.bottom - childRect.top) / 2f

        val middleX = startX + (endX - startX) / 2f

        val path = Path()
        path.moveTo(startX, startY)
        path.cubicTo(
            x1 = middleX, y1 = startY,
            x2 = middleX, y2 = endY,
            x3 = endX, y3 = endY,
        )
        drawPath(path, color, style = stroke)
    }
}
