package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import dev.bnorm.kc24.elements.AnimatedVisibility
import dev.bnorm.kc24.elements.defaultSpec
import dev.bnorm.kc24.template.SLIDE_CONTENT_SPACING
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.*
import dev.bnorm.librettist.show.assist.ShowAssistTab
import kotlin.random.Random
import kotlin.random.nextInt

class AssertionLibrariesState(
    startingLibraries: List<String>? = KNOWN_KOTLIN_LIBRARIES,
) {
    val assisted: Boolean = startingLibraries == null
    var count by mutableIntStateOf(0)
    val libraries = mutableStateListOf<String>()

    init {
        if (startingLibraries != null) libraries.addAll(startingLibraries)
    }

    companion object {
        val KNOWN_KOTLIN_LIBRARIES = listOf(
            "kotlin.test",
            "Strikt",
            "Kotest",
            "AssertK",
            "HamKrest",
            "Atrium",
            "Kluent",
        )
        val MAX_COUNT = KNOWN_KOTLIN_LIBRARIES.size
    }
}

fun ShowBuilder.AssertionLibraries(state: AssertionLibrariesState) {
    KotlinLibraries(state)
    GroovyLibraries()
    Conclusion()
}

private fun ShowBuilder.KotlinLibraries(state: AssertionLibrariesState) {
    @Composable
    fun impl(answerVisible: Transition<Boolean>, count: Int) {
        TitleAndBody {
            Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                HowManyKotlinLibraries(answerVisible = answerVisible.targetState)
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                answerVisible.AnimatedVisibility(
                    visible = { !it },
                    enter = expandIn(defaultSpec(), expandFrom = Alignment.Center),
                    exit = shrinkOut(defaultSpec(), shrinkTowards = Alignment.Center),
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (count > 0) {
                            Text(
                                text = if (count >= AssertionLibrariesState.MAX_COUNT) "Gah!" else count.toString(),
                                style = MaterialTheme.typography.h1,
                                fontSize = (120 + 16 * count).sp,
                            )
                        }

                        val random = Random(0)
                        for (library in state.libraries.subList(0, count)) {
                            for ((x0, y0) in listOf(500 to 200, 500 to -200, -500 to -200, -500 to 200)) {
                                val dx = x0 + random.nextInt(-300..300)
                                val dy = y0 + random.nextInt(-150..150)

                                Text(
                                    text = library,
                                    color = LocalContentColor.current.copy(alpha = 0.25f),
                                    fontSize = 84.sp,
                                    modifier = Modifier
                                        .rotate(random.nextFloat() * 60f - 30f)
                                        .offset(x = dx.dp, y = dy.dp)
                                        .zIndex(-1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    if (state.assisted) {
        slideForBoolean {
            // TODO what if there is no assist tab?
            ShowAssistTab("Libraries") {
                LibraryAssist(state)
            }

            impl(transition.createChildTransition { it.toBoolean() }, state.count)
        }
    } else {
        slide(states = AssertionLibrariesState.MAX_COUNT + 2) {
            val answerVisible = transition.createChildTransition { it >= AssertionLibrariesState.MAX_COUNT + 1 }
            val libraryCount = transition.createChildTransition { it.toInt().coerceIn(0..AssertionLibrariesState.MAX_COUNT) }
            impl(answerVisible, libraryCount.targetState)
        }
    }
}

private fun ShowBuilder.GroovyLibraries() {
    slide {
        TitleAndBody {
            Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                HowManyKotlinLibraries(answerVisible = true)
                Spacer(modifier = Modifier.height(SLIDE_CONTENT_SPACING))
                HowManyGroovyLibraries(answerVisible = false)
            }
        }
    }
    slide(states = 3) {
        val spockVisible = transition.createChildTransition { it < 2 }
        val questionVisible = transition.createChildTransition { it >= 1 }
        val answerVisible = transition.createChildTransition { it >= 2 }

        TitleAndBody {
            Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                HowManyKotlinLibraries(answerVisible = true)
                Spacer(modifier = Modifier.height(SLIDE_CONTENT_SPACING))
                HowManyGroovyLibraries(answerVisible = answerVisible.targetState)
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                spockVisible.AnimatedVisibility(
                    enter = expandIn(defaultSpec(), expandFrom = Alignment.Center),
                    exit = shrinkOut(defaultSpec(), shrinkTowards = Alignment.Center),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Spock!", style = MaterialTheme.typography.h1)
                        questionVisible.AnimatedVisibility(
                            enter = expandIn(defaultSpec(), expandFrom = Alignment.Center),
                            exit = shrinkOut(defaultSpec(), shrinkTowards = Alignment.Center),
                        ) {
                            Text(text = "(...is that it?)")
                        }
                    }
                }
            }
        }
    }
}

private fun ShowBuilder.Conclusion() {
    slide {
        TitleAndBody {
            Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                HowManyKotlinLibraries(answerVisible = true)
                Spacer(modifier = Modifier.height(SLIDE_CONTENT_SPACING))
                HowManyGroovyLibraries(answerVisible = true)
                Spacer(modifier = Modifier.height(SLIDE_CONTENT_SPACING))
                ConcludingQuestion()
            }
        }
    }
}

@Composable
private fun HowManyKotlinLibraries(answerVisible: Boolean) {
    Column {
        Text(buildAnnotatedString {
            append("How many ")
            withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                append("Kotlin")
            }
            append("-specific assertion libraries are there?")
        })

        AnimatedVisibility(
            visible = answerVisible,
            enter = fadeIn(defaultSpec()) + expandVertically(defaultSpec()),
            exit = fadeOut(defaultSpec()) + shrinkVertically(defaultSpec()),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(start = 64.dp, top = SLIDE_CONTENT_SPACING),
                contentAlignment = Alignment.TopStart
            ) {
                Text(buildAnnotatedString {
                    append("=> ")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                        append("Lots")
                    }
                })
            }
        }
    }
}

