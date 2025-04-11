package dev.bnorm.kc25.broadcast

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import dev.bnorm.deck.shared.KodeeElectrified
import dev.bnorm.deck.shared.KodeeExcited
import dev.bnorm.deck.shared.KodeeLost
import dev.bnorm.deck.shared.KodeeLoving
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ReactionMessage {
    abstract val timestamp: Long

    @Composable
    abstract fun Image(modifier: Modifier)

    @Serializable
    @SerialName("ping")
    data class Ping(override val timestamp: Long) : ReactionMessage() {
        @Composable
        override fun Image(modifier: Modifier) {
        }
    }

    @Serializable
    @SerialName("heart")
    data class Heart(override val timestamp: Long) : ReactionMessage() {
        @Composable
        override fun Image(modifier: Modifier) {
            KodeeLoving(modifier.graphicsLayer(rotationY = 180f))
        }
    }

    @Serializable
    @SerialName("excited")
    data class Excited(override val timestamp: Long) : ReactionMessage() {
        @Composable
        override fun Image(modifier: Modifier) {
            KodeeExcited(modifier)
        }
    }

    @Serializable
    @SerialName("electrified")
    data class Electrified(override val timestamp: Long) : ReactionMessage() {
        @Composable
        override fun Image(modifier: Modifier) {
            KodeeElectrified(modifier)
        }
    }

    @Serializable
    @SerialName("lost")
    data class Lost(override val timestamp: Long) : ReactionMessage() {
        @Composable
        override fun Image(modifier: Modifier) {
            KodeeLost(modifier.graphicsLayer(rotationY = 180f))
        }
    }
}
