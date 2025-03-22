@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    plugins {
        val kotlinVersion = "2.1.20"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        kotlin("plugin.power-assert") version kotlinVersion
        id("org.jetbrains.compose") version "1.8.0-beta01"
        id("org.jetbrains.compose.hot-reload") version "1.0.0-alpha02"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "deck.bnorm.dev"

includeBuild("storyboard")

include(":deck.bnorm.dev")
include(":kotlinconf2024")
  include(":kotlinconf2025:companion")
include(":kotlinconf2025:story")
include(":KotlinTalks:PowerAssertEvolved")
include(":shared")
