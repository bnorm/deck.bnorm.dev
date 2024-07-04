import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

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
                api(project(":kotlinconf2024"))
            }
        }
    }
}

compose {
    web {
    }
}

tasks.register<Sync>("site") {
    from(tasks.named("wasmJsBrowserProductionWebpack"))
    into(rootProject.layout.buildDirectory.dir("_site/${project.name}"))
}