@Composable
private fun HowManyGroovyLibraries(answerVisible: Boolean) {
    Column {
        Text(buildAnnotatedString {
            append("How many ")
            withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                append("Groovy")
            }
            append("-specific assertion libraries are there?")
        })

        AnimatedVisibility(
            visible = answerVisible,
            enter = fadeIn(defaultSpec()) + expandVertically(defaultSpec()),
            exit = fadeOut(defaultSpec()) + shrinkVertically(defaultSpec()),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(start = 64.dp, top = SLIDE_CONTENT_SPACING),
                contentAlignment = Alignment.TopStart
            ) {
                Text(buildAnnotatedString {
                    append("=> ")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                        append("Spock")
                    }
                })
            }
        }
    }
}

@Composable
private fun ConcludingQuestion() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 64.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                append("Why so many ")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                    append("Kotlin")
                }
                append("-specific libraries,")
            },
            style = MaterialTheme.typography.h5
        )
        Text(
            text = buildAnnotatedString {
                append("but only 1 ")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                    append("Groovy")
                }
                append("-specific library?")
            },
            style = MaterialTheme.typography.h5
        )
    }
}


@Composable
private fun LibraryAssist(state: AssertionLibrariesState) {
    Column {
        var text by remember { mutableStateOf("") }
        TextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (text !in state.libraries) {
                        state.libraries.add(text)
                        state.count++
                    }
                    text = ""
                }
            ),
        )

        for (knownLibrary in AssertionLibrariesState.KNOWN_KOTLIN_LIBRARIES) {
            Button(
                enabled = knownLibrary !in state.libraries,
                onClick = {
                    if (knownLibrary !in state.libraries) {
                        state.libraries.add(knownLibrary)
                        state.count++
                    }
                },
            ) {
                Text(knownLibrary)
            }
        }
    }
}
