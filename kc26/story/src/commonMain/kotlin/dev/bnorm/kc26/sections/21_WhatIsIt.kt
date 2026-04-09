package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc26.components.WIP
import dev.bnorm.kc26.sections.it.PowerAssertCallTransformation
import dev.bnorm.kc26.sections.it.PowerAssertIntroExample
import dev.bnorm.kc26.template.SectionBuilder
import dev.bnorm.kc26.template.SectionSceneScaffold
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.toValue

fun SectionBuilder.WhatIsIt() {
    // TODO Section: What's Power-Assert?
    //  - use survey data to adjust introduction?
    //  - quick introduction to what power-assert can do
    //  - quick introduction to known problems with using power-assert
    //  - quick introduction to the goals for power-assert

    nextSection("What's Power-Assert?")

    carouselScene(4) {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                @Composable
                fun MainPoint(text: String) {
                    Text(
                        "$BULLET_1 $text",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(start = 32.dp)
                    )
                }

                Text(
                    "Power-Assert is a compiler-plugin to help with testing.",
                    style = MaterialTheme.typography.body1
                )

                RevealEach(transition.createChildTransition { it.toValue() - 1 }) {
                    item { MainPoint("Shorten the feedback loop of tests.") }
                    item { MainPoint("Simplify assertion usage.") }
                    item { MainPoint("Improve usability for newcomers.") }
                }
            }
        }
    }

    PowerAssertIntroExample()

    carouselScene {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Slide showing how to configure Power-Assert with Gradle.")
                WIP()
            }
        }
    }

    PowerAssertCallTransformation()

    carouselScene(4) {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Power-Assert has some problems.")
                RevealEach(transition.createChildTransition { it.toValue() - 1 }) {
                    item { Text("    $BULLET_1 Verbose configuration that complicates onboarding.") }
                    item { Text("    $BULLET_1 Brittle function parameter requirements that confuse adopters.") }
                    item { Text("    $BULLET_1 Static diagram generation that limits tooling integration.") }
                }
            }
        }
    }
}

const val BULLET_1 = "•"
const val BULLET_2 = "◦"
const val BULLET_3 = "‣"
