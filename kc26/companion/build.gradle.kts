plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

group = "dev.bnorm.deck"
version = "1.0-SNAPSHOT"

kotlin {
    js {
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "kc26-companion.js"
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

        jsMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.html.core)
            }
        }
    }
}
