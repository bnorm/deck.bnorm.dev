package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import dev.bnorm.dcnyc25.algo.SearchCallbacks
import dev.bnorm.dcnyc25.algo.SearchPath
import dev.bnorm.dcnyc25.algo.myers
import dev.bnorm.dcnyc25.template.COLORS
import dev.bnorm.dcnyc25.template.SceneHalfWidth
import dev.bnorm.dcnyc25.template.TextSurface
import dev.bnorm.dcnyc25.template.Vertical
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

private sealed class EditGraphState(
    val deleteVisible: Boolean = true,
    val insertVisible: Boolean = true,
    val graphVisible: Boolean = true,
    val search: Boolean = false,
) {
    data object Blank : EditGraphState(deleteVisible = false, insertVisible = false, graphVisible = false)
    data object Delete : EditGraphState(insertVisible = false, graphVisible = false)
    data object Insert : EditGraphState(graphVisible = false)
    data object Graph : EditGraphState()
    data object Search : EditGraphState(search = true)
    data object Solution : EditGraphState(search = true)
    data class Path(val index: Int) : EditGraphState(search = true)
    data object Complete : EditGraphState(search = true)
}

fun StoryboardBuilder.EditGraph() {
    val delete = "kotlinconf".toList()
    val insert = "droidcon".toList()

    val paths = mutableListOf<SearchPath>()
    myers(delete, insert, callbacks = object : SearchCallbacks {
        override fun onHead(path: SearchPath) {
            paths.add(path)
        }
    })
    val solution = paths.last()

    val states = buildList {
        add(EditGraphState.Blank)
        add(EditGraphState.Delete)
        add(EditGraphState.Insert)
        add(EditGraphState.Graph)
        add(EditGraphState.Search)
        add(EditGraphState.Solution)
        repeat(solution.size) {
            add(EditGraphState.Path(it))
        }
        add(EditGraphState.Complete)
    }

    scene(
        states = states,
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {

        Surface(color = MaterialTheme.colors.secondary) {
            Row {
                Vertical(MaterialTheme.colors.primary) {
                    MyersDiffInfo(Modifier.sharedElement(rememberSharedContentState("myers-diff")))
                }
                Vertical(MaterialTheme.colors.secondary) {
                    EditGraph(
                        delete = delete,
                        insert = insert,
                        paths = paths,
                        solution = solution,
                        size = SceneHalfWidth,
                        transition = transition.createChildTransition { it.toState() })
                }
            }
        }
    }
}

@Composable
private fun EditGraph(
    delete: List<Char>,
    insert: List<Char>,
    paths: MutableList<SearchPath>,
    solution: SearchPath,
    size: Dp,
    transition: Transition<out EditGraphState>,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val deleteIndex by transition.animateInt(
            transitionSpec = { tween(50 * delete.size, easing = LinearEasing) }
        ) { if (it.deleteVisible) delete.size else 0 }

        val insertIndex by transition.animateInt(
            transitionSpec = { tween(50 * insert.size, easing = LinearEasing) }
        ) { if (it.insertVisible) insert.size else 0 }

        val graphColor by transition.animateColor(
            transitionSpec = { tween(300) }
        ) {
            if (it.graphVisible) Color.White else Color.White.copy(alpha = 0.0f)
        }

        val pathsColor by transition.animateColor(transitionSpec = { tween(300) }) {
            when (it) {
                is EditGraphState.Insert,
                is EditGraphState.Search,
                    -> Color.Black

                else -> Color.Black.copy(alpha = 0.0f)
            }
        }

        val searchIndex by transition.animateInt(transitionSpec = {
            if (!initialState.search && targetState.search) {
                tween(durationMillis = 250 * paths.size, easing = LinearEasing)
            } else {
                tween(durationMillis = 0)
            }
        }) {
            if (it.search) paths.size else 0
        }

        val pathIndex = transition.createChildTransition {
            val state = it
            when (state) {
                is EditGraphState.Path -> state.index
                is EditGraphState.Complete -> solution.size + 1
                else -> -1
            }
        }

        val yOffset by pathIndex.animateDp(
            transitionSpec = { tween(300, easing = EaseInOut) }
        ) { index ->
            (if (index in solution.indices) solution[index].y else insert.size / 2) * size
        }

        val xOffset by pathIndex.animateDp(
            transitionSpec = { tween(300, easing = EaseInOut) }
        ) { index ->
            (if (index in solution.indices) solution[index].x else delete.size / 2) * size
        }

        val scale by pathIndex.animateFloat(
            transitionSpec = { tween(300, easing = EaseInOut) }
        ) { index ->
            if (index in solution.indices) 1f / 5f else 1f / maxOf(insert.size + 1, delete.size + 1)
        }

        EditGraph(
            size = SceneHalfWidth,
            insert = insert.subList(0, insertIndex),
            delete = delete.subList(0, deleteIndex),
            color = graphColor,
            modifier = Modifier
                .align(Alignment.Center)
                .size(size, size)
                .scale(scale)
                .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                .offset(-xOffset, -yOffset)
                .drawWithContent {
                    val size = size.toPx() / 2
                    drawContent()

                    val paths = paths.subList(fromIndex = 0, toIndex = searchIndex)
                    for ((i, path) in paths.withIndex()) {
                        val draw = Path()
                        for ((i, offset) in path.withIndex()) {
                            val x = size + (offset.x * 2 * size)
                            val y = size + (offset.y * 2 * size)
                            if (i == 0) {
                                draw.moveTo(x, y)
                            } else {
                                draw.lineTo(x, y)
                            }
                        }
                        if (i == paths.lastIndex) {
                            drawPath(
                                path = draw,
                                color = COLORS.primary,
                                style = Stroke(
                                    width = 64.dp.toPx(),
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round,
                                ),
                            )
                        } else {
                            drawPath(
                                path = draw,
                                color = pathsColor,
                                style = Stroke(
                                    width = 32.dp.toPx(),
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round,
                                ),
                            )
                        }
                    }
                }
        )

        if (pathIndex.currentState in solution.indices && pathIndex.targetState in solution.indices) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }

        pathIndex.AnimatedVisibility(
            visible = { it >= 0 },
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                color = Color.White.copy(alpha = 0.75f),
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    textDiff(
                        solution,
                        pathIndex.currentState.coerceIn(solution.indices),
                        insert,
                        delete
                    ),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.h3,
                )
            }
        }
    }
}

