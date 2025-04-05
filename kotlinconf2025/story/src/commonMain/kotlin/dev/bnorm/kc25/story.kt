package dev.bnorm.kc25

import dev.bnorm.kc25.broadcast.BROADCAST_INDEX_DECORATOR
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.components.temp.BULLET_2
import dev.bnorm.kc25.components.temp.RevealScene
import dev.bnorm.kc25.sections.Closing
import dev.bnorm.kc25.sections.Title
import dev.bnorm.kc25.sections.existing.ComposeExample
import dev.bnorm.kc25.sections.existing.DataFrameExample
import dev.bnorm.kc25.sections.existing.PowerAssertExample
import dev.bnorm.kc25.sections.intro.ThirdPlugin
import dev.bnorm.kc25.sections.plugin.BuildableIntro
import dev.bnorm.kc25.sections.plugin.Repository
import dev.bnorm.kc25.sections.plugin.fir.FirExtensions
import dev.bnorm.kc25.sections.plugin.fir.FirTree
import dev.bnorm.kc25.sections.plugin.ir.IrExtensions
import dev.bnorm.kc25.template.SectionAndTitle
import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.kc25.template.THEME_DECORATOR
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.plus
import dev.bnorm.storyboard.easel.template.section

// TODO review all slides for consistent code formatting!
fun createStoryboard(): Storyboard {
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
        decorator = BROADCAST_INDEX_DECORATOR + THEME_DECORATOR,
    ) {
        Intro()
        Outline()
        Closing()
    }
}

private fun StoryboardBuilder.Intro() {
    Title()
//    Dont()
//    Closing()
//    Bueller()
//    Title()
}

private fun StoryboardBuilder.Outline() {
    section("Third?") {
        SectionTitle(animateToBody = true)
        ThirdPlugin()
    }

    Title(withTransition = true)

    section("Compiler-Plugin?") {
        SectionTitle(animateToBody = true)

        // TODO need a section comparing it to KSP

        RevealScene(
            "$BULLET_1 An extension to the Kotlin compiler to achieve some language-level feature.",
            "$BULLET_1 For example:",
            "    $BULLET_2 Serialization - Generates serializers for classes at compile-time.",
            "    $BULLET_2 Compose - Rewrites function declarations to inject tree node construction at runtime.",
            "    $BULLET_2 DataFrame - Generates synthetic properties based on schema of data under analysis.",
            "    $BULLET_2 Power-Assert - Rewrites function calls to include call-site information about parameters.",
            "$BULLET_1 All of these compiler-plugins generate or transform Kotlin code as it is being compiled.",
        )

        // TODO it would be cool to leave the line in place as the titles change between examples

        // TODO Serialization
        ComposeExample()
        // TODO Spring?
        DataFrameExample()
        PowerAssertExample()

        SectionTitle(animateFromBody = true)
    }

    SectionAndTitle("Let's build one!") {
        // TODO reference template project for faster project setup
        RevealScene(
            "$BULLET_1 Let's focus on the easy task: boilerplate reduction.",
            "$BULLET_1 Generate a \"Builder\" class based on a class constructor.",
        )
        BuildableIntro()
    }

    Repository()

    // FIR
    // - Structure and purpose
    // - Phases
    // - FirSession
    // - Providers and Scopes
    // - Checkers
    // - Extensions:
    //   - FirExtensionSessionComponent
    //   - FirDeclarationGenerationExtension
    //   - FirStatusTransformerExtension
    //   - FirAdditionalCheckersExtension

    SectionAndTitle("FIR") {
        RevealScene(
            "$BULLET_1 Structure",
            "    $BULLET_2 FIR is a tree-based representation of the *structure* of Kotlin code.",
            "    $BULLET_2 FIR is used to resolve symbol references and types within Kotlin code.",
            "    $BULLET_2 For example, every function call needs to be resolved to a known function.",
        )
        FirTree()
        RevealScene(
            "$BULLET_1 Phases",
            "    $BULLET_2 Resolution is performed in a sequence of phases.",
            "    $BULLET_2 Each phase is responsible for resolving a different part of the FIR structure.",
            "    $BULLET_2 For example, the `FirResolvePhase.SUPER_TYPES` phase resolves super types of all classes.",
            "    $BULLET_2 Order is extremely important, as phases build on each other to completely resolve Kotlin code.",
            "    $BULLET_2 For example, function parameter types need to be resolved before call arguments types can be resolved.",
        )
        RevealScene(
            "$BULLET_1 Extensions",
            "    $BULLET_2 Multiple phases have extension points which can alter their behavior.",
            "    $BULLET_2 These extensions impact how code is resolved, while the code is being resolved.",
            "    $BULLET_2 This means extensions are extremely specific to their phase and purpose.",
            "    $BULLET_2 This also means accessible FIR elements may only be partially resolved.",
        )
//        RevealScene(
//            "$BULLET_1 Foundation",
//            "    $BULLET_2 `FirExtension` is the base class for all FIR extensions.",
//            "    $BULLET_2 ",
//            "$BULLET_1 ",
//            "    $BULLET_2 ",
//        )
//        RevealScene(
//            "$BULLET_1 FirDeclarationGenerationExtension",
//            "    $BULLET_2 Purpose built to generating new declarations: classes, functions, properties, etc.",
//            "    $BULLET_2 Called in different phases depending on what information is currently required.",
//            "    $BULLET_2 ",
//            "$BULLET_1 ",
//            "    $BULLET_2 ",
//        )
    }

    FirExtensions()

    // IR
    // - Extensions:
    //   - IrGenerationExtension

    SectionAndTitle("IR") {
        RevealScene(
            "$BULLET_1 Semantics",
            "    $BULLET_2 IR is a tree-based representation of the *semantics* of Kotlin code.",
            "    $BULLET_2 IR helps to convert high-level concepts into low-level process.",
            "    $BULLET_2 For example, suspend functions need to be lowered into a state machine.",
        )
        RevealScene(
            "$BULLET_1 Lowerings",
            "    $BULLET_2 Performed in phases that each reduce or optimize some part of Kotlin code.",
        )
    }

    IrExtensions()

    // TODO MPP?
    // TODO Testing setup?
    // TODO Future?
}
