package dev.bnorm.kc25

import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.components.temp.BULLET_2
import dev.bnorm.kc25.components.temp.RevealScene
import dev.bnorm.kc25.sections.Closing
import dev.bnorm.kc25.sections.Title
import dev.bnorm.kc25.sections.intro.ComposeExample
import dev.bnorm.kc25.sections.intro.DataFrameExample
import dev.bnorm.kc25.sections.intro.PowerAssertExample
import dev.bnorm.kc25.sections.intro.ThirdPlugin
import dev.bnorm.kc25.sections.stages.Architecture
import dev.bnorm.kc25.sections.write.*
import dev.bnorm.kc25.sections.write.analyze.Checkers
import dev.bnorm.kc25.sections.write.resolve.Generation
import dev.bnorm.kc25.sections.write.transform.IrExtensions
import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.kc25.template.THEME_DECORATOR
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.StoryboardBuilder
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
        decorator = THEME_DECORATOR,
    ) {
        Title()
        Outline()
        Closing()
    }
}

private fun StoryboardBuilder.Outline() {
    section("Third?") {
        SectionTitle(animateToHeader = true)
        ThirdPlugin()
    }

    section("Compiler Plugin?") {
        SectionTitle(animateToHeader = true)

        RevealScene(
            "$BULLET_1 A form of meta-programming, integrated directly with the compiler.",
            "$BULLET_1 Meta-programming is about write code that writes or manipulates code.",
            "$BULLET_1 Alternatives include KSP, reflection, or annotation processing (KAPT).",
            "$BULLET_1 Experimental and unstable, usually breaks with each new Kotlin release.",
            "$BULLET_1 Should only be used if the benefits outweigh the drawbacks.",
        )

        // TODO Serialization
        ComposeExample()
        // TODO Spring?
        DataFrameExample()
        PowerAssertExample()

        // TODO this section needs some kind of ender that helps transition into the next section
    }

    Architecture()

    section("Let's write one!") {
        SectionTitle(animateToHeader = true)

        // TODO reference template project for faster project setup
        RevealScene(
            "$BULLET_1 Let's focus on the easy task: boilerplate reduction.",
            "$BULLET_1 Generate a \"Builder\" class based on a class constructor.",
        )
        BuildableIntro()
    }

    // TODO put this at the beginning with the companion as a link instead?
    //    Repository()

    section("Setup") {
        PluginRegistrar(endExclusive = REGISTRATION_FRONTEND_END)
        FirRegistrar(endExclusive = FIR_REGISTRATION_RESOLVE_END)

        // TODO need a better transition between header and stages
    }

    // TODO show all the annotation predicate types and what each match?
    // TODO show more details about predicate based provider?
    // TODO show more details about FirScope?
    Generation()

    section("FirSession") {
        // TODO talk about FirSession/MPP between resolve and analyze
        //  - leads nicely into some MPP topics
        // TODO is module the right term?
        RevealScene(
            "$BULLET_1 Holds all data and services for a single compilation unit.",
            "$BULLET_1 Each common and platform module have their own compilation unit.",
            "$BULLET_1 ...",
            "$BULLET_1 ...",
        )
    }

    section("Setup") {
        FirRegistrar(start = FIR_REGISTRATION_RESOLVE_END)
    }
    Checkers()

    section("Frontend") {
        // TODO talk about IDE integration between analyze and transform
        //  - good topic after completing the frontend
        // TODO but is it worth covering in the talk itself?
        RevealScene(
            "$BULLET_1 Resolve and Analyze are part of the \"Frontend\" of the Kotlin compiler.",
            "$BULLET_1 The frontend of the compiler is used by the new IntelliJ Kotlin plugin.",
            "$BULLET_1 This means a compiler plugin is run as part of IntelliJ indexing.",
            "    $BULLET_2 But by default this is disabled for third-party compiler plugins.",
        )
    }

    section("Setup") { PluginRegistrar(start = REGISTRATION_FRONTEND_END) }
    IrExtensions()

    // TODO testing?
    // TODO future?
}
