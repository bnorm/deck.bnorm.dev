package dev.bnorm.kc25.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.components.temp.BULLET_3
import dev.bnorm.kc25.components.temp.RevealScene
import dev.bnorm.kc25.template.HeaderScaffold
import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.section
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.Summary() {
    section("Your Plugin") {
        scene(
            states = listOf(-1, 1, 3, 4),
            enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
            exitTransition = SceneExit(alignment = Alignment.CenterEnd),
        ) {
            val items = listOf(
                AnnotatedString("$BULLET_1 For a complete example, checkout the project on GitHub:"),
                buildAnnotatedString {
                    append("    $BULLET_3 ")
                    withStyle(SpanStyle(fontFamily = JetBrainsMono)) { append("bnorm/buildable") }
                },
                androidx.compose.ui.text.AnnotatedString("$BULLET_1 There's also a template available: "),
                buildAnnotatedString {
                    append("    $BULLET_3 ")
                    withStyle(SpanStyle(fontFamily = JetBrainsMono)) { append("demiurg906/kotlin-compiler-plugin-template") }
                },
                androidx.compose.ui.text.AnnotatedString("$BULLET_1 We plan to update the template project more regularly."),
            )

            HeaderScaffold { padding ->
                Column(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    RevealEach(transition.createChildTransition { it.toState() }) {
                        for (value in items) {
                            item { Text(value) }
                        }
                    }
                }
            }
        }
    }

    section("Other Plugins") {
        scene(
            states = listOf(-1, 1, 2, 4, 5, 7, 8),
            enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
            exitTransition = SceneExit(alignment = Alignment.CenterEnd),
        ) {
            val items = listOf(
                androidx.compose.ui.text.AnnotatedString("$BULLET_1 Multiplatform parameterized unit tests:"),
                buildAnnotatedString {
                    append("    $BULLET_3 ")
                    withStyle(SpanStyle(fontFamily = JetBrainsMono)) { append("cashapp/burst") }
                },
                androidx.compose.ui.text.AnnotatedString("    $BULLET_3 Example of transformation with great documentation."),
                androidx.compose.ui.text.AnnotatedString("$BULLET_1 API friendly values classes:"),
                buildAnnotatedString {
                    append("    $BULLET_3 ")
                    withStyle(SpanStyle(fontFamily = JetBrainsMono)) { append("drewhamilton/Poko") }
                },
                androidx.compose.ui.text.AnnotatedString("    $BULLET_3 Great example of transformation and analysis."),
                androidx.compose.ui.text.AnnotatedString("$BULLET_1 Compile-time, multiplatform dependency injection framework:"),
                buildAnnotatedString {
                    append("    $BULLET_3 ")
                    withStyle(SpanStyle(fontFamily = JetBrainsMono)) { append("ZacSweers/metro") }
                },
                androidx.compose.ui.text.AnnotatedString("    $BULLET_3 Complex example of almost all compiler plugin extensions."),
            )

            HeaderScaffold { padding ->
                Column(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    RevealEach(transition.createChildTransition { it.toState() }) {
                        for (value in items) {
                            item { Text(value) }
                        }
                    }
                }
            }
        }
    }

    section("Future") {
        SectionTitle(animateToHeader = true)
        RevealScene(
            "$BULLET_1 Top question: when will the compiler plugin API be stable?",
            "$BULLET_1 The existing API will never be stable.",
            "$BULLET_1 We working on (yet) another API which we plan to be stable (KT-49508).",
            "$BULLET_1 Some future KotlinConf: Writing Your Fourth Kotlin Compiler Plugin.",
        )
    }
}