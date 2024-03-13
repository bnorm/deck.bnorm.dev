package dev.bnorm.kc24.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.LocalShowTheme
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.text.*
import kotlin.time.Duration.Companion.milliseconds

fun ShowBuilder.PowerAssertSetup() {
    section(title = { Text("Power-Assert Setup") }) {
        slide { SectionHeader() }

        GradlePlugin()
        GradleExtension()
        // TODO function extension property
        // TODO exclude source sets property
    }
}

private fun ShowBuilder.GradlePlugin() {
    slide {
        TitleAndBody {
            val state = rememberAdvancementAnimation()

            ProvideTextStyle(MaterialTheme.typography.body1.copy(fontSize = 23.sp)) {
                Column(modifier = Modifier.padding(SLIDE_PADDING)) {
                    AnimateSequence(ktsSequence, state, delay = 25.milliseconds) {
                        Text(it, modifier = Modifier.Companion.weight(0.4f))
                    }
                    AnimateSequence(groovySequence, state, delay = 19.milliseconds) {
                        GradleGroovyText(it, modifier = Modifier.weight(0.6f))
                    }
                }
            }
        }
    }
}

private fun ShowBuilder.GradleExtension() {
    slide {
        TitleAndBody {
            ProvideTextStyle(MaterialTheme.typography.body1.copy(fontSize = 23.sp)) {
                Column(modifier = Modifier.padding(SLIDE_PADDING)) {
                    // TODO this could probably be easier with some kind of animation spec builder?
                    /**
                     * buildAnimation(ktsConfigEmpty) {
                     *     onAdvance { thenLines(ktsConfigFunctionsEmpty) }
                     *     onAdvance { thenLines(ktsConfigFunctionsComplete) }
                     *     onAdvance { thenLines(ktsConfigExcludeEmpty) }
                     *     onAdvance { thenLines(ktsConfigExcludeComplete) }
                     * }
                     */
                    AnimateSequences(listOf(sequence1, sequence2, sequence3, sequence4)) {
                        Text(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun GradleGroovyText(text: String, modifier: Modifier = Modifier) {
    GroovyCodeText(text, modifier = modifier)
}

private fun styleIdentifier(
    it: String,
    codeStyle: ShowTheme.CodeStyle,
) = when (it) {
    // Properties
    "class",
    -> codeStyle.keyword

    // Annotation type
    "ExperimentalKotlinGradlePluginApi",
    -> codeStyle.annotation

    // Properties
    "functions", "excludedSourceSets",
    -> SpanStyle(color = Color(0xFFC77DBB))

    // Extension functions
    "kotlin", "version", "powerAssert",
    -> SpanStyle(color = Color(0xFF57AAF7), fontStyle = FontStyle.Italic)

    else -> null
}

private val ktsSequence: AnimationSequence<AnnotatedString>
    @Composable
    get() {
        val codeStyle = LocalShowTheme.current.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { styleIdentifier(it, codeStyle) }
        )

        return remember {
            startAnimation(
                buildString(
                    """
                        // build.gradle.kts
                        plugins {
                            kotlin("jvm") version "2.0.0"
                        }
                    """.trimIndent(),
                ),
            ).thenLines(
                buildString(
                    """
                        // build.gradle.kts
                        plugins {
                            kotlin("jvm") version "2.0.0"
                        
                        }
                    """.trimIndent(),
                ),
            ).thenLineEndDiff(
                buildString(
                    """
                        // build.gradle.kts
                        plugins {
                            kotlin("jvm") version "2.0.0"
                            kotlin("plugin.power-assert") version "2.0.0"
                        }
                    """.trimIndent(),
                ),
            )
        }
    }

private val groovySequence = startAnimation(
    """
        // build.gradle
        plugins {
            id 'org.jetbrains.kotlin.jvm' version '2.0.0'
        }
    """.trimIndent()
).thenLines(
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

private val ktsConfigEmpty =
    """
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        powerAssert {
        
        
        
        
        
        
        
        
        }
    """.trimIndent()

private val ktsConfigFunctionsEmpty =
    """
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        powerAssert {
            functions.addAll(
            
            
            )
        
        
        
        
        }
    """.trimIndent()

private val ktsConfigFunctionsComplete =
    """
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        powerAssert {
            functions.addAll(
                "kotlin.test.assertTrue",
                "kotlin.test.assertEquals",
            )
        
        
        
        
        }
    """.trimIndent()

private val ktsConfigExcludeEmpty =
    """
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        powerAssert {
            functions.addAll(
                "kotlin.test.assertTrue",
                "kotlin.test.assertEquals",
            )
            excludedSourceSets.addAll(
        
        
            )
        }
    """.trimIndent()

private val ktsConfigExcludeComplete =
    """
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        powerAssert {
            functions.addAll(
                "kotlin.test.assertTrue",
                "kotlin.test.assertEquals",
            )
            excludedSourceSets.addAll(
                "commonMain",
                "jvmMain",
            )
        }
    """.trimIndent()

val sequence1: AnimationSequence<AnnotatedString>
    @Composable
    get() {
        val codeStyle = LocalShowTheme.current.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { styleIdentifier(it, codeStyle) }
        )

        return remember {
            startAnimation(buildString(ktsConfigEmpty))
                .thenLines(buildString(ktsConfigFunctionsEmpty))
        }
    }

val sequence2: AnimationSequence<AnnotatedString>
    @Composable
    get() {
        val codeStyle = LocalShowTheme.current.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { styleIdentifier(it, codeStyle) }
        )

        return remember {
            startAnimation(buildString(ktsConfigFunctionsEmpty))
                .thenLines(buildString(ktsConfigFunctionsComplete))
        }
    }

val sequence3: AnimationSequence<AnnotatedString>
    @Composable
    get() {
        val codeStyle = LocalShowTheme.current.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { styleIdentifier(it, codeStyle) }
        )

        return remember {
            startAnimation(buildString(ktsConfigFunctionsComplete))
                .thenLines(buildString(ktsConfigExcludeEmpty))
        }
    }

val sequence4: AnimationSequence<AnnotatedString>
    @Composable
    get() {
        val codeStyle = LocalShowTheme.current.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { styleIdentifier(it, codeStyle) }
        )

        return remember {
            startAnimation(buildString(ktsConfigExcludeEmpty))
                .thenLines(buildString(ktsConfigExcludeComplete))
        }
    }
