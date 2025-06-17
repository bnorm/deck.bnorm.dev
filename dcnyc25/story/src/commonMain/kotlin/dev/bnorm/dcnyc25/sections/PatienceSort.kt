package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.template.Vertical
import dev.bnorm.deck.story.generated.resources.*
import dev.bnorm.storyboard.Scene
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.toState
import kotlinx.collections.immutable.*
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

private data class SortState(
    val zIndex: PersistentMap<Card, Float>,
    val queue: PersistentList<Card>,
    val stacks: PersistentList<PersistentList<StackItem>>,
    val solution: PersistentList<Card> = persistentListOf(),
)

private fun computeStates(elements: List<Card>): List<SortState> = sequence {
    val queue = ArrayDeque(elements)
    val count = elements.size

    fun SortState.addToStacks(card: Card): PersistentList<PersistentList<StackItem>> {
        val index = stacks.indexOfFirst { it.last().card > card }
        if (index == -1) {
            val backRef = stacks.lastOrNull()?.last()
            return stacks.add(persistentListOf(StackItem(card, backRef)))
        } else {
            val backRef = stacks.getOrNull(index - 1)?.last()
            return stacks.set(index, stacks[index].add(StackItem(card, backRef)))
        }
    }

    val zIndex = queue.mapIndexed { index, item ->
        item to (count - index).toFloat()
    }.toMap().toMutableMap()

    var current = SortState(
        zIndex = zIndex.toPersistentMap(),
        queue = queue.toPersistentList(),
        stacks = persistentListOf()
    )
    yield(current)

    while (queue.isNotEmpty()) {
        val next = queue.removeFirst()

        zIndex[next] = 2 * count - zIndex.getValue(next)
        current = SortState(
            zIndex = zIndex.toPersistentMap(),
            queue = queue.toPersistentList(),
            stacks = current.addToStacks(next)
        )
        yield(current)
    }

    val solution = generateSequence(current.stacks.lastOrNull()?.lastOrNull()) { it.backRef }
        .map { it.card }
        .toList().asReversed().toPersistentList()
    yield(current.copy(solution = solution))
}.toList()

@Composable
private fun PatienceSort(transition: Transition<SortState>, modifier: Modifier = Modifier) {
    val cardHeight = 80.dp

    fun <T> boundsSpec(): TweenSpec<T> = tween(300, delayMillis = 300, easing = EaseInOut)
    fun <T> pathEnterSpec(): TweenSpec<T> = tween(300, delayMillis = 300 + 300, easing = EaseIn)
    fun <T> pathExitSpec(): TweenSpec<T> = tween(300, easing = EaseOut)

    SharedTransitionLayout(modifier) {
        transition.AnimatedContent(transitionSpec = { EnterTransition.None togetherWith ExitTransition.None }) { state ->
            Column(verticalArrangement = Arrangement.spacedBy(48.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy((-32).dp), modifier = Modifier.height(cardHeight)) {
                    for (card in state.queue.asReversed()) {
                        Image(
                            imageResource(card.image),
                            contentDescription = null,
                            modifier = Modifier
                                .sharedElement(
                                    rememberSharedContentState(card),
                                    zIndexInOverlay = state.zIndex.getValue(card),
                                    boundsTransform = { _, _ -> boundsSpec() },
                                )
                                .height(cardHeight)
                        )
                    }
                }

                Box {
                    val offsets = remember { mutableStateMapOf<Int, Offset>() }
                    val placements = remember { mutableStateMapOf<Card, Rect>() }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        for ((index, stack) in state.stacks.withIndex()) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.onPlaced {
                                    try {
                                        offsets[index] = it.positionInParent()
                                    } catch (_: IllegalStateException) {
                                        // TODO there seems to be a possible internal error
                                        //  which can be caused by navigating the overview quickly.
                                        //  - just ignore it?
                                    }
                                }
                            ) {
                                for (item in stack) {
                                    key(item) {
                                        val color by transition.animateColor(transitionSpec = { tween(750) }) {
                                            val color = MaterialTheme.colors.primary
                                            when {
                                                item.card in it.solution -> color
                                                else -> color.copy(alpha = 0f)
                                            }
                                        }
                                        val alpha by transition.animateFloat(transitionSpec = { tween(750) }) {
                                            when {
                                                item.card !in it.solution && it.solution.isNotEmpty() -> 0.75f
                                                else -> 1f
                                            }
                                        }

                                        Image(
                                            imageResource(item.card.image),
                                            alpha = alpha,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .sharedElement(
                                                    rememberSharedContentState(item.card),
                                                    zIndexInOverlay = state.zIndex.getValue(item.card),
                                                    boundsTransform = { _, _ -> boundsSpec() },
                                                )
                                                .border(4.dp, color, RoundedCornerShape(4.dp))
                                                .onPlaced {
                                                    try {
                                                        placements[item.card] = it.boundsInParent()
                                                            .translate(offsets.getValue(index))
                                                    } catch (_: IllegalStateException) {
                                                        // TODO there seems to be a possible internal error
                                                        //  which can be caused by navigating the overview quickly.
                                                        //  - just ignore it?
                                                    }
                                                }
                                                .height(cardHeight)
                                        )
                                    }
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
                                        it.solution.isNotEmpty() -> Color.LightGray
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

                                Canvas(
                                    Modifier.matchParentSize()
                                        .animateEnterExit(
                                            enter = fadeIn(pathEnterSpec()),
                                            exit = fadeOut(pathExitSpec()),
                                        )
                                        .sharedElement(rememberSharedContentState("${item.card}-${parentItem.card}"))
                                ) {
                                    val parentRect = placements.getValue(parentItem.card)
                                    val childRect = placements.getValue(item.card)

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
                        }
                    }
                }
            }
        }
    }
}
