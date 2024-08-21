package dev.bnorm.kc25

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.*
import dev.bnorm.librettist.text.buildKotlinCodeString
import dev.bnorm.storyboard.core.*
import dev.bnorm.storyboard.easel.DesktopStoryboard
import dev.bnorm.storyboard.easel.EmbeddedStoryboard
import dev.bnorm.storyboard.easel.SlideEnter
import dev.bnorm.storyboard.easel.SlideExit
import dev.bnorm.storyboard.text.highlight.Highlighting
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.ui.FixedSize
import kotlinx.coroutines.delay

fun main() {
    DesktopStoryboard(
        title = "Sample Storyboard",
        storyboard = createStoryboard(mutableStateOf(DARK_COLORS)),
    )
}

val HIGHLIGHTING: Highlighting
    @Composable get() {
        val fontFamily = JetBrainsMono
        return Highlighting.build {
            simple += SpanStyle(color = Color(0xFFBCBEC4), fontFamily = fontFamily)
            number = simple + SpanStyle(color = Color(0xFF2AACB8))
            keyword = simple + SpanStyle(color = Color(0xFFCF8E6D))
            punctuation = simple + SpanStyle(color = Color(0xFFA1C17E))
            annotation = simple + SpanStyle(color = Color(0xFFBBB529))
            comment = simple + SpanStyle(color = Color(0xFF7A7E85))
            string = simple + SpanStyle(color = Color(0xFF6AAB73))
            property = simple + SpanStyle(color = Color(0xFFC77DBB))
            functionDeclaration = simple + SpanStyle(color = Color(0xFF56A8F5))
            extensionFunctionCall = simple + SpanStyle(color = Color(0xFF56A8F5), fontStyle = FontStyle.Italic)
            staticFunctionCall = simple + SpanStyle(fontStyle = FontStyle.Italic)
            typeParameters = simple + SpanStyle(color = Color(0xFF16BAAC))
        }
    }

val DARK_COLORS = darkColors(
    // KotlinConf 2024 website colors
    // "blue/purple" : 7E53FE or 7F51FF
    // "red" : FF2757
    // "orange" : FDB60D

    background = Color.Black,
    surface = Color(0xFF1E1F22),
    onBackground = Color(0xFFBCBEC4),
    primary = Color(0xFF7F51FF),
    primaryVariant = Color(0xFF7E53FE),
    secondary = Color(0xFFFDB60D),
)

val LIGHT_COLORS = lightColors(
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    primary = Color(0xFF7F51FF),
    primaryVariant = Color(0xFF7E53FE),
    secondary = Color(0xFFFDB60D),
)

private fun createStoryboard(colors: State<Colors>): Storyboard {
    val theme = SlideDecorator { content ->
        Highlighting(HIGHLIGHTING) {
            MaterialTheme(
                colors = animateColorsAsState(colors.value),
                typography = Typography(defaultFontFamily = Inter)
            ) {
                content()
            }
        }
    }

    movableContentOf { }
    lateinit var storyboard: Storyboard
    storyboard = Storyboard.build(size = Storyboard.DEFAULT_SIZE, decorator = theme) {
        slide(
            @Composable {
                """
                    fun main() {}
                """.trimIndent().toCode()
            },
            @Composable {
                """
                    fun main() {
                        println()
                    }
                """.trimIndent().toCode()
            },
            @Composable {
                """
                    fun main() {
                        println("Hello, World!")
                    }
                """.trimIndent().toCode().focusOn("\"Hello, World!\"")
            },
            @Composable {
                """
                    fun main() {
                        println("Hello, World!")
                    }
                """.trimIndent().toCode()
            },
            enterTransition = SlideEnter, exitTransition = SlideExit,
        ) {
            val state = transition.currentState.toValue(states.first(), states.last())()

            Column {
                Button(onClick = { storyboard.jumpTo(Storyboard.Frame(2, 0)) }) {
                    Text("Skip!")
                }
                MagicText(state, modifier = Modifier.fillMaxWidth())
            }

            SharedKodee {
                AnimateKodee {
                    default { DefaultCornerKodee(Modifier.size(50.dp)) }
                    show({ state.contains("Hello, World!") }) {
                        KodeeSurprised(Modifier.size(75.dp))
                    }
                }
            }
        }

        slide(
            false,
            enterTransition = SlideEnter, exitTransition = SlideExit,
        ) {
            val color by transition.animateColor {
                when (it.toBoolean()) {
                    false -> MaterialTheme.colors.primary
                    true -> MaterialTheme.colors.secondary
                }
            }

            var count by rememberSaveable { mutableStateOf(0) }
            Button(onClick = { count++ }, colors = ButtonDefaults.buttonColors(backgroundColor = color)) {
                Text("Hello, World : $count")
            }

            SharedKodee {
                AnimateKodee {
                    default { DefaultCornerKodee(Modifier.size(50.dp)) }
                }
            }
        }

        slide(
            0.0, 1.0, 2.0,
            enterTransition = SlideEnter, exitTransition = SlideExit,
        ) {
            val state = transition.currentState.toValue(0.0, 2.0)
            val embeddedColors = remember { mutableStateOf(DARK_COLORS) }
            val embeddedStoryboard = remember { createStoryboard(embeddedColors) }
            embeddedColors.value = when (state > 1) {
                true -> LIGHT_COLORS
                false -> DARK_COLORS
            }

            Row {
                Column(modifier = Modifier.weight(3f)) {
                    var text by rememberSaveable { mutableStateOf("") }
                    Text("$text : $state")
                    TextField(text, onValueChange = { text = it })
                }
                Column(
                    modifier = Modifier.weight(2f).fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SampleStoryboard(embeddedStoryboard)
                    LaunchedEffect(state) {
                        embeddedStoryboard.moveTo(0, state.toInt())
                    }
                }
            }

            SharedKodee {
                AnimateKodee {
                    default { DefaultCornerKodee(Modifier.size(50.dp)) }
                    show(condition = { state == 2.0 }) {
                        KodeeSurprised(Modifier.size(75.dp))
                    }
                }
            }
        }
    }
    return storyboard
}

