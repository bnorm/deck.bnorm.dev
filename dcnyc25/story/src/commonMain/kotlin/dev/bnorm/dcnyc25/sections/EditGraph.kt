package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.algo.myers
import dev.bnorm.dcnyc25.template.animateScroll
import dev.bnorm.storyboard.LocalStoryboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.assist.SceneCaption
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import kotlinx.coroutines.delay

fun StoryboardBuilder.EditGraph() {
    val start = TextFieldState("kotlinconf")
    val end = TextFieldState("droidcon")
    var path by mutableStateOf(myers(start.text.toString(), end.text.toString()))
    var index by mutableIntStateOf(-1)

    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SceneCaption {
            LaunchedEffect(start.text, end.text) {
                delay(300)
                index = -1
                path = myers(start.text.toString(), end.text.toString())
            }

            Column {
                TextField(start, label = { Text("Start") })
                Spacer(Modifier.size(8.dp))
                TextField(end, label = { Text("End") })
                Spacer(Modifier.size(8.dp))
                Row {
                    Button(onClick = { index = (index - 1).coerceIn(-1, path.lastIndex) }) {
                        Text("<<")
                    }
                    Spacer(Modifier.size(8.dp))
                    Button(onClick = { index = (index + 1).coerceIn(-1, path.lastIndex) }) {
                        Text(">>")
                    }
                }
            }
        }

        val format = LocalStoryboard.current!!.format
        val width = with(format.density) { format.size.width.toDp() }
        val height = with(format.density) { format.size.height.toDp() }

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
                        val delete = start.text.toString()
                        val insert = end.text.toString()
                        val size = height.value.toInt()

                        key(insert, delete, size) {
                            val index = updateTransition(index)
                            val vScroll = rememberScrollState()
                            val hScroll = rememberScrollState()

                            val scale by index.animateFloat(transitionSpec = {
                                tween(300, easing = LinearEasing)
                            }) {
                                if (it < 0) 1f / maxOf(insert.length + 1, delete.length + 1)
                                else 1f / 5f
                            }

                            index.animateScroll(vScroll, transitionSpec = {
                                tween(300, easing = LinearEasing)
                            }) {
                                if (it < 0) size * insert.length / 2
                                else (path[it].y + 1) * size
                            }

                            index.animateScroll(hScroll, transitionSpec = {
                                tween(300, easing = LinearEasing)
                            }) {
                                if (it < 0) size * delete.length / 2
                                else (path[it].x + 1) * size
                            }

                            EditGraph(
                                size = height,
                                insert = insert,
                                delete = delete,
                                modifier = Modifier
                                    .scale(scale)
                                    .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                                    .offset(-hScroll.value.dp, -vScroll.value.dp)
                            )

                            if (index.currentState >= 0 && index.targetState >= 0) {
                                Box(
                                    Modifier
                                        .align(Alignment.Center)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )

                                Surface(
                                    color = Color.White.copy(alpha = 0.75f),
                                    contentColor = Color.Black,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                                ) {
                                    Text(
                                        ">${index.currentState}",
                                        modifier = Modifier.padding(12.dp)
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
                Text("• An O(N*D) algorithm.")
                Text("• Published in 1986 by Eugene Myers.")
            }
        }
    }
}

@Composable
private fun EditGraph(size: Dp, insert: String, delete: String, modifier: Modifier = Modifier) {
    val textStyle = TextStyle(fontSize = with(LocalDensity.current) { size.toSp() / 3 })
    Row(modifier) {
        Column(Modifier.width(size / 2)) {
            Spacer(Modifier.height(size))
            for (i in insert) {
                Box(Modifier.height(size).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(i.toString(), style = textStyle)
                }
            }
        }
        Column {
            Row(Modifier.height(size / 2)) {
                Spacer(Modifier.width(size / 2))
                for (d in delete) {
                    Box(Modifier.width(size).fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text(d.toString(), style = textStyle)
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
