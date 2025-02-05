@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        maven("https://packages.jetbrains.team/maven/p/firework/dev")
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    plugins {
        val kotlinVersion = "2.1.20-Beta2"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        kotlin("plugin.power-assert") version kotlinVersion
        id("org.jetbrains.compose") version "1.7.3"
        id("org.jetbrains.compose-hot-reload") version "1.0.0-dev.34.1"
    }

    dependencyResolutionManagement {
        repositories {
            maven("https://packages.jetbrains.team/maven/p/firework/dev")
            mavenCentral()
            google()
        }
    }
}

rootProject.name = "deck.bnorm.dev"

includeBuild("storyboard")

include(":deck.bnorm.dev")
include(":kotlinconf2024")
include(":kotlinconf2025")
include(":shared")
