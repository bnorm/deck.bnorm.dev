package dev.bnorm.kc25.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.bnorm.deck.story.generated.resources.Res
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

private var cachedComposition: LottieComposition? = null

@Composable
fun KodeeWave(
    transition: InfiniteTransition,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    var composition by remember {
        val state = mutableStateOf(cachedComposition)
        if (cachedComposition == null) {
            scope.launch(start = CoroutineStart.UNDISPATCHED) {
                val bytes = Res.readBytes("files/KodeeWave.json")
                cachedComposition = LottieComposition.parse(bytes.decodeToString())
                state.value = cachedComposition
            }
        }
        state
    }

    val c = composition ?: return

    // The animation duration is about 5 seconds.
    // Animate about every 30 seconds.
    val durationMillis = (c.durationFrames / c.frameRate * 1_000).toInt()
    val spec = infiniteRepeatable<Float>(
        animation = tween(
            durationMillis = durationMillis,
            delayMillis = durationMillis * 6,
            easing = LinearEasing
        ),
        repeatMode = RepeatMode.Restart,
    )

    LottieAnimation(c, transition, modifier, spec)
}

@Composable
private fun LottieAnimation(
    composition: LottieComposition,
    transition: InfiniteTransition,
    modifier: Modifier = Modifier,
    spec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
        animation = tween(
            durationMillis = (composition.durationFrames / composition.frameRate * 1_000).toInt(),
            easing = LinearEasing
        ),
        repeatMode = RepeatMode.Restart,
    ),
) {
    val progress = transition.animateFloat(0f, 1f, spec)
    Image(
        modifier = modifier,
        painter = rememberLottiePainter(
            composition = composition,
            progress = progress::value,
        ),
        contentDescription = ""
    )
}
