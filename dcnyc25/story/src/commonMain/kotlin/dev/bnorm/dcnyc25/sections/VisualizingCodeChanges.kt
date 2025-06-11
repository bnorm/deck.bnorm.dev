package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.dcnyc25.template.Horizontal
import dev.bnorm.dcnyc25.template.Quarter
import dev.bnorm.dcnyc25.template.SceneHalfHeight
import dev.bnorm.dcnyc25.template.code1
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toState
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import me.saket.extendedspans.ExtendedSpans
import me.saket.extendedspans.RoundedCornerSpanPainter
import me.saket.extendedspans.drawBehind

fun StoryboardBuilder.VisualizingCodeChanges() {
    scene(
        stateCount = 6,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
//        enterTransition = enter(
//            start = SceneEnter(alignment = Alignment.CenterEnd),
//            end = SceneEnter(alignment = Alignment.BottomCenter),
//        ),
//        exitTransition = exit(
//            start = SceneExit(alignment = Alignment.CenterEnd),
//            end = SceneExit(alignment = Alignment.BottomCenter),
//        ),
    ) {
        val offset by transition.animateDp(transitionSpec = { tween(750, easing = EaseInOut) }) {
            SceneHalfHeight * when (it.toState()) {
                in 0..2 -> 0
                else -> 1
            }
        }

        val introState = transition.createChildTransition {
            it.toState()
        }

        val diffState = transition.createChildTransition {
            when ((it.toState() - 3).coerceAtLeast(0)) {
                0 -> CodeDiffState.NoDiff
                1 -> CodeDiffState.HumanDiff
                else -> CodeDiffState.ComputerDiff
            }
        }

        Column(
            Modifier
                .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                .offset(y = -offset)
        ) {
            TitleHeader()
            IntroTitle(introState)
            CodeDiffExample(diffState)
        }
    }
}

@Composable
private fun IntroTitle(transition: Transition<Int>) {
    Horizontal(MaterialTheme.colors.secondary) {
        val message = transition.createChildTransition {
            when (it) {
                0 -> buildAnnotatedString {
                    appendLine("How do you")
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("change code")
                    }
                    append("?")
                }

                1 -> buildAnnotatedString {
                    appendLine("How do you")
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        appendLine("visualize")
                    }
                    append("code change\u200bs?")
                }

                else -> buildAnnotatedString {
                    appendLine("How do\u200bes a")
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("computer")
                    }
                    appendLine(" visualize")
                    append("code change\u200bs?")
                }
            }
        }

        val style = MaterialTheme.typography.h2.copy(
            color = Color.White,
            fontWeight = FontWeight.ExtraLight
        )

        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier.padding(24.dp).fillMaxSize(),
        ) {
            ProvideTextStyle(style) {
                MagicText(message)
            }
        }
    }
}

private enum class CodeDiffState {
    NoDiff,
    HumanDiff,
    ComputerDiff,
}

@Composable
private fun CodeDiffExample(transition: Transition<CodeDiffState>) {
    Horizontal(MaterialTheme.colors.primary) {
        Row {
            Quarter(MaterialTheme.colors.primary) {
                CodeDiff(
                    transition = transition,
                    code = remember {
                        """
                           fun main() {
                               println("Hello, KotlinConf!")
                           }
                        """.trimIndent()
                            .highlight(INTELLIJ_LIGHT, language = Language.Kotlin)
                    },
                    ranges = remember {
                        persistentMapOf(
                            CodeDiffState.NoDiff to emptyList(),
                            CodeDiffState.HumanDiff to listOf(33..43),
                            CodeDiffState.ComputerDiff to listOf(33..34, 35..37, 38..40, 42..43),
                        )
                    },
                    highlightColor = Color.Red.copy(alpha = 0.5f)
                )
            }
            Quarter(MaterialTheme.colors.primary) {
                CodeDiff(
                    transition = transition,
                    code = remember {
                        """
                           fun main() {
                               println("Hello, droidcon NYC!")
                           }
                        """.trimIndent()
                            .highlight(INTELLIJ_LIGHT, language = Language.Kotlin)
                    },
                    ranges = remember {
                        persistentMapOf(
                            CodeDiffState.NoDiff to emptyList(),
                            CodeDiffState.HumanDiff to listOf(33..45),
                            CodeDiffState.ComputerDiff to listOf(33..35, 37..39, 41..45),
                        )
                    },
                    highlightColor = Color.Green.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun CodeDiff(
    transition: Transition<CodeDiffState>,
    code: AnnotatedString,
    ranges: ImmutableMap<CodeDiffState, List<IntRange>>,
    highlightColor: Color,
) {
    TextSurface {
        for ((state, ranges) in ranges) {
            val extendedSpans = remember {
                ExtendedSpans(RoundedCornerSpanPainter(topMargin = 0.sp, bottomMargin = 0.sp))
            }

            val alpha by transition.animateFloat(transitionSpec = { tween(500, easing = EaseInOut) }) {
                if (it != state) 0f else 1f
            }

            val text = remember(transition.currentState, code, ranges) {
                extendedSpans.extend(
                    buildAnnotatedString {
                        append(code)
                        for (range in ranges) {
                            addStyle(SpanStyle(background = highlightColor), range.start, range.endInclusive)
                        }
                    }
                )
            }

            Text(
                text = text,
                style = MaterialTheme.typography.code1,
                modifier = Modifier.padding(16.dp)
                    .alpha(alpha)
                    .drawBehind(extendedSpans),
                // TODO pdf export might require using a text measurer.
                onTextLayout = { extendedSpans.onTextLayout(it) }
            )
        }
    }
}

@Composable
fun TextSurface(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxSize().padding(32.dp)
    ) {
        content()
    }
}
