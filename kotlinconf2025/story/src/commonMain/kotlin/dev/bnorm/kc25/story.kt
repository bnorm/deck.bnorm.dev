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
import dev.bnorm.kc25.sections.register.Component
import dev.bnorm.kc25.sections.register.RegistrarComponentsFocus
import dev.bnorm.kc25.sections.register.Registration
import dev.bnorm.kc25.sections.stages.Architecture
import dev.bnorm.kc25.sections.write.BuildableIntro
import dev.bnorm.kc25.sections.write.analyze.Analyze
import dev.bnorm.kc25.sections.write.resolve.Resolve
import dev.bnorm.kc25.sections.write.transform.Transform
import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.kc25.template.THEME_DECORATOR
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.section

// TODO review all slides for consistent code formatting!
// TODO update all samples to 2.2.0?
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
        // TODO where to put the companion introduction?
        //  - go "back" to the second slide?
        //  - part of the title slide?
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

        // TODO revamp these bullet points
        //  - are there some visuals which could help?
        // TODO need to emphasize when you DON'T need a compiler plugin
        RevealScene(
            "$BULLET_1 A form of metaprogramming, integrated directly with the compiler.",
            "$BULLET_1 Metaprogramming is writing code that writes or manipulates code.",
            "$BULLET_1 Alternatives include KSP, reflection, or annotation processing (KAPT).",
            "$BULLET_1 Experimental and unstable, usually breaks with each new Kotlin release.",
            "    $BULLET_2 Should only be used if the benefits outweigh the drawbacks.",
        )

        // TODO Spring? (ramp into complexity?)
        // TODO Serialization
        ComposeExample()
        DataFrameExample()
        PowerAssertExample()

        section("Your Plugin") {
            // TODO your plugin here slide

            BuildableIntro()

            // TODO does this section need some kind of ender that helps transition into the next section?
        }
    }

    Architecture()

    // TODO just talked about compiler architecture, should we talk about plugin architecture?
    //  - might help get the setup part out of the way earlier
    //  - and show how people can find extensions
    //  - can highlight the extensions we'll be using and talk about them specifically

    // TODO put this at the beginning with the companion as a link instead?
    //    Repository()

    Registration()

    RegistrarComponentsFocus(Component.IrGenerationExtension, Component.FirDeclarationGenerationExtension)

    Resolve()

//    section("FirSession") {
//        // TODO talk about FirSession/MPP between resolve and analyze
//        //  - leads nicely into some MPP topics
//        // TODO is module the right term?
//        RevealScene(
//            "$BULLET_1 Holds all data and services for a single compilation unit.",
//            "$BULLET_1 Each common and platform module have their own compilation unit.",
//            "$BULLET_1 ...",
//            "$BULLET_1 ...",
//        )
//    }

    RegistrarComponentsFocus(Component.FirDeclarationGenerationExtension, Component.FirAdditionalCheckersExtension)

    Analyze()

//    section("Frontend") {
//        // TODO talk about IDE integration between analyze and transform
//        //  - good topic after completing the frontend
//        // TODO but is it worth covering in the talk itself?
//        RevealScene(
//            "$BULLET_1 Resolve and Analyze are part of the \"Frontend\" of the Kotlin compiler.",
//            "$BULLET_1 The frontend of the compiler is used by the new IntelliJ Kotlin plugin.",
//            "$BULLET_1 This means a compiler plugin is run as part of IntelliJ indexing.",
//            "    $BULLET_2 But by default this is disabled for third-party compiler plugins.",
//        )
//    }

    RegistrarComponentsFocus(Component.FirAdditionalCheckersExtension, Component.IrGenerationExtension)

    Transform()

    RegistrarComponentsFocus(Component.IrGenerationExtension, null)

    // TODO testing?
    //  - probably need to leave this to the companion :/

    // TODO future?

    section("Future") {
        RevealScene(
            "$BULLET_1 Top question: when will the compiler plugin API be stable?.",
            "$BULLET_1 The existing API will never be stable.",
            "$BULLET_1 We working on (yet) another API which we plan to be stable (KT-X).",
            "$BULLET_1 Some future KotlinConf: Writing Your Fourth Kotlin Compiler Plugin.",
        )
    }
}
