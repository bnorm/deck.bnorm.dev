package dev.bnorm.dcnyc25.sections

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.algo.SearchCallbacks
import dev.bnorm.dcnyc25.algo.SearchPath
import dev.bnorm.dcnyc25.algo.myers
import dev.bnorm.dcnyc25.template.COLORS
import dev.bnorm.storyboard.LocalStoryboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.assist.SceneCaption
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.StoryEffect
import kotlinx.coroutines.delay

fun StoryboardBuilder.EditGraph() {
    val start = TextFieldState("kotlinconf")
    val end = TextFieldState("droidcon")
//    var path by mutableStateOf(myers(start.text.toString(), end.text.toString()))
//    var index by mutableIntStateOf(-1)

    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SceneCaption {
//            LaunchedEffect(start.text, end.text) {
//                delay(300)
//                index = -1
//                path = myers(start.text.toString(), end.text.toString())
//            }

            Column {
                TextField(start, label = { Text("Start") })
                Spacer(Modifier.size(8.dp))
                TextField(end, label = { Text("End") })
//                Spacer(Modifier.size(8.dp))
//                Row {
//                    Button(onClick = { index = (index - 1).coerceIn(-1, path.lastIndex) }) {
//                        Text("<<")
//                    }
//                    Spacer(Modifier.size(8.dp))
//                    Button(onClick = { index = (index + 1).coerceIn(-1, path.lastIndex) }) {
//                        Text(">>")
//                    }
//                }
            }
        }

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
                        val delete = start.text.toString()
                        val insert = end.text.toString()

                        key(insert, delete) {
                            val paths = remember { mutableStateSetOf<SearchPath>() }
                            var active by remember { mutableStateOf<SearchPath?>(null) }

                            val vScroll = rememberScrollState(size * insert.length / 2)
                            val hScroll = rememberScrollState(size * delete.length / 2)
                            val scale = 1f / maxOf(insert.length + 1, delete.length + 1)

                            StoryEffect {
                                myers(delete, insert, callbacks = object : SearchCallbacks {
                                    override suspend fun onHead(path: SearchPath) {
                                        active = path
                                        delay(100)
                                    }

                                    override suspend fun onRight(path: SearchPath) {
                                        paths.add(path)
                                        active = path
                                        delay(100)
                                    }

                                    override suspend fun onDown(path: SearchPath) {
                                        paths.add(path)
                                        active = path
                                        delay(100)
                                    }

                                    override suspend fun onDiag(path: SearchPath) {
                                        paths.add(path)
                                        active = path
                                        delay(100)
                                    }
                                })
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
                                        fun drawSearchPath(searchPath: SearchPath, color: Color) {
                                            val path = Path()
                                            for ((i, offset) in searchPath.withIndex()) {
                                                val x =
                                                    format.size.height / 2 + (offset.x * format.size.height).toFloat()
                                                val y =
                                                    format.size.height / 2 + (offset.y * format.size.height).toFloat()
                                                if (i == 0) {
                                                    path.moveTo(x, y)
                                                } else {
                                                    path.lineTo(x, y)
                                                }
                                            }
                                            drawPath(path, color, style = Stroke(width = 64f))
                                        }

                                        for (path in paths) drawSearchPath(path, Color.DarkGray)
                                        active?.let { drawSearchPath(it, COLORS.primary) }
                                    }
                            )
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
            Spacer(Modifier.height(size / 2))
            for (i in insert) {
                Box(Modifier.height(size).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(i.toString(), style = textStyle)
                }
            }
        }
        Column {
            Row(Modifier.height(size / 2)) {
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
