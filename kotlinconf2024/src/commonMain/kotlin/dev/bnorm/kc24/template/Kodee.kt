package dev.bnorm.kc24.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.image.Kodee
import dev.bnorm.kc24.image.kodee.Petite
import dev.bnorm.kc24.image.kodee.Sitting
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

interface KodeeScope {
    fun show(condition: () -> Boolean, content: @Composable () -> Unit)
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
    Image(
        imageVector = Kodee.Petite,
        contentDescription = "",
        modifier = Modifier.requiredSize(73.dp, 63.dp),
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun KodeeLoving(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(DrawableResource("kotlin_mascot/emoji/kodee-loving.png")),
        contentDescription = "",
        modifier = modifier,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun KodeeSurprised(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(DrawableResource("kotlin_mascot/emoji/kodee-surprised.png")),
        contentDescription = "",
        modifier = modifier,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun KodeeBrokenHearted(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(DrawableResource("kotlin_mascot/emoji/kodee-broken-hearted.png")),
        contentDescription = "",
        modifier = modifier,
    )
}

@Composable
fun KodeeSitting(modifier: Modifier) {
    Image(
        imageVector = Kodee.Sitting,
        contentDescription = "",
        modifier = modifier,
    )
}
