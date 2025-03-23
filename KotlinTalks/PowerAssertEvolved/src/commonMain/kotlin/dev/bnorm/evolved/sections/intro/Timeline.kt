package dev.bnorm.evolved.sections.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.powerassertevolved.generated.resources.*
import dev.bnorm.deck.shared.KodeeElectrified
import dev.bnorm.deck.shared.KodeeLoving
import dev.bnorm.deck.shared.KodeeSurprised
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.storyboard.core.StoryboardBuilder
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.Timeline() {
    data class TimelineState(
        val image: DrawableResource,
        val link: String? = null,
        val kodee: (@Composable () -> Unit)? = null,
    )

    val kodeeSurprised = movableContentOf { KodeeSurprised(modifier = Modifier.requiredSize(75.dp)) }
    val kodeeLoving = movableContentOf { KodeeLoving(modifier = Modifier.requiredSize(75.dp)) }
    val kodeeElectrified = movableContentOf { KodeeElectrified(modifier = Modifier.requiredSize(75.dp)) }

    val states = listOf(
        TimelineState(
            image = Res.drawable.Timeline_20200204_Slack,
            link = "https://kotlinlang.slack.com/archives/C7L3JB43G/p1580838007045000",
        ),
        TimelineState(
            image = Res.drawable.Timeline_20200205_Slack,
            link = "https://kotlinlang.slack.com/archives/C7L3JB43G/p1580838007045000",
            kodee = kodeeSurprised,
        ),
        TimelineState(
            image = Res.drawable.Timeline_20200210_Twitter,
            link = " https://twitter.com/bnormcodes/status/1226929535620390912",
        ),
        TimelineState(
            image = Res.drawable.Timeline_20200210_TwitterReply,
            link = "https://twitter.com/relizarov/status/1227616757709524993",
            kodee = kodeeSurprised,
        ),
        TimelineState(
            image = Res.drawable.Timeline_20201012_Twitter,
            link = "https://twitter.com/bnormcodes/status/1315703944065187841",
            kodee = kodeeLoving,
        ),
        TimelineState(
            image = Res.drawable.Timeline_20230130_GMail,
            kodee = kodeeElectrified,
        ),
        TimelineState(
            image = Res.drawable.Timeline_20240618_Twitter,
            link = "https://twitter.com/bnormcodes/status/1803055203513487763",
            kodee = kodeeLoving,
        ),
    )

    scene(states = states) {
        HeaderAndBody(kodee = {
            for (s in states) {
                if (s.kodee == null) continue
                show(condition = { currentState == s }, content = s.kodee)
            }
        }) {
            // TODO max size but top align
            Image(
                painter = painterResource(currentState.image),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
