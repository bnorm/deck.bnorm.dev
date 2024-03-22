package dev.bnorm.kc24.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleAndBody
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.animation.AnimationSequence
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.section
import dev.bnorm.librettist.text.GroovyCodeText
import dev.bnorm.librettist.text.buildGradleKtsCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import dev.bnorm.librettist.text.thenLines

fun ShowBuilder.PowerAssertSetup() {
    section(title = { Text("Power-Assert Setup") }) {
        SectionHeader()

        GradlePlugin()
        GradleExtension()
        // TODO function extension property
        // TODO exclude source sets property
    }
}

private fun ShowBuilder.GradlePlugin() {
    slide(advancements = 2) {
        TitleAndBody {
            val state = transition.createChildTransition { it == 1 }

            ProvideTextStyle(MaterialTheme.typography.body2) {
                Column(modifier = Modifier.padding(SLIDE_PADDING)) {
                    val ktsValues = ktsSequence
                    val groovyValues = groovySequence

                    val ktsText by state.animateList(ktsValues) { if (it) ktsValues.lastIndex else 0 }
                    Text(ktsText, modifier = Modifier.Companion.weight(0.4f))

                    val groovyText by state.animateList(groovyValues) { if (it) groovyValues.lastIndex else 0 }
                    GradleGroovyText(groovyText, modifier = Modifier.weight(0.6f))
                }
            }
        }
    }
}

private fun ShowBuilder.GradleExtension() {
    slide(advancements = 5) {
        TitleAndBody {
            ProvideTextStyle(MaterialTheme.typography.body2) {
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
                    val text = transition.createChildTransition {
                        when (it) {
                            0 -> sequence1.start
                            1 -> sequence1.end
                            2 -> sequence2.end
                            3 -> sequence3.end
                            4 -> sequence4.end
                            else -> error("!")
                        }
                    }
                    Text(text.currentState)
                }
            }
        }
    }
}

@Composable
private fun GradleGroovyText(text: String, modifier: Modifier = Modifier) {
    GroovyCodeText(text, modifier = modifier)
}

private fun String.toStyle(codeStyle: Highlighting) = when (this) {
    "class" -> codeStyle.keyword
    "ExperimentalKotlinGradlePluginApi" -> codeStyle.annotation
    "functions", "excludedSourceSets" -> codeStyle.property
    "kotlin", "version", "powerAssert" -> codeStyle.extensionFunctionCall
    else -> null
}

private val ktsSequence: List<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { it.toStyle(codeStyle) }
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
            ).sequence.toList()
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
).sequence.toList()

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
        val codeStyle = ShowTheme.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { it.toStyle(codeStyle) }
        )

        return remember {
            startAnimation(buildString(ktsConfigEmpty))
                .thenLines(buildString(ktsConfigFunctionsEmpty))
        }
    }

val sequence2: AnimationSequence<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { it.toStyle(codeStyle) }
        )

        return remember {
            startAnimation(buildString(ktsConfigFunctionsEmpty))
                .thenLines(buildString(ktsConfigFunctionsComplete))
        }
    }

val sequence3: AnimationSequence<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { it.toStyle(codeStyle) }
        )

        return remember {
            startAnimation(buildString(ktsConfigFunctionsComplete))
                .thenLines(buildString(ktsConfigExcludeEmpty))
        }
    }

val sequence4: AnimationSequence<AnnotatedString>
    @Composable
    get() {
        val codeStyle = ShowTheme.code
        fun buildString(text: String) = buildGradleKtsCodeString(
            text = text,
            codeStyle = codeStyle,
            identifierType = { it.toStyle(codeStyle) }
        )

        return remember {
            startAnimation(buildString(ktsConfigExcludeEmpty))
                .thenLines(buildString(ktsConfigExcludeComplete))
        }
    }
