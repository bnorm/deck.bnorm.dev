import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
    kotlin("plugin.compose") apply false
    id("org.jetbrains.compose") apply false
    id("org.jetbrains.compose.hot-reload") apply false
}

allprojects {
    plugins.withId("org.jetbrains.compose") {
        extensions.configure<ComposeExtension> {
            extensions.configure<ResourcesExtension> {
                publicResClass = true
            }
        }

        extensions.configure<ComposeCompilerGradlePluginExtension> {
            featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
        }
    }
}

tasks.register<Sync>("site") {
    into(project.layout.buildDirectory.dir("_site"))

    from(project(":deck.bnorm.dev").tasks.named("wasmJsBrowserDistribution"))

    into("PowerAssertEvolved") {
        from(project(":KotlinTalks:PowerAssertEvolved").tasks.named("wasmJsBrowserDistribution"))
    }

    into("kotlinconf2024") {
        from(project(":kotlinconf2024").tasks.named("wasmJsBrowserDistribution"))
    }

    into("kc25") {
        from(project(":kotlinconf2025:story").tasks.named("wasmJsBrowserDistribution"))
        into("companion") {
            from(project(":kotlinconf2025:companion").tasks.named("wasmJsBrowserDistribution"))
        }
    }
}
