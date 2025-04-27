package dev.bnorm.kc25

import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.components.temp.BULLET_2
import dev.bnorm.kc25.components.temp.RevealScene
import dev.bnorm.kc25.sections.Closing
import dev.bnorm.kc25.sections.Title
import dev.bnorm.kc25.sections.intro.*
import dev.bnorm.kc25.sections.register.Component
import dev.bnorm.kc25.sections.register.RegistrarComponentsFocus
import dev.bnorm.kc25.sections.register.Registration
import dev.bnorm.kc25.sections.stages.Architecture
import dev.bnorm.kc25.sections.write.BuildableIntro
import dev.bnorm.kc25.sections.write.analyze.Analyze
import dev.bnorm.kc25.sections.write.resolve.Resolve
import dev.bnorm.kc25.sections.write.transform.Transform
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

    Architecture()

    Registration(sink)

    RegistrarComponentsFocus(Component.IrGenerationExtension, Component.FirDeclarationGenerationExtension)

    Resolve(sink)

    RegistrarComponentsFocus(Component.FirDeclarationGenerationExtension, Component.FirAdditionalCheckersExtension)

    // TODO do transform stage first and go back to analyze
    //  - skip checker to focus on code generation
    //  - can talk about why we generate bodies in IR
    //    - move "performant" in the case of errors
    //    - everything is resolved
    // TODO could transition into IDE topics after analyze?
    Analyze(sink)

    RegistrarComponentsFocus(Component.FirAdditionalCheckersExtension, Component.IrGenerationExtension)

    Transform(sink)

    RegistrarComponentsFocus(Component.IrGenerationExtension, null)

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
