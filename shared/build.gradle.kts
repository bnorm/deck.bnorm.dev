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

        val ktor_version = "3.1.1"
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.components.resources)

                api("dev.bnorm.storyboard:storyboard")
                api("dev.bnorm.storyboard:storyboard-easel")
                api("dev.bnorm.storyboard:storyboard-text")

                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
                api("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-auth:$ktor_version")
                implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
            }
        }
        jvmMain {
            dependencies {
                api(compose.desktop.currentOs)
                implementation("io.ktor:ktor-client-okhttp:$ktor_version")
            }
        }
        wasmJsMain {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktor_version")
            }
        }
    }
}

compose.resources {
    publicResClass = true
}
