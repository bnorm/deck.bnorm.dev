package dev.bnorm.kc25.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.buildable_repo_qr
import dev.bnorm.kc25.broadcast.LocalReactionListener
import dev.bnorm.kc25.broadcast.ReactionMessage
import dev.bnorm.kc25.components.KodeeWave
import dev.bnorm.storyboard.LocalSceneMode
import dev.bnorm.storyboard.SceneMode
import io.ktor.util.date.*
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

private class FloatingKodee(
    val timestamp: Long,
    val content: @Composable (Transition<Float>) -> Unit,
) {
    val state = MutableTransitionState(0f)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FloatingKodee) return false
        return timestamp == other.timestamp
    }

    override fun hashCode(): Int {
        return timestamp.hashCode()
    }
}

val LocalInfiniteTransition = compositionLocalOf<InfiniteTransition?> { null }

@Composable
fun QrCodeKodee() {
    Column {
        val infiniteTransition = LocalInfiniteTransition.current
        if (LocalSceneMode.current == SceneMode.Story && infiniteTransition != null) {
            // TODO animation blinks slightly when animating across scenes
            //  - this is probably a compottie issue and not some thing we can fix :(
            KodeeWave(
                infiniteTransition,
                Modifier.width(50.dp).wrapContentWidth(unbounded = true).width(100.dp).height(50.dp)
            )
        }

        Image(
            painter = painterResource(Res.drawable.buildable_repo_qr),
            contentDescription = "",
            modifier = Modifier.size(50.dp).background(Color.White),
        )
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun DefaultReactionKodee(modifier: Modifier = Modifier) {
//    var reaction by remember { mutableStateOf<ReactionMessage?>(null) }
//
//    Box(contentAlignment = Alignment.BottomEnd, modifier = modifier) {
//        FloatingReactions()
//
//        AnimateKodee {
//            default { QrCodeKodee() }
//
//            show(condition = { reaction != null }) {
//                when (val reaction = reaction) {
//                    null, is ReactionMessage.Ping -> {
//                        QrCodeKodee()
//                    }
//
//                    else -> {
//                        reaction.Image(Modifier.size(100.dp))
//                    }
//                }
//            }
//        }
//    }
}

@Composable
fun FloatingReactions() {
    // Do not render floating reactions in anything but story mode!
    if (LocalSceneMode.current != SceneMode.Story) return

    val queue = remember { mutableStateSetOf<FloatingKodee>() }
    val listener = LocalReactionListener.current
    LaunchedEffect(listener) {
        val reactions = listener?.listen() ?: return@LaunchedEffect
        reactions
            .filter {
                // Ignore pings.
                it !is ReactionMessage.Ping &&
                        // And should be sent within the last second.
                        it.timestamp > getTimeMillis() - 1_000
            }
            .collect { reaction ->
                when (reaction) {
                    is ReactionMessage.Ping -> Unit
                    else -> queue.add(FloatingKodee(reaction))
                }
            }
    }

    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxSize()) {
        for (kodee in queue) {
            key(kodee.timestamp) {
                LaunchedEffect(kodee.timestamp, kodee.state.currentState) {
                    if (kodee.state.currentState >= 1f) {
                        queue.remove(kodee)
                    } else {
                        kodee.state.targetState = 1f
                    }
                }
                kodee.content(rememberTransition(kodee.state, label = "kodee ${kodee.timestamp}"))
            }
        }
    }
}

private fun FloatingKodee(reaction: ReactionMessage): FloatingKodee {
    fun offsetSpec(): TweenSpec<Dp> = tween(durationMillis = 3000, easing = Ease)
    fun alphaSpec(): TweenSpec<Float> = tween(durationMillis = 3000, easing = EaseIn)

    return FloatingKodee(reaction.timestamp) { transition ->
        val random = remember { Random(reaction.timestamp) }
        val xTarget = remember { random.nextFloat() * 55f }

        val yOffset by transition.animateDp({ offsetSpec() }) { (-75).dp * it - 25.dp }
        val xOffset by transition.animateDp({ offsetSpec() }) { (-15).dp - xTarget.dp * it }
        val alpha by transition.animateFloat({ alphaSpec() }) { 1f - it }
        // TODO add a little wobble

        Box(Modifier.offset(y = yOffset, x = xOffset).alpha(alpha)) {
            reaction.Image(Modifier.size(20.dp))
        }
    }
}
