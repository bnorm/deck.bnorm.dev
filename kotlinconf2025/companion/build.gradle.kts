import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
    id("org.jetbrains.compose.hot-reload")
}

group = "dev.bnorm.deck"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "kc25-companion.js"
            }
        }
    }

    sourceSets {
        all {
            languageSettings {
                enableLanguageFeature("MultiDollarInterpolation")

                optIn("androidx.compose.animation.core.ExperimentalTransitionApi")
                optIn("androidx.compose.animation.ExperimentalAnimationApi")
                optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain {
            dependencies {
                implementation(project(":shared"))
                implementation(project(":kotlinconf2025:story"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.components.resources)
            }
        }
    }
}

tasks.register<Sync>("site") {
    from(tasks.named("wasmJsBrowserDistribution"))
    into(rootProject.layout.buildDirectory.dir("_site/kc25/companion"))
}
