package dev.bnorm.kc25

import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.components.temp.BULLET_2
import dev.bnorm.kc25.components.temp.RevealScene
import dev.bnorm.kc25.sections.Closing
import dev.bnorm.kc25.sections.Title
import dev.bnorm.kc25.sections.intro.*
import dev.bnorm.kc25.sections.register.Component
import dev.bnorm.kc25.sections.register.RegistrarComponent
import dev.bnorm.kc25.sections.register.RegistrarComponentState
import dev.bnorm.kc25.sections.register.Registration
import dev.bnorm.kc25.sections.stages.Architecture
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.sections.write.BuildableIntro
import dev.bnorm.kc25.sections.write.analyze.Checker
import dev.bnorm.kc25.sections.write.analyze.CheckerExtension
import dev.bnorm.kc25.sections.write.analyze.Errors
import dev.bnorm.kc25.sections.write.resolve.FirGeneration
import dev.bnorm.kc25.sections.write.transform.IrGeneration
import dev.bnorm.kc25.sections.write.transform.Visitor
import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.storyDecorator
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.section

// TODO review all slides for consistent code formatting!
// TODO update all samples to 2.2.0?
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

        // TODO Move section to just after registration slide?
        section("Your Plugin") {
            BuildableIntro(sink)
        }
    }

    // TODO the stages should be more front and center
    //  - push them to the outside?
    // TODO are there other visuals we want to add here?
    // TODO talk about frontend vs backend
    //  - put boxes around the frontend and the backend
    // TODO improve details about analyze stage
    // TODO improve details about parse stage
    //  - also show a UML-like tree of element linking?
    //  - save for resolve phase?
    // TODO improve details about resolve stage
    // TODO can we show an example of why types are resolved in a specific order?
    //  - super-type
    //  - return type and parameter types
    //  - local variables
    // TODO improve details about transform stage
    // TODO improve details about generate stage
    //  - should i actually include klib?
    Architecture()

    // TODO similar to stages, zoom in and out of components to see changes which need to be made
    //  - include a header and bullet points to describe how the extension can be used?
    // TODO what to put in the backend versus the frontend
    //  - put boxes around the frontend and the backend
    // TODO example showing template project and how
    // TODO move IrGenerationExtension boiler plate here?
    // TODO start with the factory version in FirExtensionRegistrar and transform into the method reference version?
    // TODO is there a better way to do detail transitions?
    //  - maybe similar to FIR tree where it's part of the diagram?
    Registration(sink)

    RegistrarComponent(
        RegistrarComponentState(
            focus = Component.FirExtensionRegistrar,
            stages = setOf(CompilerStage.Resolve, CompilerStage.Analyze),
        ),
        RegistrarComponentState(
            focus = Component.FirDeclarationGenerationExtension,
            stages = setOf(CompilerStage.Resolve),
        ),
    )

    section("Resolve") {
        // TODO show all the annotation predicate types and what each match?
        // TODO show more details about predicate based provider?
        // TODO show more details about FirScope?
        // TODO do both name functions first?
        //  - first fucus on the name functions
        //  - next focus on the generate functions
        //  - finally, focus on the predicate function
        // TODO walk through the body of callable names, generate constructor, properties, and functions
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
    )

    section("Transform") {
        IrGeneration(sink)
        // TODO still talk about IrAttribute?
        // TODO add side panels for generated code
        // TODO focus on parts of function body
        // TODO side panel for irAttribute
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
    )

    section("Analyze") {
        // TODO in what order should extension, checker and errors scenes be?
        // TODO show example of other checker configuration?
        CheckerExtension(sink)
        Checker(sink)
        // TODO show example of other multi-parameter errors?
        Errors(sink)
        // TODO could transition into IDE topics after analyze?
    }

    RegistrarComponent(
        RegistrarComponentState(
            focus = Component.FirAdditionalCheckersExtension,
            stages = setOf(CompilerStage.Analyze),
        ),
        RegistrarComponentState(),
    )

    section("Your Plugin") {
        // TODO new slide
        //  - explicitly mention the template project for people to use and write their own
        //  - show buildable repository as a sample with all this code

        // TODO testing?
        //  - probably need to leave this to the companion :/
    }

    // TODO add KT ticket
    section("Future") {
        RevealScene(
            "$BULLET_1 Top question: when will the compiler plugin API be stable?",
            "$BULLET_1 The existing API will never be stable.",
            "$BULLET_1 We working on (yet) another API which we plan to be stable (KT-X).",
            "$BULLET_1 Some future KotlinConf: Writing Your Fourth Kotlin Compiler Plugin.",
        )
    }
}
