package dev.bnorm.deck.shared.code

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import dev.bnorm.storyboard.easel.LocalStoryboard
import dev.bnorm.storyboard.text.magic.DefaultFadeDurationMillis
import dev.bnorm.storyboard.text.magic.DefaultMoveDurationMillis
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags

@Composable
fun MagicCodeSample(
    sample: Transition<CodeSample>,
    modifier: Modifier = Modifier,
) {
    val state = rememberScrollState()
    sample.animateScroll(state)

    Box(
        // Scroll should come before any padding, so it is not clipped.
        Modifier.verticalScroll(state, enabled = false)
            .then(modifier)
            // Allow scrolling to the very bottom.
            .padding(bottom = LocalStoryboard.current!!.format.run { with(density) { size.height.toDp() } })
    ) {
        MagicText(sample.createChildTransition { it.string.splitByTags() })
    }
}

@Composable
private fun Transition<CodeSample>.animateScroll(
    verticalScrollState: ScrollState,
    style: TextStyle = LocalTextStyle.current,
    transitionSpec: @Composable Transition.Segment<CodeSample>.() -> FiniteAnimationSpec<Float> = {
        tween(DefaultMoveDurationMillis, delayMillis = DefaultFadeDurationMillis, easing = EaseInOut)
    },
    label: String = "ScrollAnimation",
) {
    val lineHeight = with(LocalDensity.current) { style.lineHeight.toPx() }
    val scrollPosition by animateFloat(transitionSpec, label) { it.scroll * lineHeight }
    verticalScrollState.dispatchRawDelta(scrollPosition - verticalScrollState.value)
}
