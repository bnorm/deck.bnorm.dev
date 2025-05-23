@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }

    plugins {
         val kotlinVersion = "2.2.0-RC"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        kotlin("plugin.power-assert") version kotlinVersion
        id("org.jetbrains.compose") version "1.8.0"
        id("org.jetbrains.compose.hot-reload") version "1.0.0-alpha10"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

rootProject.name = "deck.bnorm.dev"

includeBuild("storyboard")

include(":deck.bnorm.dev")
include(":dcnyc25:story")
include(":kotlinconf2025:companion")
include(":kotlinconf2025:story")
include(":shared")
