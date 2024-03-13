pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version "1.9.22"
        kotlin("multiplatform") version "1.9.22"
        id("org.jetbrains.compose") version "1.6.0"
        id("com.bnorm.power.kotlin-power-assert") version "0.13.0"
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
