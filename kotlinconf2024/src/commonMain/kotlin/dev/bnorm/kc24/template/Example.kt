package dev.bnorm.kc24.template

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.kc24.elements.*
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideContent
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.SlideState
import kotlinx.collections.immutable.ImmutableList
import kotlin.jvm.JvmName

data class ExampleState(
    // Example
    val exampleIndex: Int = 0,

    // Gradle
    val showGradle: Boolean = false,
    val gradleIndex: Int = 0,

    // Output
    val showOutput: OutputState = OutputState.Hidden,
    val outputIndex: Int = 0,

    // Conclusions
    val conclusionIndex: Int = 0,
) {
    // TODO can Composables be supplied with each operation so the Example composable is just built automatically?
    interface Builder {
        fun updateExample()

        fun openGradle()
        fun updateGradle()
        fun closeGradle()

        fun openOutput()
        // scrollOutput()?
        fun updateOutput()
        fun minimizeOutput()
        fun closeOutput()

        fun addConclusion()
    }
}

fun ShowBuilder.slideForExample(builder: ExampleState.Builder.() -> Unit, content: SlideContent<ExampleState>) {
    val states = buildExampleStates(builder)
    val exit = states.last().copy(showGradle = false, showOutput = OutputState.Hidden, conclusionIndex = 0)
    slide(states = states.size) {
        val state = transition.createChildTransition {
            when (it) {
                SlideState.Entering -> states.first()
                SlideState.Exiting -> exit
                is SlideState.Index -> states[it.value]
            }
        }
        SlideScope(state).content()
    }
}

fun buildExampleStates(builder: ExampleState.Builder.() -> Unit): List<ExampleState> {
    return buildList {
        var last = ExampleState()

        add(last)
        object : ExampleState.Builder {
            override fun updateExample() {
                last = last.copy(exampleIndex = last.exampleIndex + 1)
                add(last)
            }

            override fun openGradle() {
                require(!last.showGradle)
                last = last.copy(showGradle = true)
                add(last)
            }

            override fun updateGradle() {
                last = last.copy(gradleIndex = last.gradleIndex + 1)
                add(last)
            }

            override fun closeGradle() {
                require(last.showGradle)
                last = last.copy(showGradle = false)
                add(last)
            }

            override fun openOutput() {
                require(last.showOutput != OutputState.Visible)
                last = last.copy(showOutput = OutputState.Visible)
                add(last)
            }

            override fun updateOutput() {
                last = last.copy(outputIndex = last.outputIndex + 1)
                add(last)
            }

            override fun minimizeOutput() {
                require(last.showOutput != OutputState.Minimized)
                last = last.copy(showOutput = OutputState.Minimized)
                add(last)
            }

            override fun closeOutput() {
                require(last.showOutput != OutputState.Hidden)
                last = last.copy(showOutput = OutputState.Hidden)
                add(last)
            }

            override fun addConclusion() {
                last = last.copy(conclusionIndex = last.conclusionIndex + 1)
                add(last)
            }
        }.builder()
    }
}

@Composable
@JvmName("ExampleGradleSequence")
fun SlideScope<ExampleState>.Example(
    exampleTextSequence: ImmutableList<AnnotatedString>,
    gradleTextSequence: ImmutableList<ImmutableList<AnnotatedString>>? = null,
    outputTextSequence: ImmutableList<ImmutableList<String>>? = null,
    conclusions: ImmutableList<Conclusion>? = null,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            val exampleText by transition.exampleTextDiff(exampleTextSequence)
            Example(exampleText)
            if (conclusions != null) {
                Conclusions(conclusions)
            }
        }

        if (outputTextSequence != null) {
            val outputText by transition.createChildTransition { it.outputIndex }.animateThrough(outputTextSequence)
            Output(outputText, Modifier.align(Alignment.BottomStart))
        }

        if (gradleTextSequence != null) {
            val gradleText by transition.createChildTransition { it.gradleIndex }.animateThrough(gradleTextSequence)
            Gradle(gradleText, Modifier.align(Alignment.TopEnd))
        }
    }
}

@Composable
fun Example(exampleText: AnnotatedString) {
    Box(modifier = Modifier.fillMaxWidth().padding(start = SLIDE_PADDING, top = SLIDE_PADDING)) {
        Text(exampleText, modifier = Modifier.horizontalScroll(rememberScrollState()))
    }
}

@Composable
private fun SlideScope<ExampleState>.Conclusions(conclusions: ImmutableList<Conclusion>) {
    Box(modifier = Modifier.padding(horizontal = SLIDE_PADDING)) {
        transition.createChildTransition { it.conclusionIndex }.ShowConclusions(conclusions)
    }
}

@Composable
private fun SlideScope<ExampleState>.Output(outputText: String, modifier: Modifier) {
    OutputText(
        text = outputText,
        state = transition.createChildTransition { it.showOutput },
        modifier = modifier
    )
}

@Composable
private fun SlideScope<ExampleState>.Gradle(
    gradleText: AnnotatedString,
    modifier: Modifier,
) {
    GradleFile(
        text = gradleText,
        visible = transition.createChildTransition { it.showGradle },
        modifier = modifier,
    )
}

@Composable
fun Transition<out ExampleState>.exampleTextDiff(
    values: ImmutableList<AnnotatedString>,
    transitionSpec: @Composable Transition.Segment<Int>.() -> FiniteAnimationSpec<Int> = {
        typingSpec(count = values.size - 1)
    },
    targetIndexByState: @Composable (state: Int) -> Int = {
        if (it > 0) values.lastIndex else 0
    },
): State<AnnotatedString> {
    val state = createChildTransition { it.exampleIndex }
    return state.animateList(
        values = values,
        transitionSpec = transitionSpec,
        targetIndexByState = targetIndexByState
    )
}
