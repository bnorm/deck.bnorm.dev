package dev.bnorm.evolved.sections.today

import androidx.compose.animation.*
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.*
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import kotlin.math.PI
import kotlin.math.atan2

fun StoryboardBuilder.ExpressionTree() {
    // TODO switch to using the when-expression style example
    val sample = """
        require(str.length >= 1 && str[0] == 'x')
    """.trimIndent()

    fun getSnippet(i: Int): IntRange? {
        val snippet = when (i) {
            in 1..2 -> "str.length >= 1 && str[0] == 'x'"
            3 -> "str"
            4 -> "str.length"
            5 -> "1"
            6 -> "str.length >= 1"
            7 -> "&&"
            8 -> "str"
            9 -> "0"
            10 -> "str[0]"
            11 -> "'x'"
            12 -> "str[0] == 'x'"
            in 13..14 -> "&&"
            else -> return null
        }
        val index = sample
            .indexOf(snippet, startIndex = if (i == 8) 11 else 0)
            .takeIf { it >= 0 } ?: return null

        return index..(index + snippet.length)
    }

    slide(stateCount = 15) {
        val child = state.createChildTransition { it.toState() }

        HeaderAndBody {
            Box {
                var textLayoutResult by remember<MutableState<TextLayoutResult?>> { mutableStateOf(null) }
                Text(text = sample.toCode(), onTextLayout = { textLayoutResult = it })
                textLayoutResult?.let { result ->
                    SharedTransitionLayout {
                        child.AnimatedContent(
                            transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                        ) {
                            TreeNodes(it, Modifier.fillMaxSize().padding(bottom = 32.dp))

                            SurroundingBox(result, it, this@SharedTransitionLayout, this@AnimatedContent, ::getSnippet)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TreeNodes(
    index: Int,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    val links = mutableListOf<Node.Link>()
    val root = EXPRESSION_TREE_ROOT

    @Composable
    fun Node(position: DpOffset, node: Node) {
        Box(Modifier.offset(position.x, position.y).size(node.size).border(2.dp, Color.Gray)) {
            Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    node.content()
                }
            }
        }

        for ((child, offset, link) in node.children) {
            val childPosition = position + offset
            if (index >= child.reveal) {
                Node(childPosition, child)
                links.add(Node.Link(position + link.start, childPosition + link.end, link.label))
            }
        }
    }

    BoxWithConstraints(modifier) {
        if (index >= root.reveal) {
            val position = DpOffset(0.dp, maxHeight / 2)
            Node(position, root)

            Canvas(Modifier.fillMaxSize()) {
                for (link in links) {
                    val start = Offset(link.start.x.toPx(), link.start.y.toPx())
                    val end = Offset(link.end.x.toPx(), link.end.y.toPx())
                    drawLine(
                        color = Color.Gray,
                        start = start,
                        end = end,
                        strokeWidth = 2.dp.toPx(),
                    )
                    if (link.label != null) {
                        val result = textMeasurer.measure(
                            text = link.label,
                            style = TextStyle(color = Color.LightGray, fontSize = 12.sp),
                        )

                        val vector = end - start
                        val lineCenter = start + vector / 2f
                        rotate(degrees = atan2(vector.y, vector.x) * 180f / PI.toFloat(), lineCenter) {
                            drawText(
                                textLayoutResult = result,
                                topLeft = lineCenter - Offset(
                                    result.size.width.toFloat() / 2f,
                                    result.size.height.toFloat()
                                ),
                                color = Color.LightGray,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> SurroundingBox(
    result: TextLayoutResult,
    item: T,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    getRange: (T) -> IntRange?,
) {
    val range = getRange(item) ?: return
    val path = result.getPathForRange(range.start, range.endInclusive).getBounds()
    val size = (path.bottomRight - path.topLeft + Offset(20f, 0f)).round()
    val offset = (path.topLeft - Offset(10f, 0f)).round()

    with(sharedTransitionScope) {
        with(LocalDensity.current) {
            Box(
                Modifier
                    .size(size.x.toDp(), size.y.toDp())
                    .offset(offset.x.toDp(), offset.y.toDp())
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("text-box"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                    .border(2.dp, MaterialTheme.colors.primary)
            )
        }
    }
}
