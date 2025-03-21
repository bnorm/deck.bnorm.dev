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
                outputFileName = "kotlinconf2025.js"
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

        val ktor_version = "3.0.0-beta-2"
        commonMain {
            dependencies {
                implementation(project(":shared"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                api(compose.components.resources)

                api("dev.bnorm.storyboard:storyboard-core")
                api("dev.bnorm.storyboard:storyboard-easel")
                api("dev.bnorm.storyboard:storyboard-text")

                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

                implementation("dev.chrisbanes.haze:haze:1.3.1")
                implementation("dev.chrisbanes.haze:haze-materials:1.3.1")
            }
        }
        jvmMain {
            dependencies {
                api(compose.desktop.currentOs)
                implementation("io.ktor:ktor-client-okhttp:$ktor_version")
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(compose.desktop.uiTestJUnit4)
            }
        }
        wasmJsMain {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktor_version")
            }
        }
    }
}

tasks.register<Sync>("site") {
    from(tasks.named("wasmJsBrowserDistribution"))
    into(rootProject.layout.buildDirectory.dir("_site/${project.name}"))
}

val extractBuildableCompilerPluginSamples = tasks.register<SampleExtraction>("samplesBuildableCompilerPluginExtract") {
    input = rootProject.layout.projectDirectory.dir("kotlinconf2025/buildable/compiler-plugin/src/main/kotlin")
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
