package dev.bnorm.dcnyc25

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.template.code1
import dev.bnorm.dcnyc25.template.storyDecorator
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.background_building
import dev.bnorm.deck.story.generated.resources.droidcon_newyork
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import org.jetbrains.compose.resources.painterResource

/**
 * ### General Ideas
 * * lots of background color blocking with the primary and secondary colors
 * *
 *
 * TODO show how different string diff algorithms work
 *  * https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore_string-search_algorithm
 *  * https://blog.jcoglan.com/2017/09/19/the-patience-diff-algorithm/
 */
fun createStoryboard(
    decorator: SceneDecorator = storyDecorator(),
): Storyboard = Storyboard.build(
    title = "(Re)creating Magic(Move) with Compose",
    description = """
        Presentation software is extremely powerful, full of creative styling, powerful animations, and plenty of
        other things. Yet every time I wrote a new presentation, I found myself repeating something with almost
        every slide; whether that be the content, styling, or animations. Wanting the torment to end (and a
        different kind of torment to begin), I wrote my own presentation framework with Compose!

        But wanting to be like all the cool presenters, I needed something like Keynote’s Magic Move for all my code
        examples! Compose has SharedTransitionLayout, how hard could this be? So I built my own version of Magic
        Move. And rebuilt it. And rebuilt it again. And Again. (And probably again between submitting and actually
        giving this talk) Let’s look at all those different iterations, the improvements each brought, and why
        diffing algorithms are hard.
    """.trimIndent(),
    decorator = decorator,
) {
    Title()
    CodeExamples()
}

fun StoryboardBuilder.Title() {
    val panes = listOf<SceneContent<Int>>(
        SceneContent {
            HorizontalPane(color = MaterialTheme.colors.primary) {
                Row {
                    Image(
                        painter = painterResource(Res.drawable.droidcon_newyork),
                        contentDescription = "title",
                        modifier = Modifier.width(LocalStoryboard.current!!.format.toDpSize().width / 2).padding(32.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource(Res.drawable.background_building),
                        contentDescription = "building",
                    )
                }
            }
        },
        SceneContent {
            HorizontalPane(color = MaterialTheme.colors.secondary) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                                append("re")
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                    append("creating\nmagic")
                                }
                                append("move\nwith compose")
                            }
                        },
                        style = MaterialTheme.typography.h2,
                    )
                    Spacer(Modifier.weight(1f))
                    Column {
                        JetBrainsEmployee(
                            name = "Brian Norman",
                            title = "Kotlin Compiler Developer",
                        )
                        Spacer(Modifier.size(4.dp))
                        Column(Modifier.padding(start = 12.dp)) {
                            Mastodon(username = "bnorm@kotlin.social")
                            Spacer(Modifier.size(4.dp))
                            Bluesky(username = "@bnorm.dev")
                        }
                    }
                }
            }
        },
        SceneContent {
            HorizontalPane(color = MaterialTheme.colors.secondary) {
            }
        }
    )

    scene(
        stateCount = panes.size - 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd, animationSpec = tween(750, easing = EaseInOut)),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd, animationSpec = tween(750, easing = EaseInOut)),
    ) {
        HorizontalPanes(panes)
    }
}

private fun StoryboardBuilder.CodeExamples() {
    val panes = listOf<SceneContent<Int>>(
        SceneContent {
            VerticalPane(color = MaterialTheme.colors.secondary) {
                Column {
                    Box(Modifier.padding(32.dp)) {
                        Text("Hello World!")
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(32.dp).fillMaxSize()
                    ) {
                        Text(
                            text = """
                                fun main() {
                                }
                            """.trimIndent().highlight(INTELLIJ_LIGHT, Language.Kotlin),
                            style = MaterialTheme.typography.code1,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        },
        SceneContent {
            VerticalPane(color = MaterialTheme.colors.primary) {
                Column {
                    Box(Modifier.padding(32.dp)) {
                        Text("Hello World!")
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(32.dp).fillMaxSize()
                    ) {
                        Text(
                            text = """
                                fun main() {
                                  println("Hello, World!")
                                }
                            """.trimIndent().highlight(INTELLIJ_LIGHT, Language.Kotlin),
                            style = MaterialTheme.typography.code1,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        },
        SceneContent {
            VerticalPane(color = MaterialTheme.colors.secondary) {
                Column {
                    Box(Modifier.padding(32.dp)) {
                        Text("Hello World!")
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(32.dp).fillMaxSize()
                    ) {
                        Text(
                            text = """
                                fun main() {
                                  println("Hello, droidcon NYC!")
                                }
                            """.trimIndent().highlight(INTELLIJ_LIGHT, Language.Kotlin),
                            style = MaterialTheme.typography.code1,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    )

    scene(
        stateCount = panes.size - 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd, animationSpec = tween(750, easing = EaseInOut)),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd, animationSpec = tween(750, easing = EaseInOut)),
    ) {
        VerticalPanes(panes)
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun SceneScope<Int>.HorizontalPanes(
    panes: List<SceneContent<Int>>,
    background: Color = MaterialTheme.colors.secondary,
) {
    val state = rememberScrollState()
    transition.animateScroll(state, transitionSpec = { tween(750, easing = EaseInOut) }) {
        if (it.toState() == 0) 0f else with(LocalDensity.current) { SceneHalfWidth.toPx() }
    }

    Column(Modifier.fillMaxSize().verticalScroll(state, enabled = false).background(background)) {
        for (content in panes) {
            Render(content)
        }
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun SceneScope<Int>.VerticalPanes(
    panes: List<SceneContent<Int>>,
    background: Color = MaterialTheme.colors.secondary,
) {
    val state = rememberScrollState()
    transition.animateScroll(state, transitionSpec = { tween(750, easing = EaseInOut) }) {
        if (it.toState() == 0) 0f else with(LocalDensity.current) { SceneHalfWidth.toPx() }
    }

    Row(Modifier.fillMaxSize().horizontalScroll(state, enabled = false).background(background)) {
        for (content in panes) {
            Render(content)
        }
    }
}

@Composable
inline fun <T> Transition<T>.animateScroll(
    scrollState: ScrollState,
    noinline transitionSpec: @Composable Transition.Segment<T>.() -> FiniteAnimationSpec<Float> = {
        spring()
    },
    label: String = "ScrollAnimation",
    targetValueByState: @Composable (state: T) -> Float,
) {
    val scrollPosition by animateFloat(transitionSpec, label, targetValueByState)
    scrollState.dispatchRawDelta(scrollPosition - scrollState.value)
}

inline val SceneHalfWidth: Dp
    @Composable get() {
        val format = LocalStoryboard.current!!.format
        return with(format.density) { format.size.width.toDp() / 2 }
    }

inline val SceneHalfHeight: Dp
    @Composable get() {
        val format = LocalStoryboard.current!!.format
        return with(format.density) { format.size.height.toDp() / 2 }
    }

@Composable
fun VerticalPane(
    color: Color = MaterialTheme.colors.surface,
    content: @Composable () -> Unit,
) {
    Surface(Modifier.fillMaxHeight().width(SceneHalfWidth), color = color, content = content)
}

@Composable
fun HorizontalPane(
    color: Color = MaterialTheme.colors.surface,
    content: @Composable () -> Unit,
) {
    Surface(Modifier.fillMaxWidth().height(SceneHalfHeight), color = color, content = content)
}

@Composable
fun QuarterPane(
    color: Color = MaterialTheme.colors.surface,
    content: @Composable () -> Unit,
) {
    Surface(Modifier.size(SceneHalfWidth, SceneHalfHeight), color = color, content = content)
}
