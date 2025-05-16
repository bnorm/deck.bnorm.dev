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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.algo.SearchCallbacks
import dev.bnorm.dcnyc25.algo.SearchPath
import dev.bnorm.dcnyc25.algo.myers
import dev.bnorm.dcnyc25.template.COLORS
import dev.bnorm.dcnyc25.template.animateScroll
import dev.bnorm.storyboard.LocalStoryboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

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

    scene(
        stateCount = 3 + solution.size + 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        val format = LocalStoryboard.current!!.format
        val width = with(format.density) { format.size.width.toDp() }
        val height = with(format.density) { format.size.height.toDp() }
        val size = height.value.toInt()

        Surface(color = MaterialTheme.colors.secondary) {
            Row {
                Surface(
                    Modifier.fillMaxHeight().width(width - height),
                    color = MaterialTheme.colors.primary
                ) {
                    LeftPane()
                }
                Surface(Modifier.fillMaxHeight().width(height), color = MaterialTheme.colors.secondary) {
                    Box {
                        val pathsColor by transition.animateColor(transitionSpec = { tween(300) }) {
                            if (it.toState() < 2) Color.Black.copy(alpha = 0.75f) else Color.Transparent
                        }

                        val pathsIndex by transition.animateInt(transitionSpec = {
                            if (targetState.toState() == 1 && initialState.toState() == 0) {
                                tween(durationMillis = 250 * paths.size, easing = LinearEasing)
                            } else {
                                tween(durationMillis = 0)
                            }
                        }) {
                            if (it.toState() > 0) paths.size else 0
                        }

                        val solutionIndex = transition.createChildTransition { it.toState() - 3 }

                        val vScroll = rememberScrollState(size * insert.size / 2)
                        solutionIndex.animateScroll(
                            vScroll,
                            transitionSpec = { tween(300, easing = EaseInOut) }
                        ) { index ->
                            size * if (index in solution.indices) solution[index].y else insert.size / 2
                        }

                        val hScroll = rememberScrollState(size * delete.size / 2)
                        solutionIndex.animateScroll(
                            hScroll,
                            transitionSpec = { tween(300, easing = EaseInOut) }
                        ) { index ->
                            size * if (index in solution.indices) solution[index].x else delete.size / 2
                        }

                        val scale by solutionIndex.animateFloat(transitionSpec = {
                            tween(300, easing = EaseInOut)
                        }) { index ->
                            if (index in solution.indices) 1f / 5f else 1f / maxOf(insert.size + 1, delete.size + 1)
                        }

                        EditGraph(
                            size = height,
                            insert = insert,
                            delete = delete,
                            modifier = Modifier
                                .scale(scale)
                                .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                                .offset(-hScroll.value.dp, -vScroll.value.dp)
                                .drawWithContent {
                                    drawContent()

                                    val paths = paths.subList(fromIndex = 0, toIndex = pathsIndex)
                                    for ((i, path) in paths.withIndex()) {
                                        val draw = Path()
                                        for ((i, offset) in path.withIndex()) {
                                            val x =
                                                format.size.height / 2 + (offset.x * format.size.height).toFloat()
                                            val y =
                                                format.size.height / 2 + (offset.y * format.size.height).toFloat()
                                            if (i == 0) {
                                                draw.moveTo(x, y)
                                            } else {
                                                draw.lineTo(x, y)
                                            }
                                        }
                                        if (i == paths.size - 1) {
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

                        if (solutionIndex.currentState in solution.indices && solutionIndex.targetState in solution.indices) {
                            Box(
                                Modifier
                                    .align(Alignment.Center)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                        }

                        solutionIndex.AnimatedVisibility(
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
                                        solutionIndex.currentState.coerceIn(solution.indices),
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
fun LeftPane() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ) {
        Text("Myers Diff", style = MaterialTheme.typography.h3)
        Spacer(Modifier.size(16.dp))
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
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
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit = {
        Text(it.toString())
    },
) {
    val textStyle = TextStyle(fontSize = with(LocalDensity.current) { size.toSp() / 3 })
    ProvideTextStyle(textStyle) {
        Row(modifier) {
            Column(Modifier.width(size / 2)) {
                Spacer(Modifier.height(size / 2))
                for (i in insert) {
                    Box(Modifier.height(size).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        content(i)
                    }
                }
            }
            Column {
                Row(Modifier.height(size / 2)) {
                    for (d in delete) {
                        Box(Modifier.width(size).fillMaxHeight(), contentAlignment = Alignment.Center) {
                            content(d)
                        }
                    }
                }
                Column(Modifier.border(16.dp, Color.White)) {
                    for (i in insert) {
                        Row {
                            for (d in delete) {
                                Box(Modifier.size(size).border(8.dp, Color.White)) {
                                    if (i == d) {
                                        Canvas(Modifier.fillMaxSize()) {
                                            drawLine(
                                                color = Color.White,
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
            }
        }
    }
}
