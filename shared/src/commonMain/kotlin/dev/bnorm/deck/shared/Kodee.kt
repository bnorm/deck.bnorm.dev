package dev.bnorm.deck.shared

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.generated.resources.*
import dev.bnorm.deck.shared.image.Kodee
import dev.bnorm.deck.shared.image.kodee.Sitting
import dev.bnorm.storyboard.core.SlideScope
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private object SharedKodeeKey

@Composable
fun SlideScope<*>.SharedKodee(content: @Composable () -> Unit) {
    val sharedElement = with(sharedTransitionScope) {
        Modifier.sharedElement(
            rememberSharedContentState(key = SharedKodeeKey),
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = sharedElement.fillMaxSize().padding(8.dp),
    ) {
        content()
    }
}

interface KodeeScope {
    fun show(condition: () -> Boolean, content: @Composable () -> Unit)

    fun default(content: @Composable () -> Unit)

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
        var default = @Composable { DefaultCornerKodee() }
        buildList {
            object : KodeeScope {
                override fun show(condition: () -> Boolean, content: @Composable () -> Unit) {
                    add(condition to content)
                }

                override fun default(content: @Composable () -> Unit) {
                    default = content
                }

            }.builder()

            add({ true } to default)
        }
    }

    // TODO Use AnimatedContent?

    var somethingVisible = false
    for ((condition, content) in icons) {
        val itemVisible = !somethingVisible && condition()
        somethingVisible = somethingVisible || itemVisible

        AnimatedVisibility(visible = itemVisible, modifier = modifier, enter = fadeIn(spec()), exit = fadeOut(spec())) {
            content()
        }
    }
}

@Composable
fun DefaultCornerKodee(modifier: Modifier = Modifier) {
    KodeePleased(modifier.graphicsLayer { rotationY = 180f })
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