@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    plugins {
         val kotlinVersion = "2.3.0"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        kotlin("plugin.power-assert") version kotlinVersion
        id("org.jetbrains.compose") version "1.10.1"
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
include(":dcnyc25:story")
include(":kc26:companion")
include(":kc26:story")
include(":kotlinconf2025:companion")
include(":kotlinconf2025:story")
include(":shared")
