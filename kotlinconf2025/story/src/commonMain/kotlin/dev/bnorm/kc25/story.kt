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
import dev.bnorm.kc25.sections.write.BuildableIntro
import dev.bnorm.kc25.sections.write.Repository
import dev.bnorm.kc25.sections.write.resolve.FirExtensions
import dev.bnorm.kc25.sections.write.transform.IrExtensions
import dev.bnorm.kc25.sections.stages.Architecture
import dev.bnorm.kc25.template.SectionAndTitle
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
//    Dont()
//    Closing()
//    Bueller()
//    Title()
        Outline()
        Closing()
    }
}

private fun StoryboardBuilder.Outline() {
    section("Third?") {
        SectionTitle(animateToHeader = true)
        ThirdPlugin()
    }

//    Title(withTransition = true)

    section("Compiler-Plugin?") {
        SectionTitle(animateToHeader = true)

        // TODO need a section comparing it to KSP
        // TODO specifically talk about it being a form of meta-programming

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

        // TODO this section needs some kind of ender that helps transition into the next section

        SectionTitle(animateFromHeader = true)
    }

    Architecture()

    SectionAndTitle("Let's write one!") {
        // TODO reference template project for faster project setup
        RevealScene(
            "$BULLET_1 Let's focus on the easy task: boilerplate reduction.",
            "$BULLET_1 Generate a \"Builder\" class based on a class constructor.",
        )
        BuildableIntro()
    }

    // TODO put this at the beginning with the companion as a link instead?
    Repository()

//    SectionAndTitle("FIR") {
//        RevealScene(
//            "$BULLET_1 Extensions",
//            "    $BULLET_2 Multiple phases have extension points which can alter their behavior.",
//            "    $BULLET_2 These extensions impact how code is resolved, while the code is being resolved.",
//            "    $BULLET_2 This means extensions are extremely specific to their phase and purpose.",
//            "    $BULLET_2 This also means accessible FIR elements may only be partially resolved.",
//        )
//    }

    // TODO split into resolve and analyze?
    FirExtensions()

    // TODO something to break up between these 3 sections (resolve, analyze, and transform)?

    // TODO talk about FirSession/MPP between resolve and analyze
    //  - leads nicely into some MPP topics

    // TODO testing between analyze and transform
    //  - probably more important than covering IR anyways

    // TODO name transform?
    IrExtensions()

    // TODO Future?
}
