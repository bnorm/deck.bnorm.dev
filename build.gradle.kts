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
