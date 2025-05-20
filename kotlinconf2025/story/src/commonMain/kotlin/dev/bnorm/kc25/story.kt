package dev.bnorm.kc25

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
import dev.bnorm.kc25.components.temp.BULLET_2
import dev.bnorm.kc25.components.temp.BULLET_3
import dev.bnorm.kc25.components.temp.RevealScene
import dev.bnorm.kc25.sections.Closing
import dev.bnorm.kc25.sections.Title
import dev.bnorm.kc25.sections.intro.*
import dev.bnorm.kc25.sections.register.*
import dev.bnorm.kc25.sections.stages.Architecture
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.sections.stages.StageTimelineTransition
import dev.bnorm.kc25.sections.write.BuildableIntro
import dev.bnorm.kc25.sections.write.analyze.Checker
import dev.bnorm.kc25.sections.write.analyze.CheckerExtension
import dev.bnorm.kc25.sections.write.analyze.Errors
import dev.bnorm.kc25.sections.write.resolve.FirGeneration
import dev.bnorm.kc25.sections.write.transform.IrGeneration
import dev.bnorm.kc25.sections.write.transform.Visitor
import dev.bnorm.kc25.template.HeaderScaffold
import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.storyDecorator
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.*
import dev.bnorm.storyboard.toState

// TODO review all slides for consistent code formatting!
fun createStoryboard(
    decorator: SceneDecorator = storyDecorator(),
    sink: MutableList<CodeSample> = mutableListOf(),
): Storyboard {
    return Storyboard.build(
        title = "Writing Your Third Kotlin Compiler Plugin",
        description = """
            Compiler plugins have become an integral part of a developer’s experience with the Kotlin programming
            language. Areas like UI development (Jetpack Compose), backend development (Spring Boot / Kotlin
            Serialization), and even testing (Power-Assert) all leverage compiler plugins. But what is a Kotlin compiler
            plugin and what can it do? Let’s learn by writing one!

            In this talk, we’ll discuss use cases for compiler plugins and learn how they integrate with the Kotlin
            compiler. Then we’ll explore everything related to how code is represented within the Kotlin compiler,
            including how to inspect, navigate, transform, and create these representations. When we’re done, we’ll have
            written a compiler plugin from scratch which can navigate the project code, inspect annotations, generate
            boilerplate, and even report errors!
        """.trimIndent(),
        decorator = decorator,
    ) {
        Title()
        Outline(sink)
        Closing()
    }
}

private fun StoryboardBuilder.Outline(sink: MutableList<CodeSample>) {
    section("Third?") {
        SectionTitle(animateToHeader = true)
        ThirdPlugin()
    }

    section("Compiler Plugin?") {
        SectionTitle(animateToHeader = true)

        // TODO revamp these bullet points
        //  - are there some visuals which could help?
        // TODO need to emphasize when you DON'T need a compiler plugin
        // TODO really need to make this slide about KSP vs compiler plugins
        RevealScene(
            "$BULLET_1 A form of metaprogramming, integrated directly with the compiler.",
            "$BULLET_1 Metaprogramming is writing code that writes or manipulates code.",
            "$BULLET_1 Alternatives include KSP, reflection, or annotation processing (KAPT).",
            "$BULLET_1 Experimental and unstable, usually breaks with each new Kotlin release.",
            "    $BULLET_2 Should only be used if the benefits outweigh the drawbacks.",
        )

        SpringBoot(sink)
        SerializationExample(sink)
        ComposeExample(sink)
        DataFrameExample(sink)
        PowerAssertExample(sink)
    }

    Architecture()

    StageTimelineTransition()

    // TODO move IrGenerationExtension boiler plate here?
    Registration(sink)

    // TODO the text fades out/in as it slides
    RegistrarComponent(
        RegistrarComponentState(
            focus = Component.FirExtensionRegistrar,
            stages = setOf(CompilerStage.Resolve, CompilerStage.Analyze),
        ),
        RegistrarComponentState(),
        enterTransition = enter(start = DetailsEnterTransition, end = SceneEnter(alignment = Alignment.CenterEnd)),
        exitTransition = exit(start = DetailsExitTransition, end = SceneExit(alignment = Alignment.CenterEnd)),
    )

    section("Your Plugin") {
        SectionTitle(animateToHeader = true)
        BuildableIntro(sink)
    }

    RegistrarComponent(
        RegistrarComponentState(),
        RegistrarComponentState(
            focus = Component.FirDeclarationGenerationExtension,
            stages = setOf(CompilerStage.Resolve),
        ),
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    )

    section("Resolve") {
        // TODO do both name functions first?
        //  - first fucus on the name functions
        //  - next focus on the generate functions
        //  - finally, focus on the predicate function
        // TODO do code errors with squiggly lines https://github.com/saket/extended-spans
        FirGeneration(sink)
    }

    RegistrarComponent(
        RegistrarComponentState(
            focus = Component.FirDeclarationGenerationExtension,
            stages = setOf(CompilerStage.Resolve),
        ),
        RegistrarComponentState(
            focus = Component.IrGenerationExtension,
            stages = setOf(CompilerStage.Transform),
        ),
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    )

    section("Transform") {
        IrGeneration(sink)
        // TODO add side panels for generated code
        Visitor(sink)
    }

    RegistrarComponent(
        RegistrarComponentState(
            focus = Component.IrGenerationExtension,
            stages = setOf(CompilerStage.Transform),
        ),
        RegistrarComponentState(
            focus = Component.FirAdditionalCheckersExtension,
            stages = setOf(CompilerStage.Analyze),
        ),
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    )

    section("Analyze") {
        CheckerExtension(sink)
        Checker(sink)
        Errors(sink)
    }

    RegistrarComponent(
        RegistrarComponentState(
            focus = Component.FirAdditionalCheckersExtension,
            stages = setOf(CompilerStage.Analyze),
        ),
        RegistrarComponentState(),
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    )

    Summary()
}

private fun StoryboardBuilder.Summary() {
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
                AnnotatedString("$BULLET_1 There's also a template available: "),
                buildAnnotatedString {
                    append("    $BULLET_3 ")
                    withStyle(SpanStyle(fontFamily = JetBrainsMono)) { append("demiurg906/kotlin-compiler-plugin-template") }
                },
                AnnotatedString("$BULLET_1 We plan to update the template project more regularly."),
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
                AnnotatedString("$BULLET_1 Multiplatform parameterized unit tests:"),
                buildAnnotatedString {
                    append("    $BULLET_3 ")
                    withStyle(SpanStyle(fontFamily = JetBrainsMono)) { append("cashapp/burst") }
                },
                AnnotatedString("    $BULLET_3 Example of transformation with great documentation."),
                AnnotatedString("$BULLET_1 API friendly values classes:"),
                buildAnnotatedString {
                    append("    $BULLET_3 ")
                    withStyle(SpanStyle(fontFamily = JetBrainsMono)) { append("drewhamilton/Poko") }
                },
                AnnotatedString("    $BULLET_3 Great example of transformation and analysis."),
                AnnotatedString("$BULLET_1 Compile-time, multiplatform dependency injection framework:"),
                buildAnnotatedString {
                    append("    $BULLET_3 ")
                    withStyle(SpanStyle(fontFamily = JetBrainsMono)) { append("ZacSweers/metro") }
                },
                AnnotatedString("    $BULLET_3 Complex example of almost all compiler plugin extensions."),
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
