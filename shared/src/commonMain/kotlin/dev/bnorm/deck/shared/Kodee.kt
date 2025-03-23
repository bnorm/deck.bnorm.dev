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
import dev.bnorm.deck.shared.image.kodee.*
import dev.bnorm.storyboard.core.SceneScope
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private object SharedKodeeKey

@Composable
fun SceneScope<*>.SharedKodee(content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .sharedElement(
                rememberSharedContentState(SharedKodeeKey),
                animatedVisibilityScope = this,
            )
            .fillMaxSize().padding(8.dp),
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

    val conditions = remember(builder) {
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

    val icons = conditions.groupBy(keySelector = { it.second }, valueTransform = { it.first })
        .mapValues { (_, conditions) -> { conditions.any { it.invoke() } } }

    // TODO Use AnimatedContent?

    var somethingVisible = false
    for ((content, condition) in icons) {
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
fun KodeeElectrified(modifier: Modifier = Modifier) {
    Kodee(Res.drawable.kodee_electrified, modifier)
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
fun KodeeGreeting(modifier: Modifier = Modifier) {
    Kodee(Kodee.Greeting, modifier)
}


@Composable
fun KodeeInLove(modifier: Modifier = Modifier) {
    Kodee(Kodee.InLove, modifier)
}


@Composable
fun KodeeJumping(modifier: Modifier = Modifier) {
    Kodee(Kodee.Jumping, modifier)
}


@Composable
fun KodeeJumpingCopy(modifier: Modifier = Modifier) {
    Kodee(Kodee.JumpingCopy, modifier)
}


@Composable
fun KodeeNaughty(modifier: Modifier = Modifier) {
    Kodee(Kodee.Naughty, modifier)
}


@Composable
fun KodeePetite(modifier: Modifier = Modifier) {
    Kodee(Kodee.Petite, modifier)
}


@Composable
fun KodeeRegular(modifier: Modifier = Modifier) {
    Kodee(Kodee.Regular, modifier)
}


@Composable
fun KodeeSharing(modifier: Modifier = Modifier) {
    Kodee(Kodee.Sharing, modifier)
}


@Composable
fun KodeeSitting(modifier: Modifier = Modifier) {
    Kodee(Kodee.Sitting, modifier)
}


@Composable
fun KodeeSmall(modifier: Modifier = Modifier) {
    Kodee(Kodee.Small, modifier)
}


@Composable
fun KodeeWaving(modifier: Modifier = Modifier) {
    Kodee(Kodee.Waving, modifier)
}


@Composable
fun KodeeWinter(modifier: Modifier = Modifier) {
    Kodee(Kodee.Winter, modifier)
}

@Composable
private fun Kodee(vector: ImageVector, modifier: Modifier) {
    Image(
        imageVector = vector,
        contentDescription = "",
        modifier = modifier,
    )
}
