package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.rememberAdvancementBoolean
import dev.bnorm.librettist.show.rememberAdvancementIndex
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.show.assist.ShowAssistTab
import kotlin.random.Random
import kotlin.random.nextInt

fun ShowBuilder.AssertionLibraries() {
    section(title = { Text("Assertion Libraries") }) {
        slide { SectionHeader() }
        KotlinLibraries()
        GroovyLibraries()
        Conclusion()
    }
}

// TODO allowing typing in the names of libraries and having the count increase while the name of the library gets added to the background
//  This list of libraries could be used later in a summary slide?
// TODO store these in a global state so they are always remembered
private const val MAX_KOTLIN_LIBRARY_COUNT = 12
private val KNOWN_KOTLIN_LIBRARIES =
    listOf("kotlin.test", "Strikt", "Kotest", "AssertK", "HamKrest", "Atrium", "Kluent")

private fun ShowBuilder.KotlinLibraries() {
    slide {
        val libraries = remember { mutableStateListOf<String>() }
        val answerVisible by rememberAdvancementBoolean()

        ShowAssistTab("Libraries") {
            // TODO what if there is now assist tab?
            LibraryAssist(libraries)
        }

        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                HowManyKotlinLibraries(answerVisible = answerVisible)

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AnimatedVisibility(
                        visible = !answerVisible,
                        enter = expandIn(expandFrom = Alignment.Center),
                        exit = shrinkOut(shrinkTowards = Alignment.Center),
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            if (libraries.isNotEmpty()) {
                                Text(
                                    text = if (libraries.size > MAX_KOTLIN_LIBRARY_COUNT) "Gah!" else libraries.size.toString(),
                                    style = MaterialTheme.typography.h1,
                                    fontSize = (60 + 8 * libraries.size).sp,
                                )
                            }

                            // TODO more evenly place text in the background? one repeat in each quadrant?
                            val random = Random(0)
                            for (library in libraries) {
                                repeat(4) {
                                    Text(
                                        text = library,
                                        color = LocalContentColor.current.copy(alpha = 0.5f),
                                        modifier = Modifier
                                            .rotate(random.nextFloat() * 90f - 45f)
                                            .offset(
                                                x = random.nextInt(-400..400).dp,
                                                y = random.nextInt(-200..200).dp,
                                            )
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
}

private fun ShowBuilder.GroovyLibraries() {
    slide {
        TitleAndBody {
            HowManyKotlinLibraries(answerVisible = true)
            Spacer(modifier = Modifier.height(16.dp))
            HowManyGroovyLibraries(answerVisible = false)
        }
    }
    slide {
        val index by rememberAdvancementIndex(3)
        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    HowManyKotlinLibraries(answerVisible = true)
                    Spacer(modifier = Modifier.height(16.dp))
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
}

private fun ShowBuilder.Conclusion() {
    slide {
        TitleAndBody {
            Column(modifier = Modifier.fillMaxSize()) {
                HowManyKotlinLibraries(answerVisible = true)
                Spacer(modifier = Modifier.height(16.dp))
                HowManyGroovyLibraries(answerVisible = true)
                Spacer(modifier = Modifier.height(16.dp))
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
                modifier = Modifier.fillMaxWidth().padding(start = 32.dp, top = 16.dp),
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
                modifier = Modifier.fillMaxWidth().padding(start = 32.dp, top = 16.dp),
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
            }, style = MaterialTheme.typography.h3
        )
        Text(
            text = buildAnnotatedString {
                append("but only 1 ")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                    append("Groovy")
                }
                append("-specific library?")
            }, style = MaterialTheme.typography.h3
        )
    }
}


@Composable
private fun LibraryAssist(libraries: SnapshotStateList<String>) {
    Column {
        var text by remember { mutableStateOf("") }
        TextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (text !in libraries) libraries.add(text)
                    text = ""
                }
            ),
        )

        for (knownLibrary in KNOWN_KOTLIN_LIBRARIES) {
            Button(
                onClick = { if (knownLibrary !in libraries) libraries.add(knownLibrary) },
                enabled = knownLibrary !in libraries,
            ) {
                Text(knownLibrary)
            }
        }
    }
}
