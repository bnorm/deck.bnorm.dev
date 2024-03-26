package dev.bnorm.kc24.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.image.Kodee
import dev.bnorm.kc24.image.kodee.Petite
import dev.bnorm.kc24.image.kodee.Sitting
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

interface KodeeScope {
    fun show(condition: () -> Boolean, content: @Composable () -> Unit)

    fun <T> Transition<T>.at(state: T): Boolean = !isRunning && currentState == state

    fun <T> Transition<T>.between(a: T, b: T): Boolean {
        return at(a) || at(b) || targetState == a && currentState == b || targetState == b && currentState == a
    }
}

@Composable
fun AnimateKodee(
    builder: KodeeScope.() -> Unit
) {
    val icons = remember(builder) {
        buildList {
            object : KodeeScope {
                override fun show(condition: () -> Boolean, content: @Composable () -> Unit) {
                    add(condition to content)
                }

            }.builder()
        }
    }

    var somethingVisible = false
    for ((condition, content) in icons) {
        val itemVisible = !somethingVisible && condition()
        somethingVisible = somethingVisible || itemVisible

        AnimatedVisibility(visible = itemVisible, enter = fadeIn(), exit = fadeOut()) {
            content()
        }
    }

    AnimatedVisibility(
        visible = !somethingVisible,
        enter = fadeIn(), exit = fadeOut(),
    ) { DefaultCornerKodee() }
}

@Composable
fun DefaultCornerKodee() {
    KodeePetite(Modifier.requiredSize(100.dp))
}

@Composable
fun KodeeExcited(modifier: Modifier = Modifier) {
    Kodee("kotlin_mascot/emoji/kodee-excited.png", modifier)
}

@Composable
fun KodeeLoving(modifier: Modifier = Modifier) {
    Kodee("kotlin_mascot/emoji/kodee-loving.png", modifier)
}

@Composable
fun KodeeSurprised(modifier: Modifier = Modifier) {
    Kodee("kotlin_mascot/emoji/kodee-surprised.png", modifier)
}

@Composable
fun KodeeBrokenHearted(modifier: Modifier = Modifier) {
    Kodee("kotlin_mascot/emoji/kodee-broken-hearted.png", modifier)
}

@Composable
fun KodeeLost(modifier: Modifier = Modifier) {
    Kodee("kotlin_mascot/emoji/kodee-lost.png", modifier)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun Kodee(path: String, modifier: Modifier) {
    Image(
        painter = painterResource(DrawableResource(path)),
        contentDescription = "",
        modifier = modifier,
    )
}

@Composable
fun KodeePetite(modifier: Modifier = Modifier) {
    Kodee(Kodee.Petite, modifier)
}

@Composable
fun KodeeSitting(modifier: Modifier = Modifier) {
    Kodee(Kodee.Sitting, modifier)
}

@Composable
private fun Kodee(vector: ImageVector, modifier: Modifier) {
    Image(
        imageVector = vector,
        contentDescription = "",
        modifier = modifier,
    )
}
