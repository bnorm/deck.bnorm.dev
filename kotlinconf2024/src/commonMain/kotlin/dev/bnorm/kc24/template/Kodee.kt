package dev.bnorm.kc24.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.image.Kodee
import dev.bnorm.kc24.image.kodee.Sitting
import dev.bnorm.kc24.kotlinconf2024.generated.resources.*
import dev.bnorm.librettist.show.SlideScope
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private object SharedKodeeKey

@Composable
fun SlideScope<*>.SharedKodee(content: @Composable () -> Unit) {
    with(sharedTransitionScope) {
        Box(
            Modifier.sharedElement(
                rememberSharedContentState(key = SharedKodeeKey),
                animatedVisibilityScope = animatedContentScope,
            ).fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            content()
        }
    }
}

interface KodeeScope {
    fun show(condition: () -> Boolean, content: @Composable () -> Unit)

    fun <T> Transition<out T>.both(condition: (T) -> Boolean, content: @Composable () -> Unit) {
        show(condition = { condition(currentState) && condition(targetState) }, content)
    }

    fun <T> Transition<out T>.either(condition: (T) -> Boolean, content: @Composable () -> Unit) {
        show(condition = { condition(currentState) || condition(targetState) }, content)
    }
}

@Composable
fun AnimateKodee(
    modifier: Modifier = Modifier,
    builder: KodeeScope.() -> Unit = {},
) {
    fun <T> spec(): FiniteAnimationSpec<T> = tween()

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

        AnimatedVisibility(visible = itemVisible, enter = fadeIn(spec()), exit = fadeOut(spec())) {
            content()
        }
    }

    Box(modifier) {
        AnimatedVisibility(
            visible = !somethingVisible,
            enter = fadeIn(spec()),
            exit = fadeOut(spec()),
        ) {
            DefaultCornerKodee()
        }
    }
}

@Composable
fun DefaultCornerKodee() {
    KodeePleased(Modifier.requiredSize(100.dp).graphicsLayer { rotationY = 180f })
}

@Composable
fun KodeeExcited(modifier: Modifier = Modifier) {
    Kodee(Res.drawable.kodee_excited, modifier)
}

@Composable
fun KodeeLoving(modifier: Modifier = Modifier) {
    Kodee(Res.drawable.kodee_loving, modifier)
}

@Composable
fun KodeeSad(modifier: Modifier = Modifier) {
    Kodee(Res.drawable.kodee_sad, modifier)
}

@Composable
fun KodeeSurprised(modifier: Modifier = Modifier) {
    Kodee(Res.drawable.kodee_surprised, modifier)
}

@Composable
fun KodeeBrokenHearted(modifier: Modifier = Modifier) {
    Kodee(Res.drawable.kodee_broken_hearted, modifier)
}

@Composable
fun KodeeLost(modifier: Modifier = Modifier) {
    Kodee(Res.drawable.kodee_lost, modifier)
}

@Composable
fun KodeePleased(modifier: Modifier = Modifier) {
    Kodee(Res.drawable.kodee_pleased, modifier)
}

@Composable
private fun Kodee(drawableResource: DrawableResource, modifier: Modifier) {
    Image(
        painter = painterResource(drawableResource),
        contentDescription = "",
        modifier = modifier,
    )
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
