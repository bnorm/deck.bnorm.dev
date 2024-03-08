package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.rememberAdvancementBoolean
import dev.bnorm.librettist.show.rememberAdvancementIndex
import dev.bnorm.librettist.section.section

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
private const val MAX_KOTLIN_LIBRARY_COUNT = 1
private fun ShowBuilder.KotlinLibraries() {
    slide {
        TitleAndBody {
            HowManyKotlinLibraries(answerVisible = false)
        }
    }
    slide {
        val count by rememberAdvancementIndex(MAX_KOTLIN_LIBRARY_COUNT)
        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                HowManyKotlinLibraries(answerVisible = false)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = (count + 1).toString(),
                        style = MaterialTheme.typography.h1,
                        fontSize = (60 + 8 * count).sp,
                    )
                }
            }
        }
    }
    slide {
        val visible by rememberAdvancementBoolean()
        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                Column {
                    HowManyKotlinLibraries(answerVisible = visible)
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AnimatedVisibility(
                        visible = !visible,
                        enter = expandIn(expandFrom = Alignment.Center),
                        exit = shrinkOut(shrinkTowards = Alignment.Center),
                    ) {
                        Text(
                            text = "Gah!",
                            style = MaterialTheme.typography.h1,
                            fontSize = (60 + 8 * MAX_KOTLIN_LIBRARY_COUNT).sp,
                        )
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
        Box(modifier = Modifier.fillMaxWidth().padding(start = 32.dp, top = 16.dp), contentAlignment = Alignment.TopStart) {
            Text(buildAnnotatedString {
                append("=> ")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                    append("Lots")
                }
            })
        }
    }
}

@Composable
private fun HowManyGroovyLibraries(answerVisible: Boolean) {
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
        Box(modifier = Modifier.fillMaxWidth().padding(start = 32.dp, top = 16.dp), contentAlignment = Alignment.TopStart) {
            Text(buildAnnotatedString {
                append("=> ")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                    append("Spock")
                }
            })
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
