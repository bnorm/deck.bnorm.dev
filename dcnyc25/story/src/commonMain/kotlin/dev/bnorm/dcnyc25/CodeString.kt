package dev.bnorm.dcnyc25

import androidx.compose.animation.core.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import dev.bnorm.dcnyc25.template.SceneHeight
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.highlight.CodeStyle
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.magic.DefaultFadeDurationMillis
import dev.bnorm.storyboard.text.magic.DefaultMoveDurationMillis
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags

class CodeString(
    initialText: String,
) {
    var text: AnnotatedString by mutableStateOf(initialText.highlight())
        private set

    fun update(newText: String) {
        text = newText.highlight()
    }
}

fun String.highlight(
    codeStyle: CodeStyle = INTELLIJ_LIGHT,
    scope: CodeScope = CodeScope.File,
    identifierStyle: (String) -> SpanStyle? = { _ -> null },
): AnnotatedString {
    return highlight(
        codeStyle = codeStyle,
        language = Language.Kotlin,
        scope = scope,
        identifierStyle = identifierStyle
    )
}

fun AnnotatedString.highlight(
    codeStyle: CodeStyle = INTELLIJ_LIGHT,
    scope: CodeScope = CodeScope.File,
    identifierStyle: (String) -> SpanStyle? = { _ -> null },
): AnnotatedString {
    val styled = text.highlight(
        codeStyle = codeStyle,
        language = Language.Kotlin,
        scope = scope,
        identifierStyle = identifierStyle
    )
    return buildAnnotatedString {
        append(this@highlight)
        for (range in styled.spanStyles) {
            addStyle(range.item, range.start, range.end)
        }
    }
}

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
            .padding(bottom = SceneHeight)
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
