package dev.bnorm.kc24

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.bnorm.deck.kotlinconf2024.generated.resources.Res
import dev.bnorm.deck.kotlinconf2024.generated.resources.closing_background_badge
import dev.bnorm.deck.kotlinconf2024.generated.resources.closing_background_phone
import dev.bnorm.deck.kotlinconf2024.generated.resources.opening_background
import dev.bnorm.deck.shared.KodeeLoving
import dev.bnorm.kc24.elements.*
import dev.bnorm.kc24.examples.*
import dev.bnorm.kc24.sections.Future
import dev.bnorm.kc24.template.SLIDE_CONTENT_SPACING
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleSlide
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.text.thenLineEndDiff
import dev.bnorm.storyboard.core.*
import dev.bnorm.storyboard.easel.section
import dev.bnorm.storyboard.text.highlight.Highlighting
import org.jetbrains.compose.resources.painterResource

val KotlinPlusPowerAssertEqualsLove: Storyboard by lazy {
    Storyboard.build(
        size = Storyboard.DEFAULT_SIZE * 2,
        decorator = { content ->
            Highlighting(Theme.codeStyle) {
                MaterialTheme(colors = Theme.dark, typography = Theme.typography) {
                    content()
                }
            }
        },
    ) {
        KotlinPlusPowerAssertEqualsLove()
    }
}

private fun StoryboardBuilder.KotlinPlusPowerAssertEqualsLove() {
    val section1 = "Crafting an Assertion"
    val section2 = "Why Power-Assert?"
    val section3 = "A Look at the Future"

    slide { Title() }

    section(title = section1) {
        SectionHeader(animateToBody = true)
        BasicAssertTrueExample()
        ExampleTransition {
            val start = BasicAssertTrueCode
            val end = BasicAssertEqualsCode
            remember { startAnimation(start).thenLineEndDiff(end).toList() }
        }
        BasicAssertEqualsExample()
        ExampleTransition {
            val start = BasicAssertEqualsCode
            val end = AssertEqualsMessageCode
            remember { startAnimation(start).thenLineEndDiff(end).toList() }
        }
        AssertEqualsMessageExample()
        ExampleTransition {
            val start = AssertEqualsMessageCode
            val end = AssertKCode
            remember { startAnimation(start).thenLineEndDiff(end).toList() }
        }
        AssertKExample()
        SectionHeader(animateFromBody = true)
    }

    // TODO transition from bike-shedding to a new power-assert library seems counter productive

    SectionChange(section1, section2)

    section(title = section2) {
        SectionHeader(animateToBody = true)
        SimpleAssertExample()
        ComplexExpressionsExample()
        AssertTrueSmartcastExample()
        RequireExample()
        AssertEqualsAndNotNullExample()
        // TODO examples
        //  - at this point we could compare assertTrue/assertEquals/assertNotNull as the primary toolbox

        // TODO summary slide before going into the next example?
        SoftAssertExample()
        SectionHeader(animateFromBody = true)
    }

    SectionChange(section2, section3)

    section(title = section3) {
        SectionHeader(animateToBody = true)
        Future()
    }

    slide { Summary(transition) }
}

private fun StoryboardBuilder.SectionChange(previousTitle: String, nextTitle: String) {
    slideForTransition {
        SectionHeader(showAsBody = updateTransition(false)) {
            val values = remember(previousTitle, nextTitle) {
                startAnimation(previousTitle).thenLineEndDiff(nextTitle).toList()
            }
            val text by transition.animateList(values, transitionSpec = { typingSpec(count = values.size) }) {
                if (it == SlideState.End) values.lastIndex else 0
            }
            Text(text)
        }
    }
}

