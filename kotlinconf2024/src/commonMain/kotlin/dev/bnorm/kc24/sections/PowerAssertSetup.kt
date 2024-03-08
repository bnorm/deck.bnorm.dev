package dev.bnorm.kc24.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.text.*
import kotlin.time.Duration.Companion.milliseconds

fun ShowBuilder.PowerAssertSetup() {
    section(title = { Text("Power-Assert Setup") }) {
        slide { SectionHeader() }

        GradlePlugin()
    }
}

private fun ShowBuilder.GradlePlugin() {
    slide {
        TitleAndBody {
            val state = rememberAdvancementAnimation()

            ProvideTextStyle(MaterialTheme.typography.body2) {
                Column {
                    AnimateText(ktsSequence, state, delay = 25.milliseconds) {
                        GradleKtsText(it, modifier = Modifier.Companion.weight(0.4f))
                    }
                    AnimateText(groovySequence, state, delay = 19.milliseconds) {
                        GradleGroovyText(it, modifier = Modifier.weight(0.6f))
                    }
                }
            }
        }
    }
}

@Composable
private fun GradleKtsText(text: String, modifier: Modifier = Modifier) {
    KotlinCodeText(text, modifier = modifier, identifierType = {
        when (it) {
            "kotlin", "version" -> SpanStyle(
                color = Color(0xFF57AAF7),
                fontStyle = FontStyle.Italic
            )

            else -> null
        }
    })
}

@Composable
private fun GradleGroovyText(text: String, modifier: Modifier = Modifier) {
    GroovyCodeText(text, modifier = modifier)
}

private val ktsSequence = startTextAnimation(
    """
        // build.gradle.kts
        plugins {
            kotlin("jvm") version "2.0.0"
        }
    """.trimIndent(),
).thenDiff(
    """
        // build.gradle.kts
        plugins {
            kotlin("jvm") version "2.0.0"
        
        }
    """.trimIndent(),
).thenLineEndDiff(
    """
        // build.gradle.kts
        plugins {
            kotlin("jvm") version "2.0.0"
            kotlin("plugin.power-assert") version "2.0.0"
        }
    """.trimIndent(),
)

private val groovySequence = startTextAnimation(
    """
        // build.gradle
        plugins {
            id 'org.jetbrains.kotlin.jvm' version '2.0.0'
        }
    """.trimIndent()
).thenDiff(
    """
        // build.gradle
        plugins {
            id 'org.jetbrains.kotlin.jvm' version '2.0.0'
        
        }
    """.trimIndent(),
).thenLineEndDiff(
    """
        // build.gradle
        plugins {
            id 'org.jetbrains.kotlin.jvm' version '2.0.0'
            id 'org.jetbrains.kotlin.plugin.power-assert' version '2.0.0'
        }
    """.trimIndent(),
)