@Composable
private fun SampleStoryboard(storyboard: Storyboard) {
    Surface(
        elevation = 8.dp,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .aspectRatio(storyboard.size.width / storyboard.size.height)
    ) {
        MaterialTheme(colors = darkColors(), typography = Typography(), shapes = Shapes()) {
            FixedSize(Storyboard.DEFAULT_SIZE) {
                EmbeddedStoryboard(storyboard, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

private fun Storyboard.moveTo(slideIndex: Int, stateIndex: Int) {
    val currentFrame = currentFrame
    if (currentFrame.slideIndex == slideIndex) {
        when (currentFrame.stateIndex) {
            // The current index is the desired index -> do nothing.
            stateIndex -> return
            // The previous index is the current index -> advance forward to reach the desired index.
            stateIndex - 1 -> advance(AdvanceDirection.Forward)
            // The next index is the current index -> advance backward to reach the desired index.
            stateIndex + 1 -> advance(AdvanceDirection.Backward)
            // Otherwise -> jump directly to the desired index.
            else -> jumpTo(Storyboard.Frame(slideIndex, stateIndex))
        }
    } else {
        // TODO check for advancing to next or previous slide
        jumpTo(Storyboard.Frame(slideIndex, stateIndex))
    }
}

fun String.toStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
//        "",
//            -> codeStyle.property

        "main",
            -> codeStyle.functionDeclaration

//        "",
//            -> codeStyle.extensionFunctionCall

        "println",
            -> codeStyle.staticFunctionCall

        else -> null
    }
}

@Composable
fun String.toCode(
    identifierType: (Highlighting, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    val highlighting = Highlighting.current
    return rememberSaveable(highlighting, this) {
        buildKotlinCodeString(
            this,
            highlighting,
            identifierType = { identifierType(highlighting, it) ?: it.toStyle(highlighting) })
    }
}

fun AnnotatedString.focusOn(
    subString: String,
    focused: SpanStyle = SpanStyle(),
    unfocused: SpanStyle = SpanStyle(color = Color.Gray.copy(alpha = 0.5f)),
): AnnotatedString {
    val start = indexOf(subString).takeIf { it >= 0 } ?: return this
    val end = start + subString.length
    val length = length

    return buildAnnotatedString {
        append(this@focusOn.text)

        if (start != 0) {
            for (range in subSequence(0, start).spanStyles) {
                addStyle(range.item + unfocused, range.start, range.end)
            }
            addStyle(unfocused, 0, start)
        }

        for (range in subSequence(start, end).spanStyles) {
            addStyle(range.item + focused, start + range.start, start + range.end)
        }

        if (end != length) {
            for (range in subSequence(end, length).spanStyles) {
                addStyle(range.item + unfocused, end + range.start, end + range.end)
            }
            addStyle(unfocused, end, length)
        }
    }
}

@Composable
private fun animateColorsAsState(colors: Colors): Colors {
    val transition = updateTransition(colors)
    return colors.copy(
        primary = transition.animateColor { it.primary }.value,
        primaryVariant = transition.animateColor { it.primaryVariant }.value,
        secondary = transition.animateColor { it.secondary }.value,
        secondaryVariant = transition.animateColor { it.secondaryVariant }.value,
        background = transition.animateColor { it.background }.value,
        surface = transition.animateColor { it.surface }.value,
        error = transition.animateColor { it.error }.value,
        onPrimary = transition.animateColor { it.onPrimary }.value,
        onSecondary = transition.animateColor { it.onSecondary }.value,
        onBackground = transition.animateColor { it.onBackground }.value,
        onSurface = transition.animateColor { it.onSurface }.value,
        onError = transition.animateColor { it.onError }.value,
    )
}
