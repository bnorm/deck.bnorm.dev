package dev.bnorm.kc24.template

import androidx.compose.animation.core.Transition
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.examples.ExampleCarousel
import dev.bnorm.kc24.examples.ExampleState
import dev.bnorm.kc24.examples.buildExampleStates
import dev.bnorm.librettist.show.*

@ShowBuilderDsl
interface ExampleBuilder {
    @ShowBuilderDsl
    fun example(
        builder: ExampleState.Builder.() -> Unit,
        forward: Boolean = true,
        kodee: KodeeScope.(Transition<out ExampleState>) -> Unit = {},
        content: SlideContent<ExampleState>,
    )
}

@ShowBuilderDsl
fun ShowBuilder.examples(
    block: ExampleBuilder.() -> Unit,
) {
    val upstream = this

    var previousContent: SlideContent<ExampleState>? = null
    var previousExit: ExampleState? = null
    object : ExampleBuilder {
        override fun example(
            builder: ExampleState.Builder.() -> Unit,
            forward: Boolean,
            kodee: KodeeScope.(Transition<out ExampleState>) -> Unit,
            content: SlideContent<ExampleState>,
        ) {
            val pContent = previousContent
            val pExit = previousExit
            upstream.ExampleCarousel(
                forward = forward,
                start = {
                    if (pContent != null && pExit != null) {
                        val scope = SlideScope(pExit)
                        scope.pContent()
                    }
                },
                end = {
                    val scope = SlideScope(ExampleState(exampleIndex = 0))
                    scope.content()
                }
            )

            val states = buildExampleStates(builder)
            val exit = states.last().copy(showGradle = false, showOutput = OutputState.Hidden, conclusionIndex = 0)
            upstream.slide(states = states.size) {
                val scope = createChildScope {
                    when (it) {
                        SlideState.Entering -> states.first()
                        SlideState.Exiting -> exit
                        is SlideState.Index -> states[it.value]
                    }
                }
                TitleAndBody(
                    kodee = { kodee(scope.transition) },
                ) {
                    scope.content()
                }
            }

            previousContent = content
            previousExit = exit
        }
    }.block()
}
