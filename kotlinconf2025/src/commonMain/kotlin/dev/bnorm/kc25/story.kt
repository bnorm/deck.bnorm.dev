package dev.bnorm.kc25

import dev.bnorm.kc25.sections.Bueller
import dev.bnorm.kc25.sections.Closing
import dev.bnorm.kc25.sections.CompilerArchitecture
import dev.bnorm.kc25.sections.Dont
import dev.bnorm.kc25.sections.Title
import dev.bnorm.kc25.template.THEME_DECORATOR
import dev.bnorm.storyboard.core.Storyboard

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
        Dont()
        Closing()
        Bueller()
        Title()

        CompilerArchitecture()

        // What's a compiler plugin?
        // - Compose.
        // - Serialization.
        // - Dataframe.
        // - Power-Assert.

        // Why would you need one?
        // - Boiler plate (serialization, power-assert).
        // - Colored functions (compose).
        // - Dynamic properties (dataframe).

        // Let's build one!
        // - Generate "Builder" classes.
        // - What do we want generated?
        // - Testing setup.

        // FIR
        // - Structure and purpose.
        // - Phases.
        // - Session.
        // - Providers and Scopes.
        // - Checkers.
        // - Extensions:
        //   - FirExtensionSessionComponent.
        //   - FirDeclarationGenerationExtension.
        //   - FirStatusTransformerExtension.
        //   - FirAdditionalCheckersExtension.

        // IR

        Closing()
    }
}
