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
                outputFileName = "kc25.js"
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

                api("dev.bnorm.storyboard:storyboard:0.1.0-alpha03")
                api("dev.bnorm.storyboard:storyboard-easel:0.1.0-alpha03")
                api("dev.bnorm.storyboard:storyboard-text:0.1.0-alpha03")

                implementation("dev.chrisbanes.haze:haze:1.5.2")
                implementation("io.github.alexzhirkevich:compottie:2.0.0-rc04")
            }
        }
    }
}

val extractBuildableCompilerPluginSamples = tasks.register<SampleExtraction>("samplesBuildableCompilerPluginExtract") {
    input = rootProject.layout.projectDirectory.dir("kotlinconf2025/buildable/compiler-plugin/src")
    output = layout.projectDirectory.dir("src/commonMain/composeResources/files/samples/buildable/compiler-plugin")
}

val extractBuildableRuntimeSamples = tasks.register<SampleExtraction>("extractBuildableRuntimeSamples") {
    input = rootProject.layout.projectDirectory.dir("kotlinconf2025/buildable/runtime/src/commonMain/kotlin")
    output = layout.projectDirectory.dir("src/commonMain/composeResources/files/samples/buildable/runtime")
}

tasks.named("copyNonXmlValueResourcesForCommonMain").configure {
    dependsOn(extractBuildableCompilerPluginSamples)
    dependsOn(extractBuildableRuntimeSamples)
}
