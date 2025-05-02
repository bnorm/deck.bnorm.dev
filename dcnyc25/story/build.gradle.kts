import dev.bnorm.gradle.sample.extraction.SampleExtraction
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
                outputFileName = "dcnyc25.js"
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

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                api(compose.components.resources)

                api("dev.bnorm.storyboard:storyboard")
                api("dev.bnorm.storyboard:storyboard-easel")
                api("dev.bnorm.storyboard:storyboard-text")

                implementation("dev.chrisbanes.haze:haze:1.5.2")
                implementation("io.github.alexzhirkevich:compottie:2.0.0-rc04")
            }
        }
    }
}
