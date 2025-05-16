import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

group = "dev.bnorm.deck"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.components.resources)
                api("org.jetbrains.compose.material:material-icons-core:1.7.3")

                api("dev.bnorm.storyboard:storyboard:0.1.0-SNAPSHOT")
                api("dev.bnorm.storyboard:storyboard-easel:0.1.0-SNAPSHOT")
                api("dev.bnorm.storyboard:storyboard-text:0.1.0-SNAPSHOT")

                api(dependencies.platform("io.ktor:ktor-bom:3.1.2"))
                api(dependencies.platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))

                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
                api("io.ktor:ktor-client-core")
                implementation("io.ktor:ktor-client-auth")
                implementation("io.ktor:ktor-client-content-negotiation")
                implementation("io.ktor:ktor-serialization-kotlinx-json")
            }
        }
        jvmMain {
            dependencies {
                api(compose.desktop.currentOs)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug")
                 // TODO KTOR-8409
                // implementation("io.ktor:ktor-client-okhttp")
                implementation("io.ktor:ktor-client-cio")
            }
        }
        wasmJsMain {
            dependencies {
                implementation("io.ktor:ktor-client-js")
            }
        }
    }
}

compose.resources {
    publicResClass = true
}
