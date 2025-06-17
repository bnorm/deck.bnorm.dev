@file:OptIn(ExperimentalUuidApi::class)

package dev.bnorm.deck.kc25.companion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.broadcast.VoteMessage
import dev.bnorm.deck.shared.broadcast.BroadcastClient
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private val id = Uuid.random().toString()

private const val CHANNEL_ID = "story-dcnyc25-vote"
private val voter = BroadcastClient(bearerToken = null, VoteMessage.serializer())

@Composable
fun Vote(current: Boolean?, onClick: (Boolean) -> VoteMessage) {
    val scope = rememberCoroutineScope()
    fun submit(message: VoteMessage) {
        scope.launch { voter.broadcast(CHANNEL_ID, message) }
    }

    val selected = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
    val default = ButtonDefaults.buttonColors()

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
            onClick = { submit(onClick(false)) },
            colors = if (current == false) selected else default
        ) {
            Text("No")
        }
        Button(
            onClick = { submit(onClick(true)) },
            colors = if (current == true) selected else default
        ) {
            Text("Yes")
        }
    }
}

@Composable
private fun MultiVote(question: String, initial: VoteMessage.MultiVoteMessage) {
    var vote by remember { mutableStateOf(initial) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            question,
            style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Light)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Code Editor", style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Light))
                Vote(vote.editor) { vote.copy(editor = it).also { vote = it } }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Code Review", style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Light))
                Vote(vote.review) { vote.copy(review = it).also { vote = it } }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Presentation Slides", style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Light))
                Vote(vote.slides) { vote.copy(slides = it).also { vote = it } }
            }
        }
    }
}

@Composable
fun SendCodeVote() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            var vote by remember { mutableStateOf(VoteMessage.Code(userId = id)) }

            Text("Do you read code?", style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Light))
            Vote(vote.value) { vote.copy(value = it).also { vote = it } }
        }

        MultiVote("Does it provide good context?", VoteMessage.Context(userId = id))
        MultiVote("Does it have good style?", VoteMessage.Style(userId = id))
        MultiVote("Good provide enough time?", VoteMessage.Time(userId = id))
    }
}

@Composable
fun App() {
    SendCodeVote()
}
