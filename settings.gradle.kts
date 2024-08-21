@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven { setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }

    plugins {
        val kotlinVersion = "2.0.20-Beta2"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        kotlin("plugin.power-assert") version kotlinVersion
        id("org.jetbrains.compose") version "1.7.0-dev1756"
    }

    dependencyResolutionManagement {
        repositories {
            mavenCentral()
            google()
            maven { setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        }
    }
}

rootProject.name = "deck.bnorm.dev"

includeBuild("storyboard")

include(":deck.bnorm.dev")
include(":kotlinconf2024")
include(":kotlinconf2025")
include(":shared")
