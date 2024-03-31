package dev.bnorm.kc24.elements

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.kc24.sections.ExampleState
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.librettist.animation.animateList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs

@Composable
fun GradleFile(
    text: AnnotatedString,
    visible: Transition<Boolean>,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(Int.MAX_VALUE),
) {
    val outputOffset by visible.animateDp(transitionSpec = { defaultSpec() }) {
        if (it) 20.dp else 1520.dp
    }

    MacWindow(
        color = Color(0xFF313438),
        modifier = modifier.fillMaxHeight().requiredWidth(1520.dp).offset(x = outputOffset),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // TODO use icon from IntelliJ
                Image(
                    painter = painterResource(DrawableResource("gradle/ICON-GRADLE-ALT_MONO-REV.png")),
                    contentDescription = "",
                    modifier = Modifier.size(36.dp),
                )
                Spacer(Modifier.width(16.dp))
                Text(text = "build.gradle.kts", fontSize = 28.sp, lineHeight = 28.sp)
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colors.surface)
                .padding(SLIDE_PADDING)
                .padding(bottom = 100.dp)
        ) {
            Text(text, modifier = Modifier)
        }
    }
}

@Composable
fun Transition<ExampleState>.gradleTextDiff(
    values: ImmutableList<AnnotatedString>,
    transitionSpec: @Composable Transition.Segment<Int>.() -> FiniteAnimationSpec<Int> = {
        typingSpec(count = values.size - 1)
    },
    targetIndexByState: @Composable (state: Int) -> Int = {
        if (it > 0) values.lastIndex else 0
    },
): State<AnnotatedString> {
    val state = createChildTransition { it.gradleIndex }
    return state.animateList(
        values = values,
        transitionSpec = transitionSpec,
        targetIndexByState = targetIndexByState
    )
}

@Composable
fun Transition<ExampleState>.gradleTextDiff(
    sequence: ImmutableList<ImmutableList<AnnotatedString>>,
): State<AnnotatedString> {
    val values = remember(sequence) { sequence.flatten().toImmutableList() }
    val mapping = remember(sequence) {
        val mapping = mutableMapOf<Int, Int>()
        mapping[0] = 0

        var size = 0
        for ((index, value) in sequence.withIndex()) {
            size += value.size
            mapping[index + 1] = size - 1
        }

        mapping
    }
    return gradleTextDiff(
        values = values,
        transitionSpec = {
            typingSpec(count = abs(mapping.getValue(targetState) - mapping.getValue(initialState)))
        },
        targetIndexByState = { mapping.getValue(it) },
    )
}
