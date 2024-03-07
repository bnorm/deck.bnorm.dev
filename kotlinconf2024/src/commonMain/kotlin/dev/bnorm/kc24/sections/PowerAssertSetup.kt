package dev.bnorm.kc24.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.text.KotlinCodeText
import kotlin.time.Duration.Companion.milliseconds

fun ShowBuilder.PowerAssertSetup() {
    section(title = { Text("Power-Assert Setup") }) {
        slide { SectionHeader() }

        GradlePlugin()
    }
}

private fun ShowBuilder.GradlePlugin() {
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
                Column {
                    AnimateDiff(ktsValues, state, charDelay = 50.milliseconds) { text ->
                        KotlinCodeText(text, modifier = Modifier.weight(0.4f), identifierType = {
                            when (it) {
                                "kotlin", "version" -> SpanStyle(
                                    color = Color(0xFF57AAF7),
                                    fontStyle = FontStyle.Italic
                                )

                                else -> null
                            }
                        })
                    }
                    AnimateDiff(groovyValues, state, charDelay = 38.milliseconds) { text ->
                        KotlinCodeText(text, modifier = Modifier.weight(0.6f))
                    }
                }
            }
        }
    }
}
