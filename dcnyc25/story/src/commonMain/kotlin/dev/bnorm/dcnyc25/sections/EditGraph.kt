package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import dev.bnorm.dcnyc25.template.animateScroll
import dev.bnorm.storyboard.LocalStoryboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.EditGraph() {
    scene(
        stateCount = 13,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
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
                        ExampleEditGraph(
                            size = height,
                            transition = transition.createChildTransition { it.toState() },
                            modifier = Modifier
                                .scale(1 / 5f)
                                .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                        )

                        Box(
                            Modifier
                                .align(Alignment.Center)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
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
fun ExampleEditGraph(size: Dp, transition: Transition<Int>, modifier: Modifier = Modifier) {
    val insert = "artwork"
    val delete = "driftwood"

    val vScroll = rememberScrollState()
    val hScroll = rememberScrollState()

    transition.animateScroll(vScroll) {
        val size = size.value.toInt()
        when (it) {
            0 -> 0
            1 -> 0
            2 -> 1
            3 -> 2
            4 -> 2
            5 -> 2
            6 -> 3
            7 -> 4
            8 -> 5
            9 -> 5
            10 -> 5
            11 -> 6
            else -> insert.length
        } * size
    }

    transition.animateScroll(hScroll) {
        val size = size.value.toInt()
        when (it) {
            0 -> 0
            1 -> 1
            2 -> 1
            3 -> 2
            4 -> 3
            5 -> 4
            6 -> 5
            7 -> 6
            8 -> 7
            9 -> 8
            else -> delete.length
        } * size
    }

    EditGraph(
        size = size,
        insert = insert,
        delete = delete,
        modifier = modifier.offset(-hScroll.value.dp, -vScroll.value.dp)
    )
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