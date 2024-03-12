import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.bnorm.power.kotlin-power-assert") version "0.13.0"
}

group = "dev.bnorm.kc24"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "show.js"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.components.resources)

                implementation("dev.bnorm.librettist:librettist-core")
                implementation("dev.bnorm.librettist:librettist-text")
            }
        }
        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(compose.desktop.uiTestJUnit4)
                implementation("com.willowtreeapps.assertk:assertk:0.28.0")
                implementation("org.apache.pdfbox:pdfbox:3.0.1")
            }
        }
    }
}

compose.experimental {
    web.application {}
}

tasks.register<Sync>("site") {
    from(tasks.named("wasmJsBrowserProductionWebpack"))
    into(rootProject.layout.buildDirectory.dir("_site/${project.name}"))
}
