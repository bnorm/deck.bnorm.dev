package dev.bnorm.kc25.broadcast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
fun ReactionGraph(reactionListener: ReactionListener?) {
    val keys = remember {
        listOf(
            ReactionMessage.Heart(0).clear(),
            ReactionMessage.Excited(0).clear(),
            ReactionMessage.Electrified(0).clear(),
            ReactionMessage.Lost(0).clear(),
        )
    }
    val queues = remember { mutableStateMapOf<ReactionMessage, ArrayDeque<Instant>>() }
    var lastClearTime by remember { mutableStateOf(Instant.DISTANT_PAST) }
    LaunchedEffect(reactionListener) {
        reactionListener?.let { listener ->
            listener.listen()
                .drop(1)
                .filter { it !is ReactionMessage.Ping }
                .map { it }
                .collect {
                    val clearTime = Instant.fromEpochMilliseconds(it.timestamp) + 15.seconds
                    lastClearTime = clearTime
                    queues.getOrPut(it.clear()) { ArrayDeque() }.addLast(clearTime)
                }
        }
    }

    var nextClearTime by remember { mutableStateOf<Instant?>(null) }
    val clearTime = nextClearTime ?: lastClearTime
    LaunchedEffect(clearTime) {
        val now = Clock.System.now()
        if (nextClearTime == null && clearTime > now) nextClearTime = lastClearTime
        delay(clearTime - now)
        var next: Instant? = null
        for ((_, queue) in queues) {
            while (queue.isNotEmpty()) {
                val first = queue.removeFirst()
                if (first > clearTime) {
                    queue.addFirst(first) // Add it back.
                    next = minOf(next ?: first, first)
                    break
                }
            }
        }
        nextClearTime = next
    }

    lastClearTime // Always read the last clear time so the ArrayQueues are redrawn.
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (base in keys) {
            val count = queues[base]?.size ?: 0
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                base.Image(Modifier.size(32.dp))
                Text("$count", textAlign = TextAlign.End, modifier = Modifier.width(64.dp))

                Row(Modifier.weight(1f)) {
                    val value = count / (count + 10f)
                    if (value > 0) {
                        Box(
                            Modifier.height(32.dp).background(Color.White)
                                .weight(value)
                        )
                    }
                    Box(
                        Modifier.height(32.dp).background(Color.DarkGray)
                            .weight(1f - value)
                    )
                }
            }
        }
    }
}
