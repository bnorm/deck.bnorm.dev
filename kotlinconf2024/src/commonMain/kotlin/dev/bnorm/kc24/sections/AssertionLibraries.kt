package dev.bnorm.kc24.sections

import androidx.compose.animation.*
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
import dev.bnorm.kc24.template.SLIDE_CONTENT_SPACING
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.rememberAdvancementBoolean
import dev.bnorm.librettist.show.rememberAdvancementIndex
import dev.bnorm.librettist.section.section
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
    section(title = { Text("Assertion Libraries") }) {
        slide { SectionHeader() }
        KotlinLibraries(state)
        GroovyLibraries()
        Conclusion()
    }
}

private fun ShowBuilder.KotlinLibraries(state: AssertionLibrariesState) {
    slide {
        if (state.assisted) {
            // TODO what if there is no assist tab?
            ShowAssistTab("Libraries") {
                LibraryAssist(state)
            }
        } else {
            val count by rememberAdvancementIndex(AssertionLibrariesState.MAX_COUNT + 1)
            LaunchedEffect(count) {
                state.count = count
            }
        }

        val answerVisible by rememberAdvancementBoolean()

        TitleAndBody {
            Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                HowManyKotlinLibraries(answerVisible = answerVisible)
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AnimatedVisibility(
                    visible = !answerVisible,
                    enter = expandIn(expandFrom = Alignment.Center),
                    exit = shrinkOut(shrinkTowards = Alignment.Center),
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (state.count > 0) {
                            Text(
                                text = if (state.count >= AssertionLibrariesState.MAX_COUNT) "Gah!" else state.count.toString(),
                                style = MaterialTheme.typography.h1,
                                fontSize = (60 + 8 * state.count).sp,
                            )
                        }

                        val random = Random(0)
                        for (library in state.libraries.subList(0, state.count)) {
                            for ((x0, y0) in listOf(250 to 100, 250 to -100, -250 to -100, -250 to 100)) {
                                val dx = x0 + random.nextInt(-150..150)
                                val dy = y0 + random.nextInt(-75..75)

                                Text(
                                    text = library,
                                    color = LocalContentColor.current.copy(alpha = 0.25f),
                                    fontSize = 42.sp,
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
    slide {
        val index by rememberAdvancementIndex(3)
        TitleAndBody {
            Column(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                HowManyKotlinLibraries(answerVisible = true)
                Spacer(modifier = Modifier.height(SLIDE_CONTENT_SPACING))
                HowManyGroovyLibraries(answerVisible = index == 2)
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AnimatedVisibility(
                    visible = index != 2,
                    enter = expandIn(expandFrom = Alignment.Center),
                    exit = shrinkOut(shrinkTowards = Alignment.Center),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Spock!", style = MaterialTheme.typography.h1)
                        AnimatedVisibility(index == 1) {
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
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(start = 32.dp, top = SLIDE_CONTENT_SPACING),
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
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(start = 32.dp, top = SLIDE_CONTENT_SPACING),
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
        modifier = Modifier.fillMaxSize().padding(top = 32.dp),
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
