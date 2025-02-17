package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bnorm.evolved.sections.intro.BULLET_1
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.toCode
import dev.bnorm.storyboard.core.SlideScope
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.enter
import dev.bnorm.storyboard.easel.exit
import dev.bnorm.storyboard.easel.template.RevealEach

val moveDuration = DefaultDurationMillis
val fadeDuration = moveDuration / 2

fun StoryboardBuilder.NewApi() {
    // TODO don't show the details of each class until after the examples?

    OverviewSlide(introduction = true)

    DetailSlide(
        title = { SharedExplainCall() },
    ) {
        Text(
            text = """
                @Target(AnnotationTarget.FUNCTION)
                annotation class ExplainCall {
                    companion object {
                        val explanation: CallExplanation?
                            get() = throw NotImplementedError("...")
                    }
                }
            """.trimIndent().toCode(),
        )
    }

    OverviewSlide(introduction = false)

    DetailSlide(
        title = { SharedCallExplanation() },
    ) {
        Text(
            text = """
                class CallExplanation(
                    val offset: Int,
                    val source: String,
                    val dispatchReceiver: Receiver?,
                    val contextArguments: Map<String, ValueArgument>,
                    val extensionReceiver: Receiver?,
                    val valueArguments: Map<String, ValueArgument>,
                ) {
                    abstract class Argument() {
                        abstract val startOffset: Int
                        abstract val endOffset: Int
                        abstract val expressions: List<Expression>
                    }
                
                    class ValueArgument(
                        override val startOffset: Int,
                        override val endOffset: Int,
                        override val expressions: List<Expression>,
                    ) : Argument()
                
                    class Receiver(
                        override val startOffset: Int,
                        override val endOffset: Int,
                        override val expressions: List<Expression>,
                        val isImplicit: Boolean,
                    ) : Argument()
                    
                    val expressions: List<Expression>
                        get() = buildList {
                            if (dispatchReceiver != null) {
                                addAll(dispatchReceiver.expressions)
                            }
                            for (valueArgument in contextArguments.values) {
                                addAll(valueArgument.expressions)
                            }
                            if (extensionReceiver != null) {
                                addAll(extensionReceiver.expressions)
                            }
                            for (valueArgument in valueArguments.values) {
                                addAll(valueArgument.expressions)
                            }
                        }
                }
                
                
                
            """.trimIndent().toCode(),
        )
    }

    OverviewSlide(introduction = false)

    DetailSlide(
        title = { SharedExpression() },
    ) {
        Text(
            text = """
                abstract class Expression {
                    abstract val startOffset: Int
                    abstract val endOffset: Int
                    abstract val displayOffset: Int
                    abstract val value: Any?
                }
                
                class ValueExpression(
                    override val startOffset: Int,
                    override val endOffset: Int,
                    override val displayOffset: Int,
                    override val value: Any?,
                ) : Expression()
                
                class EqualityExpression(
                    override val startOffset: Int,
                    override val endOffset: Int,
                    override val displayOffset: Int,
                    override val value: Any?,
                    val lhs: Any?,
                    val rhs: Any?,
                ) : Expression()
                
                
                
                
                
                
                
            """.trimIndent().toCode(),
        )
    }

    OverviewSlide(introduction = false, final = true)
}

