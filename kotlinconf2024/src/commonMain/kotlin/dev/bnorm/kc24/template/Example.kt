package dev.bnorm.kc24.template

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.kc24.elements.*
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideContent
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.SlideState
import kotlinx.collections.immutable.ImmutableList
import kotlin.jvm.JvmName

data class ExampleState(
    // Gradle
    val showGradle: Boolean = false,
    val gradleIndex: Int = 0,

    // Output
    val showOutput: OutputState = OutputState.Hidden,
    val outputIndex: Int = 0,

    // Conclusions
    val conclusionIndex: Int = 0,
) {
    interface Builder {
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
    exampleText: AnnotatedString,
    gradleTextSequence: ImmutableList<ImmutableList<AnnotatedString>>?,
    outputTextSequence: ImmutableList<String>?,
    conclusions: ImmutableList<Conclusion>? = null,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Example(exampleText)
            if (conclusions != null) {
                Conclusions(conclusions)
            }
        }

        if (outputTextSequence != null) {
            val outputText by transition.outputTextDiff(outputTextSequence)
            Output(outputText, Modifier.align(Alignment.BottomStart))
        }

        if (gradleTextSequence != null) {
            val gradleText by transition.gradleTextDiff(gradleTextSequence)
            Gradle(gradleText, Modifier.align(Alignment.TopEnd))
        }
    }
}

@Composable
fun SlideScope<ExampleState>.Example(
    exampleText: AnnotatedString,
    gradleTextSequence: ImmutableList<AnnotatedString>?,
    outputText: String?,
    conclusions: ImmutableList<Conclusion>? = null,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Example(exampleText)
            if (conclusions != null) {
                Conclusions(conclusions)
            }
        }

        if (outputText != null) {
            Output(outputText, Modifier.align(Alignment.BottomStart))
        }

        if (gradleTextSequence != null) {
            val gradleText by transition.gradleTextDiff(gradleTextSequence)
            Gradle(gradleText, Modifier.align(Alignment.TopEnd))
        }
    }
}

@Composable
private fun Example(exampleText: AnnotatedString) {
    Box(modifier = Modifier.padding(start = SLIDE_PADDING, top = SLIDE_PADDING)) {
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
