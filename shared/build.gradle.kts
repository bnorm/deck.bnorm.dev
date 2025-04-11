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
        all {
            languageSettings {
                optIn("androidx.compose.animation.core.ExperimentalTransitionApi")
                optIn("androidx.compose.animation.ExperimentalAnimationApi")
                optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.components.resources)

                api("dev.bnorm.storyboard:storyboard")
                api("dev.bnorm.storyboard:storyboard-easel")
                api("dev.bnorm.storyboard:storyboard-text")

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
                implementation("io.ktor:ktor-client-okhttp")
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
