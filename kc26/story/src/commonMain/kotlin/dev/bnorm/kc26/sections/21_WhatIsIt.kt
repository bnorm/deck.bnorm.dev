package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bnorm.kc26.sections.it.PowerAssertCallTransformation
import dev.bnorm.kc26.sections.it.PowerAssertIntroExample
import dev.bnorm.kc26.template.SectionBuilder
import dev.bnorm.kc26.template.SectionSceneScaffold
import dev.bnorm.kc26.template.WIP
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.toValue

fun SectionBuilder.WhatIsIt() {
    // TODO Section: What's Power-Assert?
    //  - use survey data to adjust introduction?
    //  - quick introduction to what power-assert can do
    //  - quick introduction to how power-assert works
    //  - quick introduction to known problems with using power-assert
    //  - quick introduction to the goals for power-assert

    nextSection("What's Power-Assert?")

    carouselScene(7) {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                @Composable
                fun MainPoint(text: String) {
                    Text(
                        "$BULLET_1 $text",
                        style = MaterialTheme.typography.body1.copy(
                            fontStyle = FontStyle.Italic,
                        ),
                        modifier = Modifier.padding(start = 32.dp)
                    )
                }

                @Composable
                fun SubPoint(text: String) {
                    Text(
                        "$BULLET_2 $text",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 64.dp),
                    )
                }

                Text(
                    "Power-Assert is a compiler-plugin to help with testing.",
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold,
                    )
                )

                RevealEach(transition.createChildTransition { it.toValue() - 1 }) {
                    item { MainPoint("Shorten the feedback loop of tests.") }
                    item {
                        SubPoint(
                            "Reduce the time and effort required to understand tests failure by providing clear" +
                                    " feedback on assertions, eliminating the need to start a debugger.",
                        )
                    }
                    item { MainPoint("Simplify assertion usage.") }
                    item {
                        SubPoint(
                            "Provide powerful and detailed assertion feedback without requiring users to use " +
                                    "specialized assert functions, ensuring a smoother experience for all users.",
                        )
                    }
                    item { MainPoint("Enhance usability for newcomers.") }
                    item {
                        SubPoint(
                            "Make the assertion process intuitive and accessible. We'd like to minimize the learning " +
                                    "curve and make advanced features easily available without much ceremony.",
                        )
                    }
                }
            }
        }
    }

    carouselScene {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Slide showing how to configure Power-Assert with Gradle.")
                WIP()
            }
        }
    }

    PowerAssertIntroExample()

    PowerAssertCallTransformation()
}

const val BULLET_1 = "•"
const val BULLET_2 = "◦"
const val BULLET_3 = "‣"
