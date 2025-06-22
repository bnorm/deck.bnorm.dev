package dev.bnorm.dcnyc25.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.CodeString
import dev.bnorm.dcnyc25.MagicCodeSample
import dev.bnorm.dcnyc25.highlight
import dev.bnorm.dcnyc25.old.magic.toWords
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toState

private data class IdeaState(
    val highlightUnique: Boolean = false,
    val highlightExpanded: Boolean = false,
    val infoProgress: Int = 0,
    val showCompose: Boolean = false,
    val composeIndex: Int = 0,
    val showAfter: Boolean = false,
)

fun StoryboardBuilder.Idea(sampleStart: CodeString, sampleEnd: CodeString) {
    val states = start { IdeaState(highlightUnique = true) }
        .then { copy(infoProgress = 1) }
        .then { copy(infoProgress = 2, highlightUnique = false, highlightExpanded = true) }
        .then { copy(infoProgress = 3) }
        .then { copy(showCompose = true, highlightExpanded = false) }
        .then { copy(composeIndex = 1) }
        .then { copy(composeIndex = 2) }
        .then { copy(composeIndex = 3) }
        .then { copy(composeIndex = 4) }
        .then { copy(composeIndex = 5) }
        .then { copy(showAfter = true) }
        .then { copy(showAfter = false) }
        .then { copy(showCompose = false) }
        .then { copy(infoProgress = 4) }
        .then { copy(infoProgress = 5) }

    scene(
        states = states,
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.BottomCenter),
            end = SceneEnter(alignment = Alignment.CenterEnd),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.BottomCenter),
            end = SceneExit(alignment = Alignment.CenterEnd),
        ),
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
                IdeaInfo(state.createChildTransition { it.infoProgress })
            }

            Vertical(
                MaterialTheme.colors.secondary,
                modifier = Modifier.sharedElement(rememberSharedContentState("diff-example"))
            ) {
                IdeaSample(
                    state.createChildTransition { it.showAfter },
                    sampleStart,
                    sampleEnd,
                    state.createChildTransition { it.highlightUnique },
                    state.createChildTransition { it.highlightExpanded },
                )
            }

            Vertical(MaterialTheme.colors.primary) {
                Box(Modifier.padding(16.dp)) {
                    TextSurface {
                        ProvideTextStyle(MaterialTheme.typography.code2) {
                            MagicCodeSample(
                                state.createChildTransition { MagicTextSample[it.composeIndex] },
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
private fun IdeaInfo(progress: Transition<Int>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row {
                OutlinedText("Idea", style = MaterialTheme.typography.h2)
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
                Bullet(step = 1, text = "Match unique elements which appear in both sequences.")
                Bullet(step = 2, text = "Expand match ranges in both sequences while beginning and end match.")
                Bullet(step = 3, text = "Repeat for remaining, unmatched elements.")
                Bullet(step = 4, text = "Some visual inconsistencies, but handles many code changes well.")
                Bullet(step = 5, text = "Use LCS idea from Patience to improve expansion order?")
            }
        }
    }
}

@Composable
private fun IdeaSample(
    showAfter: Transition<Boolean>,
    before: CodeString,
    after: CodeString,
    showUnique: Transition<Boolean>,
    showExpanded: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {
    val unique = remember(before.text, after.text) {
        val beforeUnique = before.text.toWords().map { it.text }
            .groupingBy { it }.eachCount()
            .filter { it.value == 1 }.keys

        val afterUnique = after.text.toWords().map { it.text }
            .groupingBy { it }.eachCount()
            .filter { it.value == 1 }.keys

        (beforeUnique intersect afterUnique).toList()
    }

    val expanded = remember {
        // TODO ew
        listOf(
            "fun ",
            "main() ",
            "{",
            "  println(\"",
            "Hello",
            ", ",
            "droidcon!\")",
            "}",
        )
    }

    @Composable
    fun Before(modifier: Modifier = Modifier) {
        val measurer = rememberTextMeasurer()
        val style = MaterialTheme.typography.code1
        val textLayout = remember(before.text) { measurer.measure(before.text, style = style) }

        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedText("Before", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Box(Modifier.padding(16.dp)) {
                    HighlightStrings(
                        visible = showUnique,
                        strings = unique,
                        color = MatchColor,
                        textLayout = { textLayout },
                    )
                    HighlightStrings(
                        visible = showExpanded,
                        strings = expanded,
                        color = MatchColor,
                        textLayout = { textLayout },
                    )

                    ProvideTextStyle(style) {
                        MagicText(
                            transition = showAfter.createChildTransition {
                                if (it) after.text.toWords() else before.text.toWords()
                            },
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun After(modifier: Modifier = Modifier) {
        var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }

        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedText("After", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Box(Modifier.padding(16.dp)) {
                    HighlightStrings(
                        visible = showUnique,
                        strings = unique,
                        color = MatchColor,
                        textLayout = { textLayout!! },
                    )
                    HighlightStrings(
                        visible = showExpanded,
                        strings = expanded,
                        color = MatchColor,
                        textLayout = { textLayout!! },
                    )
                    Text(
                        text = after.text,
                        style = MaterialTheme.typography.code1,
                        onTextLayout = { textLayout = it },
                    )
                }
            }
        }
    }

    Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Before(Modifier.weight(1f))
        After(Modifier.weight(1f))
    }
}

private val MagicTextSample = buildCodeSamples {
    val collapse by tag("collapse")

    val body by tag("body")
    val pieces by tag("script")
    val layout by tag("layout")
    val piece by tag("edit")
    val modifier by tag("modifier")
    val text by tag("text")

    val base = extractTags(
        """
            @Composable
            fun MagicText(
              before: AnnotatedString,
              after: AnnotatedString,
              asAfter: Boolean,
            ) {${body}
              ${pieces}val (bPieces, aPieces) =
                remember(before, after) {
                  computePieces(before, after)
                }${pieces}

              ${layout}SharedTransitionLayout {
                AnimatedContent(asAfter) { asAfter ->
                  val pieces: List<Piece> =
                    if (asAfter) aPieces else bPieces

                  Column {
                    val iter = pieces.iterator()
                    while (iter.hasNext()) {
                      Row {
                        while (iter.hasNext()) {
                          val piece = iter.next()
                          if (piece.isLineEnd()) break
                          Piece(piece)
                        }
                      }
                    }
                  }
                }
              }${layout}
            ${body}}

            ${piece}@Composable
            context(
              _: AnimatedVisibilityScope,
              _: SharedTransitionScope
            )
            fun RowScope.Piece(piece: Piece) {
              ${modifier}val modifier = when {
                piece.key == null -> 
                  Modifier.animateEnterExit(${collapse}${collapse})
                piece.crossFade -> Modifier.sharedBounds(
                  rememberSharedContentState(piece.key),
                 ${collapse}${collapse}
                )
                else -> Modifier.sharedElement(
                  rememberSharedContentState(piece.key),
                 ${collapse}${collapse}
                )
              }${modifier}

              ${text}Text(piece.text, modifier.alignByBaseline())${text}
            }${piece}
        """.trimIndent()
    ).highlight {
        when (it) {
            "text",
            "key",
                -> INTELLIJ_LIGHT.property

            "computePieces",
                -> INTELLIJ_LIGHT.staticFunctionCall

            "sharedElement",
            "sharedBounds",
            "animateEnterExit",
            "alignByBaseline",
                -> INTELLIJ_LIGHT.extensionFunctionCall

            "remember",
            "SharedTransitionLayout",
            "AnimatedContent",
            "Column",
            "Row",
            "rememberSharedContentState",
            "Piece",
            "Text",
                -> SpanStyle(color = Color(0xFF009900)) // Compose function calls.

            else -> null
        }
    }

    CodeSample(base).collapse(body).hide(piece).collapse(collapse)
        .then { reveal(body).focus(pieces) }
        .then { focus(layout).reveal(piece) }
        .then { scroll(piece).focus(modifier) }
        .then { focus(text) }
        .then { unfocus() }
}
