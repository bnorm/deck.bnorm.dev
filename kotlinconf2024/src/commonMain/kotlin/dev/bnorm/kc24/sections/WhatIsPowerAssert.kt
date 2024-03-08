package dev.bnorm.kc24.sections

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.rememberAdvancementIndex

fun ShowBuilder.WhatIsPowerAssert() {
    slide { SectionHeader(animateToBody = false) { Text("I would assert...") } }

    // TODO instead of striking and going to good assertions...
    //  1. describe that Power-Assert produces good assertion output,
    //  2. describe the characteristics of good assertion output,
    //  3. then go into the next section.
    PowerAssertIntro()

    // TODO analyze good assertion problems
    //  1. lots of assert*() functions ( and that's only kotlin.test )
    //  2. confusing parameter ( what's expect, what's actual, where's the message go, etc )
    //  3. requires a custom message if condition is derived from something complex ( easy to forget until something fails )
}

fun ShowBuilder.PowerAssertIntro() {
    section(title = { Text(introTitle(strike = false)) }) {
        slide { SectionHeader() }

        slide {
            val index by rememberAdvancementIndex(4)
            TitleAndBody(title = {
                Text(introTitle(strike = index > 2))
            }) {
                when (index) {
                    0 -> Text("● What is Power-Assert?")

                    1 -> {
                        Text(buildAnnotatedString {
                            append("● ")
                            withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                                append("What is Power-Assert?")
                            }
                        })
                    }

                    else -> {
                        Text(buildAnnotatedString {
                            append("● ")
                            withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                                append("What is Power-Assert?")
                            }
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("● What is a good test assertion?")
                    }
                }
            }
        }
    }
}

private fun introTitle(strike: Boolean) = buildAnnotatedString {
    if (strike) {
        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
            append("Power-Assert!")
        }
        append(" Good Assertions")
    } else {
        append("Power-Assert!")
    }
}
