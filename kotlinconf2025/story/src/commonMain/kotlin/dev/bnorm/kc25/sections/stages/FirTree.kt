package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.layout.HorizontalTree
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code2
import dev.bnorm.storyboard.LocalSceneMode
import dev.bnorm.storyboard.SceneMode
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags

private abstract class FirNode(
    val name: String,
) {
    open val children: List<FirNode> get() = emptyList()

    @Composable
    open fun Content(transition: Transition<Int>) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(name, style = MaterialTheme.typography.code2)
        }
    }
}

private class FirElement(
    override val children: List<FirNode>,
) : FirNode("FirElement")

private class FirExpression : FirNode("FirExpression") {
    override val children: List<FirNode> = listOf(
        FirBlock(),
        FirFunctionCall(),
        FirLiteralExpression(),
    )
}

private class FirBlock : FirNode("FirBlock")
private class FirFunctionCall : FirNode("FirFunctionCall")
private class FirLiteralExpression : FirNode("FirLiteralExpression")

private class FirDeclaration(
    override val children: List<FirNode>,
) : FirNode("FirDeclaration")

private class FirFile : FirNode("FirFile")
private class FirClass : FirNode("FirClass")

private class FirFunction : FirNode("FirFunction") {
    val codeSamples = buildCodeSamples {
        val n by tag("class name")
        val p1 by tag("p1")
        val p2 by tag("p2")
        val p3 by tag("p3")
        val p4 by tag("p4")
        val p5 by tag("p5")
        val p6 by tag("p6")

        val base = """
            class ${n}FirFunction${n} {${p1}
              val typeParameters: List<FirTypeParameterRef>${p1}${p2}
              val dispatchReceiverType: ConeSimpleKotlinType?${p2}${p3}
              val receiverParameter: FirReceiverParameter?${p3}${p4}
              val valueParameters: List<FirValueParameter>${p4}${p5}
              val returnTypeRef: FirTypeRef${p5}${p6}
              val body: FirBlock?${p6}
            }
        """.trimIndent().toCodeSample()

        val node = CodeSample(AnnotatedString("FirFunction"))
        val baseCodeSample = base.hide(p1, p2, p3, p4, p5, p6)

        node
            .then { baseCodeSample }
            .then { reveal(p1) }
            .then { reveal(p2) }
            .then { reveal(p3) }
            .then { reveal(p4) }
            .then { reveal(p5) }
            .then { reveal(p6) }
            .then { node }
    }

    @Composable
    override fun Content(transition: Transition<Int>) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                .animateContentSize(tween(durationMillis = 300, delayMillis = 300))
        ) {
            ProvideTextStyle(MaterialTheme.typography.code2) {
                MagicText(
                    transition.createChildTransition {
                        codeSamples[it.coerceIn(codeSamples.indices)].string.splitByTags()
                    },
                )
            }
        }
    }
}

private val states = buildList {
    val function = FirFunction()
    val fClass = FirClass()
    val file = FirFile()
    val declaration = FirDeclaration(listOf(function, fClass, file))
    val expression = FirExpression()
    val element = FirElement(listOf(declaration, expression))

    repeat(9) { add(function) }
    add(FirDeclaration(listOf(function)))
    add(FirDeclaration(listOf(function, fClass)))
    add(declaration)

    add(FirElement(listOf(declaration)))
    add(element)
}.withIndex().toList()

@Composable
fun Transition<Int>.FirTree() {
    val transition = createChildTransition { states[it.coerceIn(states.indices)] }

    SharedTransitionLayout {
        transition.createChildTransition { it.value }.AnimatedContent(
            transitionSpec = { fadeIn() togetherWith fadeOut() },
        ) { root ->
            var offset by remember { mutableStateOf(Offset.Zero) }
            val placements = remember { mutableStateMapOf<FirNode, Rect>() }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                HorizontalTree(
                    root = root,
                    getChildren = { node -> node.children },
                    connection = { parent, parentRect, child, childRect ->
                        if (LocalSceneMode.current == SceneMode.Preview) {
                            Connection(parentRect, childRect)
                        }
                    },
                    modifier = Modifier.onPlaced {
                         try {
                            offset = it.positionInParent()
                        } catch (_: IllegalStateException) {
                            // TODO there seems to be a possible internal error
                            //  which can be caused by navigating the overview quickly.
                            //  - just ignore it?
                        }
                    }
                ) { node ->
                    FirElement(
                        node = node,
                        index = transition.createChildTransition { it.index },
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(node.name),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                            .onPlaced {
                                try {
                                    placements[node] = it.boundsInParent()
                                } catch (_: IllegalStateException) {
                                    // TODO there seems to be a possible internal error
                                    //  which can be caused by navigating the overview quickly.
                                    //  - just ignore it?
                                }
                            }
                    )
                }

                if (LocalSceneMode.current != SceneMode.Preview) {
                    for (element in placements) {
                        val (parent, parentRect) = element
                        for (child in parent.children) {
                            val childRect = placements[child] ?: continue

                            Connection(
                                parentRect = parentRect.translate(offset),
                                childRect = childRect.translate(offset),
                                modifier = Modifier.sharedElement(
                                    rememberSharedContentState("${parent.name}-${child.name}"),
                                    animatedVisibilityScope = this@AnimatedContent
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
context(scope: AnimatedVisibilityScope)
private fun FirElement(node: FirNode, index: Transition<Int>, modifier: Modifier = Modifier) {
    Box(modifier.border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(8.dp))) {
        node.Content(index)
    }
}

@Composable
private fun Connection(
    parentRect: Rect,
    childRect: Rect,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    stroke: Stroke = with(LocalDensity.current) { Stroke(width = 2.dp.toPx()) },
) {
    Canvas(modifier.fillMaxSize()) {
        // TODO ideas
        //  - can probably do this a bit more dynamically; determining closest edges?
        //  - can we add an arrow support, to indicate direction?

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
