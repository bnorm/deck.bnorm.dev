pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("jvm") version "1.9.22"
        kotlin("multiplatform") version "1.9.22"
        id("org.jetbrains.compose") version "1.6.0"
    }

    dependencyResolutionManagement {
        repositories {
            mavenCentral()
            maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
            google()
        }
    }
}

rootProject.name = "deck.bnorm.dev"

includeBuild("librettist")

include(":kotlinconf2024")
