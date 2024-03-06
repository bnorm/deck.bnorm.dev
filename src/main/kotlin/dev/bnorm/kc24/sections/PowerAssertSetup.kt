package dev.bnorm.kc24.sections

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.AnimateDiff
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.SlideGroupScope
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.text.KotlinCodeText
import kotlin.time.Duration.Companion.milliseconds

fun SlideGroupScope.PowerAssertSetup() {
    section(title = { Text("Power-Assert Setup") }) {
        slide { SectionHeader() }

        GradlePlugin()

        // TODO fill out extension slide for
        GradleExtension()
    }
}

private fun SlideGroupScope.GradlePlugin() {
    slide {
        val ktsValues = listOf(
            """
                // build.gradle.kts
                plugins {
                    kotlin("jvm") version "2.0.0"
                }
            """.trimIndent(),
            """
                // build.gradle.kts
                plugins {
                    kotlin("jvm") version "2.0.0"
                
                }
            """.trimIndent(),
            """
                // build.gradle.kts
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
            """.trimIndent(),
        )

        val groovyValues = listOf(
            """
                // build.gradle
                plugins {
                    id "org.jetbrains.kotlin.jvm" version "2.0.0"
                }
            """.trimIndent(),
            """
                // build.gradle
                plugins {
                    id "org.jetbrains.kotlin.jvm" version "2.0.0"
                
                }
            """.trimIndent(),
            """
                // build.gradle
                plugins {
                    id "org.jetbrains.kotlin.jvm" version "2.0.0"
                    id "org.jetbrains.kotlin.plugin.power-assert" version "2.0.0"
                }
            """.trimIndent(),
        )

        TitleAndBody {
            val state = rememberAdvancementAnimation()

            ProvideTextStyle(MaterialTheme.typography.body2) {
                AnimateDiff(ktsValues, state, charDelay = 50.milliseconds) { text ->
                    KotlinCodeText(text, identifierType = {
                        when (it) {
                            "kotlin", "version" -> SpanStyle(color = Color(0xFF57AAF7), fontStyle = FontStyle.Italic)
                            else -> null
                        }
                    })
                }
                Spacer(modifier = Modifier.height(32.dp))
                AnimateDiff(groovyValues, state, charDelay = 38.milliseconds) { text ->
                    KotlinCodeText(text)
                }
            }
        }
    }
}

private fun SlideGroupScope.GradleExtension() {
    slide {
        TitleAndBody {
            Text("Want to exclude source sets from transformation?")
        }
    }
}