fun textDiff(path: SearchPath, index: Int, insert: List<Char>, delete: List<Char>): AnnotatedString {
    val iter = path.subList(fromIndex = 0, toIndex = index + 1).iterator()
    if (!iter.hasNext()) return AnnotatedString("")

    return buildAnnotatedString {
        var last = iter.next()
        while (iter.hasNext()) {
            val next = iter.next()
            if (last.x + 1 == next.x && last.y + 1 == next.y) {
                // Diag
                append(insert[next.y - 1])
            } else if (last.x + 1 == next.x) {
                // Right
                withStyle(SpanStyle(background = Color.Red.copy(alpha = 0.5f))) {
                    append(delete[next.x - 1])
                }
            } else {
                // Down
                withStyle(SpanStyle(background = Color.Green.copy(alpha = 0.5f))) {
                    append(insert[next.y - 1])
                }
            }
            last = next
        }
    }
}

@Composable
fun MyersDiffInfo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("Myers Diff", style = MaterialTheme.typography.h2)
        }
        TextSurface {
            Column(Modifier.padding(16.dp)) {
                Text("• Computes the shortest edit script for transforming one sequence into another.")
//                Text("• An O(N*D) algorithm.")
                Text("• Published in 1986 by Eugene Myers.")
            }
        }
    }
}

@Composable
private fun <T> EditGraph(
    size: Dp,
    insert: List<T>,
    delete: List<T>,
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit = {
        Text(it.toString())
    },
) {
    val textStyle = TextStyle(
        fontSize = with(LocalDensity.current) { size.toSp() / 3 },
        fontWeight = FontWeight.Bold,
    )
    ProvideTextStyle(textStyle) {
        Row(modifier) {
            Column(Modifier.padding(top = size / 2).width(size / 2).rightBorder(8.dp, color)) {
                for (i in insert) {
                    Box(Modifier.height(size).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        content(i)
                    }
                }
            }
            Column {
                Row(Modifier.height(size / 2).bottomBorder(8.dp, color)) {
                    for (d in delete) {
                        Box(Modifier.width(size).fillMaxHeight(), contentAlignment = Alignment.Center) {
                            content(d)
                        }
                    }
                }
                Column(Modifier.border(8.dp, color)) {
                    for (i in insert) {
                        Row {
                            for (d in delete) {
                                Box(Modifier.size(size).border(8.dp, color)) {
                                    if (i == d) {
                                        Canvas(Modifier.fillMaxSize()) {
                                            drawLine(
                                                color = color,
                                                start = Offset(0f, 0f),
                                                end = Offset(size.toPx(), size.toPx()),
                                                strokeWidth = 16.dp.toPx()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Box(Modifier.size(delete.size * size, size / 2).topBorder(8.dp, color))
            }
            Box(Modifier.padding(top = size / 2).size(size / 2, insert.size * size).leftBorder(8.dp, color))
        }
    }
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color): Modifier = drawBehind {
    val strokeWidthPx = strokeWidth.toPx()

    val width = size.width
    val height = size.height - strokeWidthPx / 2

    drawLine(
        color = color,
        start = Offset(x = 0f, y = height),
        end = Offset(x = width, y = height),
        strokeWidth = strokeWidthPx
    )
}

fun Modifier.topBorder(strokeWidth: Dp, color: Color): Modifier = drawBehind {
    val strokeWidthPx = strokeWidth.toPx()

    val width = size.width
    val height = strokeWidthPx / 2

    drawLine(
        color = color,
        start = Offset(x = 0f, y = height),
        end = Offset(x = width, y = height),
        strokeWidth = strokeWidthPx
    )
}

fun Modifier.rightBorder(strokeWidth: Dp, color: Color): Modifier = drawBehind {
    val strokeWidthPx = strokeWidth.toPx()

    val width = size.width - strokeWidthPx / 2
    val height = size.height

    drawLine(
        color = color,
        start = Offset(x = width, y = 0f),
        end = Offset(x = width, y = height),
        strokeWidth = strokeWidthPx
    )
}

fun Modifier.leftBorder(strokeWidth: Dp, color: Color): Modifier = drawBehind {
    val strokeWidthPx = strokeWidth.toPx()

    val width = strokeWidthPx / 2
    val height = size.height

    drawLine(
        color = color,
        start = Offset(x = width, y = 0f),
        end = Offset(x = width, y = height),
        strokeWidth = strokeWidthPx
    )
}