@Composable
fun Title() {
    TitleSlide {
        Box(Modifier.fillMaxSize()) {
            // TODO add some animation?
            //  - make image slide to the left when exiting?
            //  - drop title off the bottom
            //  - how does the next slide appear?
            Image(
                painter = painterResource(Res.drawable.opening_background),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )

            Box(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                Column(Modifier.fillMaxWidth().align(Alignment.BottomStart), horizontalAlignment = Alignment.Start) {
                    ProvideTextStyle(MaterialTheme.typography.h1) {
                        Text("Kotlin")
                        Text(" + Power-Assert")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(" = ")
                            KodeeLoving(modifier = Modifier.requiredSize(125.dp).graphicsLayer { rotationY = 180f })
                        }
                    }
                    Spacer(Modifier.size(32.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        JetBrainsEmployee(
                            name = "Brian Norman",
                            title = "Kotlin Compiler Developer",
                            modifier = Modifier.align(Alignment.BottomStart),
                        )
                        Mastodon(
                            username = "bnorm@kotlin.social",
                            modifier = Modifier.align(Alignment.BottomEnd),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Summary(transition: Transition<out SlideState<*>>) {
    val state = transition.createChildTransition { it != SlideState.Start }
    TitleSlide {
        Box(Modifier.fillMaxSize()) {
            SummaryBackground(state)

            Box(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                Column {
                    Spacer(Modifier.size(SLIDE_CONTENT_SPACING))
                    Text("", style = MaterialTheme.typography.h3)
                    Spacer(Modifier.size(SLIDE_CONTENT_SPACING))
                    Spacer(Modifier.size(4.dp))

                    ProvideTextStyle(MaterialTheme.typography.body1) {
                        Text(buildAnnotatedString {
                            append("Docs: ")
                            appendLink("kotl.in/power-assert", "https://kotl.in/power-assert")
                        })
                        Spacer(Modifier.size(SLIDE_CONTENT_SPACING))
                        Text(buildAnnotatedString {
                            append("Slack: ")
                            appendLink("#power-assert", "https://kotlinlang.slack.com/archives/C06V6SFE71D")
                            append(" (KotlinLang)")
                        })
                        Spacer(Modifier.size(SLIDE_CONTENT_SPACING))
                        Text(buildAnnotatedString {
                            append("Slides: ")
                            appendLink("deck.bnorm.dev/kotlinconf2024", "https://deck.bnorm.dev/kotlinconf2024")
                        })
                    }
                }

                state.AnimatedVisibility(
                    enter = fadeIn(defaultSpec()),
                    exit = fadeOut(defaultSpec()),
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Column {
                        Mastodon(username = "bnorm@kotlin.social")
                        Spacer(Modifier.size(SLIDE_CONTENT_SPACING))
                        Text(
                            text = "Thank you,\nand don’t\nforget to vote!",
                            style = MaterialTheme.typography.h3,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryBackground(state: Transition<Boolean>) {
    Box(Modifier.fillMaxSize()) {
        DrawArc(state)
        Row(Modifier.align(Alignment.TopEnd)) {
            state.AnimatedVisibility(
                enter = slideInVertically(defaultSpec()) { -it },
                exit = slideOutVertically(defaultSpec()) { -it },
            ) {
                Image(
                    painter = painterResource(Res.drawable.closing_background_badge),
                    contentDescription = "",
                    modifier = Modifier.size((869 * 1920 / 3840f).dp, (1627 * 1080 / 2160f).dp),
                )
            }
            state.AnimatedVisibility(
                enter = slideInHorizontally(defaultSpec()) { it },
                exit = slideOutHorizontally(defaultSpec()) { it },
            ) {
                Image(
                    painter = painterResource(Res.drawable.closing_background_phone),
                    contentDescription = "",
                    modifier = Modifier.size((993 * 1920 / 3840f).dp, (1930 * 1080 / 2160f).dp),
                )
            }
        }
    }
}

@Composable
private fun DrawArc(state: Transition<Boolean>) {
    val path = Animatable(0f)
    LaunchedEffect(state.targetState && state.currentState) {
        if (state.targetState && state.currentState) {
            path.animateTo(1.2f, animationSpec = defaultSpec())
        }
    }

    Canvas(Modifier.fillMaxSize()) {
        val xEnd = (3044 * 1920 / 3840f).dp.toPx()
        val yEnd = (1953 * 1080 / 2160f).dp.toPx()
        val topLeft = Offset((2364 * 1920 / 3840f).dp.toPx(), ((1634 - 415) * 1920 / 3840f).dp.toPx())
        val size = Size((415).dp.toPx(), (415).dp.toPx())

        if (path.value > 0f) {
            val angleFraction = path.value.coerceAtMost(1f)
            val angle = lerp(0f, -129.5f, angleFraction)
            drawArc(Color.White, 180f, angle, false, topLeft, size, style = Stroke(2.0f))

            if (path.value > 1f) {
                val arrowFraction = (path.value - 1f).coerceAtLeast(0f) * 5f
                val left = lerp(Offset(xEnd, yEnd), Offset(xEnd - 25, yEnd - 2), arrowFraction)
                val right = lerp(Offset(xEnd, yEnd), Offset(xEnd - 2, yEnd + 25), arrowFraction)
                drawLine(Color.White, Offset(xEnd, yEnd), left, strokeWidth = 2.0f)
                drawLine(Color.White, Offset(xEnd, yEnd), right, strokeWidth = 2.0f)
            }
        }
    }
}
