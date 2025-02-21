package dev.bnorm.evolved.template.code

import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import dev.bnorm.storyboard.text.highlight.Highlighting

fun <T> T.twice(): Pair<T, T> = this to this

@Composable
fun Transition<Int>.MagicCode(
    transitions: List<Pair<String, String>>,
    moveDuration: Int = DefaultDurationMillis,
    fadeDuration: Int = moveDuration / 2,
    identifierType: (Highlighting, String) -> SpanStyle? = { _, _ -> null },
) {
    val currentState = currentState
    val targetState = targetState
    key(currentState, targetState) {
        SharedTransitionLayout {
            AnimatedContent(
                modifier = Modifier,
                transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                contentAlignment = Alignment.TopStart,
                contentKey = { it },
            ) { renderState ->
                @Composable
                fun Token.toModifier(): Modifier {
                    return when {
                        // Text is completely different and should fade in and out.
                        key == null -> Modifier.animateEnterExit(
                            enter = fadeIn(tween(fadeDuration, delayMillis = moveDuration + fadeDuration)),
                            exit = fadeOut(tween(fadeDuration)),
                        )

                        //                        // Text content is the same, but the styling may be different,
                        //                        // so move and cross-fade.
                        //                        crossFade -> Modifier.sharedBounds(
                        //                            rememberSharedContentState(key),
                        //                            animatedVisibilityScope = this@AnimatedContent,
                        //                            enter = fadeIn(tween(moveDuration, delayMillis = fadeDuration)),
                        //                            exit = fadeOut(tween(moveDuration, delayMillis = fadeDuration)),
                        //                            boundsTransform = { _, _ ->
                        //                                tween(moveDuration, delayMillis = fadeDuration)
                        //                            },
                        //                        )

                        // Text and styling are the same, so only move.
                        else -> Modifier.sharedElement(
                            rememberSharedContentState(key),
                            animatedVisibilityScope = this@AnimatedContent,
                            boundsTransform = { _, _ ->
                                tween(moveDuration, delayMillis = fadeDuration)
                            },
                        )
                    }
                }

                @Composable
                fun render(tokens: List<Token?>) {
                    Column {
                        val iterator = tokens.iterator()
                        while (iterator.hasNext()) {
                            Row {
                                Text("")
                                while (iterator.hasNext()) {
                                    val sharedText = iterator.next() ?: break
                                    Text(
                                        sharedText.text,
                                        sharedText.toModifier().alignByBaseline(),
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }

                @Composable
                fun render(currentState: Int, targetState: Int, renderState: Int) {
                    if (currentState > targetState) {
                        render(targetState, currentState, renderState)
                        return
                    } else if (currentState == targetState) {
                        if (currentState < 0) {
                            val state = transitions.first().first
                            render(state.toCodeTokens(identifierType))
                        } else if (currentState >= transitions.size) {
                            val state = transitions.last().second
                            render(state.toCodeTokens(identifierType))
                        } else {
                            val state = transitions[currentState].first
                            render(state.toCodeTokens(identifierType))
                        }
                    } else {
                        if (currentState < 0) {
                            val state = transitions.first().first
                            render(state.toCodeTokens(identifierType))
                        } else if (currentState >= transitions.size) {
                            val state = transitions.last().second
                            render(state.toCodeTokens(identifierType))
                        } else {
                            val (before, after) = transitions[currentState]
                            if (renderState == currentState) {
                                render(before.toCodeTokens(identifierType))
                            } else {
                                render(after.toCodeTokens(identifierType))
                            }
                        }
                    }
                }

                render(currentState, targetState, renderState)
            }
        }
    }
}

@Composable
fun <S> Transition<S>.AnimatedContent(
    modifier: Modifier = Modifier,
    transitionSpec: AnimatedContentTransitionScope<S>.() -> ContentTransform = {
        (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
            .togetherWith(fadeOut(animationSpec = tween(90)))
    },
    contentAlignment: Alignment = Alignment.TopStart,
    contentKey: (targetState: S) -> Any? = { it },
    content: @Composable() AnimatedContentScope.(currentState: S, targetState: S, renderState: S) -> Unit,
) {
    val currentState = currentState
    val targetState = targetState
    key(currentState, targetState) {
        SharedTransitionLayout {
            AnimatedContent(
                modifier = modifier,
                transitionSpec = transitionSpec,
                contentAlignment = contentAlignment,
                contentKey = contentKey,
            ) {
                content(currentState, targetState, it)
            }
        }
    }
}
