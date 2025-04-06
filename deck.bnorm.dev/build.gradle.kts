plugins {
    kotlin("multiplatform")
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
                outputFileName = "deck.js"
            }
        }
    }

    sourceSets {
        jsMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.html.core)
                implementation(npm("youtube-player", "5.6.0"))
            }
        }
    }
}
