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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.CodeString
import dev.bnorm.dcnyc25.highlight
import dev.bnorm.dcnyc25.old.kc24.animateList
import dev.bnorm.dcnyc25.old.kc24.startAnimation
import dev.bnorm.dcnyc25.old.kc24.thenLineEndDiff
import dev.bnorm.dcnyc25.template.*
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.deck.shared.JetBrainsMono
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
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState

private data class LineEndingState(
    val highlightDiff: Boolean = false,
    val highlightPrefix: Boolean = false,
    val highlightDelete: Boolean = false,
    val highlightAdd: Boolean = false,
    val showInfo: Boolean = false,
    val infoProgress: Int = 0,
    val showCompose: Boolean = false,
    val composeIndex: Int = 0,
    val showAfter: Boolean = false,
)

fun StoryboardBuilder.LineEnding(before: CodeString, after: CodeString) {
    val states = start { LineEndingState() }
        .then { copy(highlightDiff = true) }
        .then { copy(highlightDiff = false, showInfo = true) }
        .then { copy(infoProgress = 1, highlightPrefix = true) }
        .then { copy(infoProgress = 2) }
        .then { copy(infoProgress = 3, highlightDelete = true) }
        .then { copy(infoProgress = 4, highlightAdd = true) }
        .then { copy(infoProgress = 5, highlightPrefix = false, highlightDelete = false, highlightAdd = false) }
        .then { copy(showCompose = true) }
        .then { copy(composeIndex = 1) }
        .then { copy(composeIndex = 2) }
        .then { copy(composeIndex = 3) }
        .then { copy(composeIndex = 4) }
        .then { copy(showAfter = true) }
        .then { copy(showAfter = false) }
        .then { copy(showCompose = false) }

    scene(
        states = states,
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.CenterEnd),
            end = SceneEnter(alignment = Alignment.BottomCenter),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.CenterEnd),
            end = SceneExit(alignment = Alignment.BottomCenter),
        ),
    ) {
        val state = transition.createChildTransition { it.toState() }

        val scrollState = rememberScrollState()
        state.animateScroll(scrollState, transitionSpec = { tween(durationMillis = 750) }) {
            val pane = when {
                it.showCompose -> 1
                it.showInfo -> 0
                else -> 1
            }
            with(LocalDensity.current) { (SceneHalfWidth * pane).roundToPx() }
        }

        Row(Modifier.horizontalScroll(scrollState, enabled = false)) {
            Vertical(MaterialTheme.colors.primary) {
                LineEndingInfo(state)
            }

            LineEndingSample(
                state = state,
                before = before,
                after = after,
                modifier = Modifier.sharedElement(rememberSharedContentState("diff-example")),
            )

            Vertical(MaterialTheme.colors.primary) {
                Box(Modifier.padding(16.dp)) {
                    TextSurface {
                        Box(Modifier.padding(16.dp)) {
                            val code = state.createChildTransition {
                                MagicLineEndingSample[it.composeIndex].string.splitByTags()
                            }
                            ProvideTextStyle(MaterialTheme.typography.code2) {
                                MagicText(code)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LineEndingInfo(state: Transition<LineEndingState>) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            OutlinedText("Line Ending", style = MaterialTheme.typography.h2)
        }
        TextSurface {
            @Composable
            fun Reveal(step: Int, text: AnnotatedString) {
                state.AnimatedVisibility(
                    visible = { it.infoProgress >= step },
                    enter = fadeIn(tween(750)), exit = fadeOut(tween(750)),
                ) {
                    Text(text)
                }
            }

            @Composable
            fun Reveal(step: Int, text: String) = Reveal(step, AnnotatedString(text))

            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(BulletSpacing)) {
                Reveal(step = 1, text = "• Find the common prefix of each line.")
                Reveal(step = 2, text = "• Create a sequence which iterates from the first line to the last.")
                Reveal(step = 3, text = "• Create substrings of the line by removing each non-prefix character.")
                Reveal(step = 4, text = "• Continue creating substrings by adding each non-prefix character.")
                Reveal(
                    step = 5,
                    text = buildAnnotatedString {
                        append("• Use '")
                        withStyle(SpanStyle(fontFamily = JetBrainsMono)) {
                            append("animateIntAsState")
                        }
                        append("' with '")
                        withStyle(SpanStyle(fontFamily = JetBrainsMono)) {
                            append("LinearEasing")
                        }
                        append("' to iterate through the sequence.")
                    },
                )
            }
        }
    }
}

@Composable
private fun LineEndingSample(
    state: Transition<LineEndingState>,
    before: CodeString,
    after: CodeString,
    modifier: Modifier = Modifier,
) {
    val highlightDiff = state.createChildTransition { it.highlightDiff }
    val highlightPrefix = state.createChildTransition { it.highlightPrefix }
    val highlightDelete = state.createChildTransition { it.highlightDelete }
    val highlightAdd = state.createChildTransition { it.highlightAdd }

    val commonPrefix = remember {
        // TODO ew
        listOf(
            "fun main() {",
            "  println(\"Hello, ",
            "}",
        )
    }

    val sampleAnimation = remember(before.text, after.text) {
        startAnimation(before.text)
            .thenLineEndDiff(after.text)
            .toList()
    }

    val sampleText by state.animateList(
        values = sampleAnimation,
        transitionSpec = { typing(sampleAnimation.size) }
    ) {
        if (it.showAfter) sampleAnimation.lastIndex else 0
    }

    @Composable
    fun Before(modifier: Modifier = Modifier) {
        var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                OutlinedText("Before", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Box(Modifier.padding(16.dp)) {
                    HighlightStrings(
                        visible = highlightDiff,
                        strings = listOf("KotlinConf"),
                        color = DeleteColor,
                        textLayout = { textLayout!! },
                    )
                    HighlightStrings(
                        visible = highlightPrefix,
                        strings = commonPrefix,
                        color = MatchColor,
                        textLayout = { textLayout!! },
                    )
                    HighlightCharacters(
                        visible = highlightDelete,
                        strings = listOf("KotlinConf!\")"),
                        color = DeleteColor,
                        textLayout = { textLayout!! },
                    )
                    Text(
                        text = sampleText,
                        style = MaterialTheme.typography.code1,
                        onTextLayout = { textLayout = it },
                    )
                }
            }
        }
    }

    @Composable
    fun After(modifier: Modifier = Modifier) {
        var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                OutlinedText("After", style = MaterialTheme.typography.h2)
            }
            TextSurface {
                Box(Modifier.padding(16.dp)) {
                    HighlightStrings(
                        visible = highlightDiff,
                        strings = listOf("droidcon"),
                        color = AddColor,
                        textLayout = { textLayout!! },
                    )
                    HighlightStrings(
                        visible = highlightPrefix,
                        strings = commonPrefix,
                        color = MatchColor,
                        textLayout = { textLayout!! },
                    )
                    HighlightCharacters(
                        visible = highlightAdd,
                        strings = listOf("droidcon!\")"),
                        color = AddColor,
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

    SharedTransitionLayout(modifier) {
        state.AnimatedContent(transitionSpec = { EnterTransition.None togetherWith ExitTransition.None }) {
            Row {
                Vertical(MaterialTheme.colors.secondary) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Before(
                            Modifier.weight(1f).sharedElement(
                                rememberSharedContentState("before"),
                                boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                            )
                        )
                        if (it.showInfo) {
                            After(
                                Modifier.weight(1f).sharedElement(
                                    rememberSharedContentState("after"),
                                    boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                                )
                            )
                        }
                    }
                }
                if (it.infoProgress == 0) {
                    Vertical(MaterialTheme.colors.primary) {
                        if (!it.showInfo) {
                            Box(Modifier.padding(16.dp)) {
                                After(
                                    Modifier.sharedElement(
                                        rememberSharedContentState("after"),
                                        boundsTransform = BoundsTransform { _, _ -> tween(durationMillis = 750) }
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun TextLayoutResult.getBoundingBox(from: Int, to: Int): Rect {
    require(from <= to) { "from ($from) should be less than or equal to to ($to)" }
    require(from >= 0) { "from ($from) should be greater than or equal to 0" }
    require(to <= layoutInput.text.length) { "to ($to) should be less than or equal to length (${layoutInput.text.length})" }

    val fromBox = getBoundingBox(from)
    val toBox = if (from == to) fromBox else getBoundingBox(to)

    return Rect(
        left = minOf(fromBox.left, toBox.left),
        top = minOf(fromBox.top, toBox.top),
        right = maxOf(fromBox.right, toBox.right),
        bottom = maxOf(fromBox.bottom, toBox.bottom),
    )
}

private val MagicLineEndingSample = buildCodeSamples {
    val body by tag("body")
    val seq by tag("seq")
    val index by tag("index")
    val text by tag("text")

    val base = extractTags(
        """
            @Composable 
            fun MagicLineEnding(
              before: AnnotatedString,
              after: AnnotatedString,
              asAfter: Boolean,
            ) {${body}
              ${seq}val sequence: List<AnnotatedString> =
                remember(before, after) {
                  buildSequence(before, after)
                }${seq}
    
              ${index}val index: Int by animateIntAsState(
                targetValue = when {
                  asAfter -> sequence.lastIndex
                  else -> 0
                },
                animationSpec = tween(
                  durationMillis = 35 * sequence.size,
                  easing = LinearEasing
                )
              )${index}
    
              ${text}Text(sequence[index])${text}
            ${body}}
        """.trimIndent()
    ).highlight(
        identifierStyle = {
            when (it) {
                "size" -> INTELLIJ_LIGHT.property
                "lastIndex", "LinearEasing" -> INTELLIJ_LIGHT.staticProperty
                "buildSequence", "tween" -> INTELLIJ_LIGHT.staticFunctionCall
                // Compose function call.
                "remember", "animateIntAsState", "Text" -> SpanStyle(color = Color(0xFF009900))
                else -> null
            }
        }
    )

    CodeSample(base).collapse(body)
        .then { reveal(body).focus(seq) }
        .then { focus(index) }
        .then { focus(text) }
        .then { unfocus() }
}
