package dev.bnorm.kc24.examples

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.kc24.elements.*
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.storyboard.core.*
import kotlinx.collections.immutable.ImmutableList
import kotlin.jvm.JvmName
import kotlin.time.Duration.Companion.milliseconds

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

val EnterForward: (AdvanceDirection) -> EnterTransition = { direction ->
    slideInHorizontally(defaultSpec(750.milliseconds)) {
        if (direction == AdvanceDirection.Forward) it else -it
    }
}

val ExitForward: (AdvanceDirection) -> ExitTransition = { direction ->
    slideOutHorizontally(defaultSpec(750.milliseconds)) {
        if (direction == AdvanceDirection.Forward) -it else it
    }
}

fun StoryboardBuilder.slideForExample(
    builder: ExampleState.Builder.() -> Unit,
    enterTransition: (AdvanceDirection) -> EnterTransition = { EnterTransition.None },
    exitTransition: (AdvanceDirection) -> ExitTransition = { ExitTransition.None },
    content: @Composable ExampleScope.() -> Unit,
) {
    val states = buildExampleStates(builder)
    val exit = states.last().copy(showGradle = false, showOutput = OutputState.Hidden, conclusionIndex = 0)
    slide(stateCount = states.size, enterTransition, exitTransition) {
        val slideScope = this@slide
        val exampleState = state.createChildTransition {
            when (it) {
                SlideState.Start -> states.first()
                SlideState.End -> exit
                is SlideState.Value -> states[it.value]
            }
        }

        val scope = remember(slideScope, exampleState) {
            object : ExampleScope {
                override val slideScope: SlideScope<*> get() = slideScope
                override val transition: Transition<out ExampleState> get() = exampleState
            }
        }

        scope.content()
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
fun ExampleScope.Example(
    exampleText: AnnotatedString,
    gradleText: AnnotatedString? = null,
    outputText: String,
    conclusions: ImmutableList<Conclusion>? = null,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Example(exampleText)

            if (conclusions != null) {
                Conclusions(conclusions)
            }
        }

        Output(outputText, Modifier.align(Alignment.BottomStart))

        if (gradleText != null) {
            Gradle(gradleText, Modifier.align(Alignment.TopEnd))
        }
    }
}

@Composable
@JvmName("ExampleGradleSequence")
fun ExampleScope.Example(
    exampleTextSequence: ImmutableList<AnnotatedString>,
    gradleTextSequence: ImmutableList<ImmutableList<AnnotatedString>>? = null,
    outputTextSequence: ImmutableList<ImmutableList<String>>? = null,
    conclusions: ImmutableList<Conclusion>? = null,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (exampleTextSequence.size == 1) {
                Example(exampleTextSequence[0])
            } else {
                val exampleText by transition.exampleTextDiff(exampleTextSequence)
                Example(exampleText)
            }

            if (conclusions != null) {
                Conclusions(conclusions)
            }
        }

        if (outputTextSequence != null) {
            val outputText by transition.createChildTransition { it.outputIndex }
                .animateThrough(outputTextSequence, transitionSpec = { typingSpec(duration = 1000.milliseconds) })
            Output(outputText, Modifier.align(Alignment.BottomStart))
        }

        if (gradleTextSequence != null) {
            val gradleText by transition.createChildTransition { it.gradleIndex }
                .animateThrough(gradleTextSequence)
            Gradle(gradleText, Modifier.align(Alignment.TopEnd))
        }
    }
}

@Composable
fun Example(exampleText: AnnotatedString) {
    Box(modifier = Modifier.fillMaxWidth().padding(start = SLIDE_PADDING, top = SLIDE_PADDING)) {
        ProvideTextStyle(MaterialTheme.typography.body2) {
            Text(exampleText, modifier = Modifier.horizontalScroll(rememberScrollState()))
        }
    }
}

@Composable
private fun ExampleScope.Conclusions(conclusions: ImmutableList<Conclusion>) {
    Box(modifier = Modifier.padding(horizontal = SLIDE_PADDING)) {
        ProvideTextStyle(MaterialTheme.typography.h6) {
            transition.createChildTransition { it.conclusionIndex }.ShowConclusions(conclusions)
        }
    }
}

@Composable
fun ExampleScope.Output(outputText: String, modifier: Modifier) {
    OutputText(
        text = outputText,
        state = transition.createChildTransition { it.showOutput },
        modifier = modifier
    )
}

@Composable
fun ExampleScope.Gradle(
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
