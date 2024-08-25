package dev.bnorm.deck

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.KodeeLoving
import dev.bnorm.kc24.KotlinPlusPowerAssertEqualsLove

@Composable
fun Talks() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(32.dp)
            .width(IntrinsicSize.Max),
    ) {
        Talk(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Kotlin + Power-Assert = ")
                    KodeeLoving(modifier = Modifier.requiredSize(50.dp).graphicsLayer { rotationY = 180f })
                }
            },
            videoId = "N8u-6d0iCiE",
            storyboard = KotlinPlusPowerAssertEqualsLove,
        )

        // TODO render a PDF as a Storyboard?
        Talk(
            title = { Text("Declarative Test Setup") },
            videoId = "_K25Z--4hxg",
        )

        // TODO render a PDF as a Storyboard?
        Talk(
            title = { Text("Elevated Gardening with the Kotlin Ecosystem") },
            videoId = "nVj9mbWz-Os",
        )
    }
}
