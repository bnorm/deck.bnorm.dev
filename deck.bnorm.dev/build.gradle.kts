import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

group = "dev.bnorm.deck"
version = "1.0-SNAPSHOT"

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "deck.js"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared"))
                implementation(project(":kotlinconf2024"))
            }
        }
        wasmJsMain {
            dependencies {
                implementation(npm("youtube-player", "5.6.0"))
            }
        }
    }
}

tasks.register<Sync>("site") {
    from(tasks.named("wasmJsBrowserDistribution"))
    into(rootProject.layout.buildDirectory.dir("_site"))
}
