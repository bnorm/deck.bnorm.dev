@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version "2.0.0"
        kotlin("plugin.compose") version "2.0.0"
        kotlin("plugin.power-assert") version "2.0.0"
        id("org.jetbrains.compose") version "1.7.0-alpha01"
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

include(":deck.bnorm.dev")
include(":kotlinconf2024")
