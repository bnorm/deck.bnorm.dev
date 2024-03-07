package dev.bnorm.kc24.sections

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
private const val MAX_KOTLIN_LIBRARY_COUNT = 12
private fun ShowBuilder.KotlinLibraries() {
    slide {
        TitleAndBody {
            Text(howManyKotlinLibraries(withLots = false))
        }
    }
    slide {
        val count by rememberAdvancementIndex(MAX_KOTLIN_LIBRARY_COUNT)
        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(howManyKotlinLibraries(withLots = false))
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
                Row {
                    Text(howManyKotlinLibraries(withLots = false))
                    AnimatedVisibility(visible) {
                        Text(buildAnnotatedString {
                            withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                                append("Lots!")
                            }
                        })
                    }
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
            Text(howManyKotlinLibraries(withLots = true))
            Spacer(modifier = Modifier.height(8.dp))
            Text(howManyGroovyLibraries(withSpock = false))
        }
    }
    slide {
        val visible by rememberAdvancementIndex(3)
        TitleAndBody {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(howManyKotlinLibraries(withLots = true))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text(howManyGroovyLibraries(withSpock = false))
                        AnimatedVisibility(visible == 2) {
                            Text(buildAnnotatedString {
                                withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                                    append("Spock!")
                                }
                            })
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AnimatedVisibility(
                        visible = visible != 2,
                        enter = expandIn(expandFrom = Alignment.Center),
                        exit = shrinkOut(shrinkTowards = Alignment.Center),
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Spock!", style = MaterialTheme.typography.h1)
                            AnimatedVisibility(visible == 1) {
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
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(howManyKotlinLibraries(withLots = true))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(howManyGroovyLibraries(withSpock = true))
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
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
        }
    }
}

// TODO markdown parser?
private fun howManyGroovyLibraries(withSpock: Boolean) = buildAnnotatedString {
    append("How many ")
    withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
        append("Groovy")
    }
    append("-specific assertion libraries? ")
    if (withSpock) {
        withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
            append("Spock!")
        }
    }
}

// TODO markdown parser?
private fun howManyKotlinLibraries(withLots: Boolean) = buildAnnotatedString {
    append("How many ")
    withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
        append("Kotlin")
    }
    append("-specific assertion libraries? ")
    if (withLots) {
        withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
            append("Lots!")
        }
    }
}
