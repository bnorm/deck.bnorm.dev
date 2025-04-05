package dev.bnorm.evolved.sections.today

import androidx.compose.animation.*
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.*
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.MagicCode
import dev.bnorm.evolved.template.code.twice
import dev.bnorm.storyboard.StoryboardBuilder
import kotlin.math.PI
import kotlin.math.atan2

fun StoryboardBuilder.TransformTree() {
    // TODO needs lots of work to be clear what is happening

    val root = EXPRESSION_TREE_ROOT
    val nodes = root.flattenNodes()

    scene(stateCount = 15) {
        val child = frame.createChildTransition { it.toState() }

        HeaderAndBody {
            Box(Modifier.fillMaxSize()) {
                ProvideTextStyle(MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)) {
                    child.MagicCode(TRANSFORM_TRANSFORMATIONS)
                }

                SharedTransitionLayout {
                    child.AnimatedContent(
                        transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                    ) {
                        Box(Modifier.fillMaxSize().padding(bottom = 32.dp)) {
                            TreeNodes(root, Modifier.fillMaxSize())

                            val node = nodes.getOrNull(it)
                            if (node != null) {
                                SurroundingBox(
                                    root,
                                    node,
                                    this@SharedTransitionLayout,
                                    this@AnimatedContent,
                                    Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Node.flattenNodes(): List<Node> = buildList {
    fun recursive(node: Node) {
        add(node)
        for ((child, _, _) in node.children) {
            recursive(child)
        }
    }
    recursive(this@flattenNodes)
}

@Composable
private fun TreeNodes(root: Node, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val links = mutableListOf<Node.Link>()

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
            Node(childPosition, child)
            links.add(Node.Link(position + link.start, childPosition + link.end, link.label))
        }
    }

    BoxWithConstraints(modifier) {
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

@Composable
private fun SurroundingBox(
    root: Node,
    surround: Node,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    fun findRec(position: DpOffset, node: Node): DpRect? {
        if (node === surround) return DpRect(position, node.size)

        for ((child, offset, _) in node.children) {
            val childPosition = position + offset
            findRec(childPosition, child)?.let { return it }
        }

        return null
    }

    with(sharedTransitionScope) {
        BoxWithConstraints(modifier) {
            val position = DpOffset(0.dp, maxHeight / 2)
            val rect = findRec(position, root) ?: return@BoxWithConstraints

            Box(
                Modifier
                    .offset(rect.left, rect.top)
                    .size(rect.width, rect.height)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("surround-box"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                    .border(2.dp, MaterialTheme.colors.primary)
            )
        }
    }
}

val TRANSFORM_TRANSFORMATIONS = listOf(
    """
    """.trimIndent().twice(),

    """
    """.trimIndent() to """
        <i>val tmp1 = str</i>
    """.trimIndent(),

    """
        val tmp1 = str
    """.trimIndent() to """
        val tmp1 = str
        <i>val tmp2 = tmp1.length</i>
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
    """.trimIndent().twice(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        <i>val tmp3 = tmp2 >= 1</i>
        <i>if (tmp3) {</i>
        <i>}</i>
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
        }
    """.trimIndent().twice(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
        <i>    val tmp4 = str</i>
        }
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
        }
    """.trimIndent().twice(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
        <i>    val tmp5 = tmp4[0]</i>
        }
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
        }
    """.trimIndent().twice(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
        <i>    val tmp6 = tmp5 == 'x'</i>
        <i>    assert(tmp6)</i>
        }
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            val tmp6 = tmp5 == 'x'
            assert(tmp6)
        }<i></i>
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            val tmp6 = tmp5 == 'x'
            assert(tmp6)
        }<i> else {</i>
        <i>}</i>
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            val tmp6 = tmp5 == 'x'
            assert(tmp6)
        } else {
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            val tmp6 = tmp5 == 'x'
            assert(tmp6)
        } else {
        <i>    assert(false)</i>
        }
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            val tmp6 = tmp5 == 'x'
            assert(tmp6)
        } else {
            assert(false)
        }
    """.trimIndent().twice(),
)
