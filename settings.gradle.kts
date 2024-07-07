@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
    }

    plugins {
        val kotlinVersion = "2.0.10-RC-510"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        kotlin("plugin.power-assert") version kotlinVersion
        id("org.jetbrains.compose") version "1.7.0-alpha01"
    }

    dependencyResolutionManagement {
        repositories {
            mavenCentral()
            google()
            maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
        }
    }
}

rootProject.name = "deck.bnorm.dev"

includeBuild("librettist")

include(":deck.bnorm.dev")
include(":kotlinconf2024")
