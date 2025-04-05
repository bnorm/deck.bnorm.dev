import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

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

    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.all {
                languageSettings {
                    enableLanguageFeature("ContextParameters")
                    enableLanguageFeature("WhenGuards")
                    enableLanguageFeature("MultiDollarInterpolation")

                    optIn("androidx.compose.animation.core.ExperimentalTransitionApi")
                    optIn("androidx.compose.animation.ExperimentalAnimationApi")
                    optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
                    optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
                }
            }
        }
    }
}

tasks.register<Sync>("site") {
    into(project.layout.buildDirectory.dir("_site"))

    from(project(":deck.bnorm.dev").tasks.named("wasmJsBrowserDistribution"))

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
