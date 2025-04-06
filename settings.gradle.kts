@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven { setUrl("https://redirector.kotlinlang.org/maven/dev") }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        val kotlinVersion = "2.2.0-dev-12451"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        kotlin("plugin.power-assert") version kotlinVersion
        id("org.jetbrains.compose") version "1.8.0+dev2284"
        id("org.jetbrains.compose.hot-reload") version "1.0.0-alpha03"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven { setUrl("https://redirector.kotlinlang.org/maven/dev") }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "deck.bnorm.dev"

includeBuild("storyboard")

include(":deck.bnorm.dev")
include(":kotlinconf2025:companion")
include(":kotlinconf2025:story")
include(":shared")
