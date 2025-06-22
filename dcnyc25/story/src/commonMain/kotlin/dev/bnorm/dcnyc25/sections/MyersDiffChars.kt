package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.CodeString
import dev.bnorm.dcnyc25.MagicCodeSample
import dev.bnorm.dcnyc25.highlight
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toState

private data class MyersDiffState(
    val infoProgress: Int = 0,
    val showCompose: Boolean = false,
    val composeIndex: Int = 0,
    val showAfter: Boolean = false,
)

fun StoryboardBuilder.MyersDiffChars(before: CodeString, after: CodeString) {
    val states = start { MyersDiffState() }
        .then { copy(infoProgress = 1) }
        .then { copy(infoProgress = 2) }
        .then { copy(infoProgress = 3) }
        .then { copy(infoProgress = 4) }
        .then { copy(showCompose = true) }
        .then { copy(composeIndex = 1) }
        .then { copy(composeIndex = 2) }
        .then { copy(composeIndex = 3) }
        .then { copy(composeIndex = 4) }
        .then { copy(composeIndex = 5) }
        .then { copy(showAfter = true) }
        .then { copy(showAfter = false) }
        .then { copy(showCompose = false) }

    scene(
        states = states,
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {
        val state = transition.createChildTransition { it.toState() }

        val scrollState = rememberScrollState()
        state.animateScroll(scrollState, transitionSpec = { tween(durationMillis = 750) }) {
            val pane = when {
                it.showCompose -> 1
                else -> 0
            }
            with(LocalDensity.current) { (SceneHalfWidth * pane).roundToPx() }
        }

        Row(Modifier.horizontalScroll(scrollState, enabled = false)) {
            Vertical(MaterialTheme.colors.primary) {
                MyersDiffInfo(
                    title = state.createChildTransition { it.infoProgress >= 1 },
                    progress = state.createChildTransition { it.infoProgress },
                    modifier = Modifier.sharedElement(rememberSharedContentState("myers-diff")),
                )
            }

            MyersDiffCharsExample(
                showAfter = state.createChildTransition { it.showAfter },
                before = before,
                after = after,
                modifier = Modifier.sharedElement(rememberSharedContentState("diff-example")),
            )

            Vertical(MaterialTheme.colors.primary) {
                Box(Modifier.padding(16.dp)) {
                    TextSurface {
                        ProvideTextStyle(MaterialTheme.typography.code2) {
                            MagicCodeSample(
                                state.createChildTransition { MagicDiffSample[it.composeIndex] },
                                Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyersDiffInfo(title: Transition<Boolean>, progress: Transition<Int>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row {
                title.AnimatedVisibility(
                    visible = { it },
                    enter = fadeIn(tween(750)) +
                            expandHorizontally(tween(750), clip = false, expandFrom = Alignment.End),
                    exit = fadeOut(tween(750)) +
                            shrinkHorizontally(tween(750), clip = false, shrinkTowards = Alignment.End),
                ) {
                    OutlinedText("Myers ", style = MaterialTheme.typography.h2)
                }
                OutlinedText("Diff", style = MaterialTheme.typography.h2)
            }
        }
        TextSurface {
            @Composable
            fun Bullet(step: Int, text: String) {
                progress.AnimatedVisibility(
                    visible = { it >= step },
                    enter = fadeIn(tween(750)), exit = fadeOut(tween(750)),
                ) {
                    Text("• $text")
                }
            }

            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(BulletSpacing)) {
                Bullet(step = 1, text = "Most common algorithm published in 1986 by Eugene Myers.")
                Bullet(step = 2, text = "Computes the shortest edit script for transforming one string into another.")
                Bullet(step = 3, text = "Each edit will get a unique key used by shared element 'Modifier'.")
                Bullet(step = 4, text = "Transform edit script into a column of rows of 'Text' Composables.")
                Bullet(step = 5, text = "Instead of characters, use 'words' to break up code into elements.")
            }
        }
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun MyersDiffCharsExample(
    showAfter: Transition<Boolean>,
    before: CodeString,
    after: CodeString,
    modifier: Modifier = Modifier,
) {
    @Composable
    fun Before(modifier: Modifier = Modifier) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedText("Before", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    MagicText(
                        transition = showAfter.createChildTransition {
                            if (it) after.text.toChars() else before.text.toChars()
                        },
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    fun After(modifier: Modifier = Modifier) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedText("After", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Text(
                    text = after.text,
                    style = MaterialTheme.typography.code1,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }

    Row(modifier = modifier) {
        Vertical(MaterialTheme.colors.secondary) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Before(
                    Modifier.weight(1f).sharedElement(
                        rememberSharedContentState("before"),
                        boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                    )
                )
                After(
                    Modifier.weight(1f).sharedElement(
                        rememberSharedContentState("after"),
                        boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                    )
                )
            }
        }
    }
}

fun AnnotatedString.toChars(): List<AnnotatedString> {
    return buildList {
        for (i in 0..<length) {
            add(subSequence(i, i + 1))
        }
    }
}

private val MagicDiffSample = buildCodeSamples {
    val collapse by tag("collapse")

    val body by tag("body")
    val script by tag("script")
    val layout by tag("layout")
    val edit by tag("edit")
    val modifier by tag("modifier")
    val text by tag("text")

    val base = extractTags(
        """
            @Composable
            fun MagicDiff(
              before: AnnotatedString,
              after: AnnotatedString,
              asAfter: Boolean,
            ) {${body}
              ${script}val editScript: List<Edit> =
                remember(before, after) {
                  computeEditScript(before, after)
                }${script}

              ${layout}SharedTransitionLayout {
                AnimatedContent(asAfter) { asAfter ->
                  Column {
                    val iter = editScript.iterator()
                    while (iter.hasNext()) {
                      Row {
                        while (iter.hasNext()) {
                          val edit = iter.next()
                          Edit(edit, asAfter)
                         ${collapse}${collapse}
                        }
                      }
                    }
                  }
                }
              }${layout}
            ${body}}

            ${edit}@Composable
            context(
              _: AnimatedVisibilityScope,
              _: SharedTransitionScope
            )
            fun RowScope.Edit(edit: Edit, asAfter: Boolean) {
              ${modifier}val modifier = when (edit) {
                is Edit.Match -> Modifier.sharedElement(
                  rememberSharedContentState(edit.key),
                 ${collapse}${collapse}
                )
                else -> Modifier.animateEnterExit(${collapse}${collapse})
              }${modifier}

              ${text}if (
                edit is Edit.Match ||
                (edit is Edit.Add && asAfter) ||
                (edit is Edit.Delete && !asAfter)
              ) {
                Text(edit.text, modifier.alignByBaseline())
              }${text}
            }${edit}
        """.trimIndent()
    ).highlight {
        when (it) {
            "text",
            "key",
                -> INTELLIJ_LIGHT.property

            "computeEditScript",
                -> INTELLIJ_LIGHT.staticFunctionCall

            "sharedElement",
            "animateEnterExit",
            "alignByBaseline",
                -> INTELLIJ_LIGHT.extensionFunctionCall

            "remember",
            "SharedTransitionLayout",
            "AnimatedContent",
            "Column",
            "Row",
            "rememberSharedContentState",
            "Edit",
            "Text",
                -> SpanStyle(color = Color(0xFF009900)) // Compose function calls.

            else -> null
        }
    }

    CodeSample(base).collapse(body).hide(edit).collapse(collapse)
        .then { reveal(body).focus(script) }
        .then { focus(layout).reveal(edit) }
        .then { scroll(edit).focus(modifier) }
        .then { focus(text) }
        .then { unfocus() }
}
