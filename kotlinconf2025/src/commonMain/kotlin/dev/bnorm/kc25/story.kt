package dev.bnorm.kc25

import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.components.temp.BULLET_2
import dev.bnorm.kc25.components.temp.RevealSlide
import dev.bnorm.kc25.sections.Closing
import dev.bnorm.kc25.sections.Title
import dev.bnorm.kc25.sections.plugin.BuildableIntro
import dev.bnorm.kc25.sections.plugin.Repository
import dev.bnorm.kc25.sections.plugin.fir.FirExtensions
import dev.bnorm.kc25.sections.plugin.ir.IrExtensions
import dev.bnorm.kc25.template.SectionAndTitle
import dev.bnorm.kc25.template.THEME_DECORATOR
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.StoryboardBuilder

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
        decorator = THEME_DECORATOR,
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
    SectionAndTitle("Third?") {
        RevealSlide(
            "$BULLET_1 Writing Your First Kotlin Compiler Plugin.",
            "    $BULLET_2 [link to KotlinConf talk here]",
            "    $BULLET_2 Primarily JVM bytecode manipulation.",
            "$BULLET_1 Writing Your Second Kotlin Compiler Plugin.",
            "    $BULLET_2 [link to first blog post part here]",
            "    $BULLET_2 Primarily IR manipulation.",
            "$BULLET_1 Writing Your Third Kotlin Compiler Plugin.",
            "    $BULLET_2 This talk!",
            "    $BULLET_2 Primarily FIR manipulation.",
        )
    }

    SectionAndTitle("What is a compiler-plugin?") {
        // TODO should each of these show an example of what code is being generated/transformed?
        RevealSlide(
            "$BULLET_1 An extension to the Kotlin compiler to achieve some language-level feature.",
            "$BULLET_1 For example:",
            "    $BULLET_2 Serialization - Generates serializers for classes at compile-time.",
            "    $BULLET_2 Compose - Rewrites function declarations to inject tree node construction at runtime.",
            "    $BULLET_2 DataFrame - Generates synthetic properties based on schema of data under analysis.",
            "    $BULLET_2 Power-Assert - Rewrites function calls to include call-site information about parameters.",
            "$BULLET_1 All of these compiler-plugins generate or transform Kotlin code as it is being compiled.",
        )
    }

//    SectionAndTitle("Why would you need one?") {
//        // TODO this section may not be important if the previous section includes examples?
//        RevealSlide(
//            "$BULLET_1 Boiler plate reduction.",
//            "    $BULLET_2 Compiler-plugins can help write the boilerplate code for you.",
//            "    $BULLET_2 Boilerplate is time consuming and error-prone.",
//            "$BULLET_1 Colored functions.",
//            "    $BULLET_2 Compose offers a different \"color\" of function declarations.",
//            "    $BULLET_2 Allows for a different kind of behavior encapsulation, similar to suspend functions.",
//            "$BULLET_1 Dynamic properties.",
//            "    $BULLET_2 Generate synthetic properties based on some external schema.",
//            "    $BULLET_2 Achieve dynamic-typed like behavior in a static-typed language.",
//        )
//    }

    SectionAndTitle("Let's build one!") {
        // TODO summary of previous work for reference of project setup
        RevealSlide(
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
        RevealSlide(
            "$BULLET_1 Structure",
            "    $BULLET_2 FIR is a tree-based representation of the *structure* of Kotlin code.",
            "    $BULLET_2 FIR is used to resolve symbol references and types within Kotlin code.",
            "    $BULLET_2 For example, every function call needs to be resolved to a known function.",
        )
        RevealSlide(
            "$BULLET_1 Phases",
            "    $BULLET_2 Resolution is performed in a sequence of phases.",
            "    $BULLET_2 Each phase is responsible for resolving a different part of the FIR structure.",
            "    $BULLET_2 For example, the `FirResolvePhase.SUPER_TYPES` phase resolves super types of all classes.",
            "    $BULLET_2 Order is extremely important, as phases build on each other to completely resolve Kotlin code.",
            "    $BULLET_2 For example, function parameter types need to be resolved before call arguments types can be resolved.",
        )
        RevealSlide(
            "$BULLET_1 Extensions",
            "    $BULLET_2 Multiple phases have extension points which can alter their behavior.",
            "    $BULLET_2 These extensions impact how code is resolved, while the code is being resolved.",
            "    $BULLET_2 This means extensions are extremely specific to their phase and purpose.",
            "    $BULLET_2 This also means accessible FIR elements may only be partially resolved.",
        )
//        RevealSlide(
//            "$BULLET_1 Foundation",
//            "    $BULLET_2 `FirExtension` is the base class for all FIR extensions.",
//            "    $BULLET_2 ",
//            "$BULLET_1 ",
//            "    $BULLET_2 ",
//        )
//        RevealSlide(
//            "$BULLET_1 FirDeclarationGenerationExtension",
//            "    $BULLET_2 Purpose built to generating new declarations: classes, functions, properties, etc.",
//            "    $BULLET_2 Called in different phases depending on what information is currently required.",
//            "    $BULLET_2 ",
//            "$BULLET_1 ",
//            "    $BULLET_2 ",
//        )
    }

    FirExtensions()

//    SectionAndTitle("Piecemeal FIR") {
//        // TODO Generation
//        //  sections we need to talk about
//        //     1. predicates and finding declarations
//        //     2. nested class creation
//        //     3. constructor / function / property generation
//        //     4. ... ?
//        RevealSlide(
//            "[FirDeclarationGenerationExtension]",
//            "[registerPredicates()]",
//            "[val ANNOTATION_PREDICATE = annotated(ANNOTATION_FQ_NAME)]",
//            "[session.predicateBasedProvider.getSymbolsByPredicate(Piecemeal.ANNOTATION_PREDICATE)]",
//        )
//        RevealSlide(
//            "[getNestedClassifiersNames(...)]",
//            "[generateNestedClassLikeDeclaration(...)]",
//        )
//        RevealSlide(
//            "[getCallableNamesForClass(...)]",
//            "[generateConstructors(...)]",
//            "[generateFunctions(...)]",
//            "[generateProperties(...)]",
//        )
//
//        // TODO Checkers
//        RevealSlide(
//            "[FirAdditionalCheckersExtension]",
//            "[FirClassChecker]",
//            "[check(...)]",
//            "[BuildableErrors]",
//        )
//
//        // TODO Status transformer
//        //  not include?
//        RevealSlide(
//            "[BuildableFirStatusTransformerExtension]",
//            "[FirDeclarationDataKey / FirDeclaration.originalVisibility]",
//            "[checker]",
//        )
//    }

    // IR
    // - Extensions:
    //   - IrGenerationExtension

    SectionAndTitle("IR") {
        RevealSlide(
            "$BULLET_1 Semantics",
            "    $BULLET_2 IR is a tree-based representation of the *semantics* of Kotlin code.",
            "    $BULLET_2 IR helps to convert high-level concepts into low-level process.",
            "    $BULLET_2 For example, suspend functions need to be lowered into a state machine.",
        )
        RevealSlide(
            "$BULLET_1 Lowerings",
            "    $BULLET_2 Performed in phases that each reduce or optimize some part of Kotlin code.",
        )
    }

    IrExtensions()

//    SectionAndTitle("Piecemeal IR") {
//        // Implementation
//        RevealSlide(
//            "[IrGenerationExtension]",
//            "[IrElementVisitorVoid]",
//            "[irAttribute]",
//        )
//    }

    // MPP

    // Testing setup

    // Future
}
