package dev.bnorm.kc24.examples

import androidx.compose.animation.core.Transition
import dev.bnorm.kc24.elements.OutputState
import dev.bnorm.kc24.template.KodeeScope
import dev.bnorm.kc24.template.TitleAndBody
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

    object : ExampleBuilder {
        private var previousContent: SlideContent<ExampleState>? = null
        private var previousExit: ExampleState? = null

        override fun example(
            builder: ExampleState.Builder.() -> Unit,
            forward: Boolean,
            kodee: KodeeScope.(Transition<out ExampleState>) -> Unit,
            content: SlideContent<ExampleState>,
        ) {
            val states = buildExampleStates(builder)
            val enter = states.first()
            val exit = states.last().copy(showGradle = false, showOutput = OutputState.Hidden, conclusionIndex = 0)

            val pContent = previousContent
            val pExit = previousExit
            previousContent = content
            previousExit = exit

            upstream.ExampleCarousel(
                forward = forward,
                start = {
                    if (pContent != null && pExit != null) {
                        SlideScope(pExit).pContent()
                    }
                },
                end = {
                    SlideScope(enter).content()
                }
            )

            upstream.slide(states = states.size) {
                val scope = createChildScope {
                    when (it) {
                        SlideState.Entering -> enter
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
        }
    }.block()
}
