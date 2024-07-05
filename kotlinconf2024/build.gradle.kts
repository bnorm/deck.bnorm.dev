import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    kotlin("plugin.power-assert")
    id("org.jetbrains.compose")
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
                outputFileName = "kotlinconf2024.js"
            }
        }
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
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                api(compose.components.resources)

                api("dev.bnorm.librettist:librettist-core")
                api("dev.bnorm.librettist:librettist-text")
            }
        }
        jvmMain {
            dependencies {
                api(compose.desktop.currentOs)
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

compose.resources {
    publicResClass = true
}

compose {
    web {
    }
}

tasks.register<Sync>("site") {
    from(tasks.named("wasmJsBrowserDistribution"))
    into(rootProject.layout.buildDirectory.dir("_site/${project.name}"))
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
powerAssert {
    functions = listOf(
        "kotlin.require",
        "kotlin.test.assertTrue",
        "kotlin.test.assertNotNull",
        "kotlin.test.assertEquals",
        "dev.bnorm.assert.AssertScope.assert",
        "dev.bnorm.assert.assert",
    )
}
