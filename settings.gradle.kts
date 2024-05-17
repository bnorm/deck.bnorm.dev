pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version "2.0.0-RC3"
        kotlin("plugin.compose") version "2.0.0-RC3"
        kotlin("plugin.power-assert") version "2.0.0-RC3"
        id("org.jetbrains.compose") version "1.6.10-rc03"
    }

    dependencyResolutionManagement {
        repositories {
            mavenCentral()
            google()
        }
    }
}

rootProject.name = "deck.bnorm.dev"

includeBuild("librettist")

include(":kotlinconf2024")