private fun StoryboardBuilder.OverviewSlide(introduction: Boolean, final: Boolean = false) {
    if (introduction) {
        slide(stateCount = 4) {
            HeaderAndBody(
                modifier = Modifier.animateEnterExit(
                    enter = enter(
                        start = { EnterTransition.None },
                        end = { fadeIn(tween(fadeDuration, delayMillis = moveDuration + fadeDuration)) },
                    ),
                    exit = fadeOut(tween(fadeDuration)),
                )
            ) {
                ProvideTextStyle(MaterialTheme.typography.h4) {
                    Text("What's new?")
                }
                ProvideTextStyle(MaterialTheme.typography.h5) {
                    RevealEach(state.createChildTransition { it.toState() - 1 }) {
                        item { Row { Text("$BULLET_1 New annotation: "); SharedExplainCall() } }
                        item { Row { Text("$BULLET_1 New class: "); SharedCallExplanation() } }
                        item { Row { Text("$BULLET_1 New base class: "); SharedExpression() } }
                    }
                }
            }
        }
    } else if (final) {
        slide(stateCount = 1) {
            HeaderAndBody(
                modifier = Modifier.animateEnterExit(
                    enter = enter(start = { fadeIn(tween(fadeDuration, delayMillis = moveDuration + fadeDuration)) }),
                    exit = exit(start = { fadeOut(tween(fadeDuration)) }),
                )
            ) {
                ProvideTextStyle(MaterialTheme.typography.h4) {
                    Text("What's new?")
                }
                ProvideTextStyle(MaterialTheme.typography.h5) {
                    Row { Text("$BULLET_1 New annotation: "); SharedExplainCall() }
                    Row { Text("$BULLET_1 New class: "); SharedCallExplanation() }
                    Row { Text("$BULLET_1 New base class: "); SharedExpression() }
                }
            }
        }
    } else {
        slide(stateCount = 1) {
            HeaderAndBody(
                modifier = Modifier.animateEnterExit(
                    enter = fadeIn(tween(fadeDuration, delayMillis = moveDuration + fadeDuration)),
                    exit = fadeOut(tween(fadeDuration)),
                )
            ) {
                ProvideTextStyle(MaterialTheme.typography.h4) {
                    Text("What's new?")
                }
                ProvideTextStyle(MaterialTheme.typography.h5) {
                    Row { Text("$BULLET_1 New annotation: "); SharedExplainCall() }
                    Row { Text("$BULLET_1 New class: "); SharedCallExplanation() }
                    Row { Text("$BULLET_1 New base class: "); SharedExpression() }
                }
            }
        }
    }
}

private fun StoryboardBuilder.DetailSlide(
    title: @Composable SlideScope<Int>.() -> Unit,
    content: @Composable SlideScope<Int>.() -> Unit,
) {
    slide(stateCount = 1) {
        HeaderAndBody(
            modifier = Modifier.animateEnterExit(
                enter = fadeIn(tween(fadeDuration, delayMillis = moveDuration + fadeDuration)),
                exit = fadeOut(tween(fadeDuration)),
            )
        ) {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                title()
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize()) {
                content()
            }
        }
    }

}

@Composable
private fun SlideScope<Int>.SharedExplainCall() {
    Text(
        text = "@ExplainCall".toCode(),
        modifier = Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(key = "explain-call"),
            animatedVisibilityScope = this,
            enter = fadeIn(tween(moveDuration, delayMillis = fadeDuration)),
            exit = fadeOut(tween(moveDuration, delayMillis = fadeDuration)),
            boundsTransform = { _, _ ->
                tween(moveDuration, delayMillis = fadeDuration)
            },
        ),
    )
}

@Composable
private fun SlideScope<Int>.SharedCallExplanation() {
    Text(
        text = "CallExplanation".toCode(),
        modifier = Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(key = "call-explanation"),
            animatedVisibilityScope = this,
            enter = fadeIn(tween(moveDuration, delayMillis = fadeDuration)),
            exit = fadeOut(tween(moveDuration, delayMillis = fadeDuration)),
            boundsTransform = { _, _ ->
                tween(moveDuration, delayMillis = fadeDuration)
            },
        ),
    )
}

@Composable
private fun SlideScope<Int>.SharedExpression() {
    Text(
        text = "Expression".toCode(),
        modifier = Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(key = "expression"),
            animatedVisibilityScope = this,
            enter = fadeIn(tween(moveDuration, delayMillis = fadeDuration)),
            exit = fadeOut(tween(moveDuration, delayMillis = fadeDuration)),
            boundsTransform = { _, _ ->
                tween(moveDuration, delayMillis = fadeDuration)
            },
        ),
    )
}
