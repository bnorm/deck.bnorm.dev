package dev.bnorm.kc24.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.animation.then
import dev.bnorm.librettist.rememberHighlighted
import dev.bnorm.librettist.text.buildGradleKtsCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

enum class GradleText {
    Initial {
        override fun buildText(highlighting: Highlighting): AnnotatedString {
            return """
                plugins {
                    kotlin("jvm") version "2.0.0"
                }
            """.trimIndent().toKts(highlighting)
        }
    },

    AddPlugin {
        override fun buildText(highlighting: Highlighting): AnnotatedString {
            return """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
            """.trimIndent().toKts(highlighting)
        }
    },

    AddConfig {
        override fun buildText(highlighting: Highlighting): AnnotatedString {
            return """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
                
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                powerAssert {
                }
            """.trimIndent().toKts(highlighting)
        }
    },

    AddAssertTrue {
        override fun buildText(highlighting: Highlighting): AnnotatedString {
            return """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
                
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                powerAssert {
                    functions = listOf(
                        "kotlin.test.assertTrue",
                    )
                }
            """.trimIndent().toKts(highlighting)
        }
    },

    AddRequire {
        override fun buildText(highlighting: Highlighting): AnnotatedString {
            return """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
                
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                powerAssert {
                    functions = listOf(
                        "kotlin.require",
                        "kotlin.test.assertTrue",
                    )
                }
            """.trimIndent().toKts(highlighting)
        }
    },

    AddSourceSet {
        override fun buildText(highlighting: Highlighting): AnnotatedString {
            return """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
                
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                powerAssert {
                    functions = listOf(
                        "kotlin.require",
                        "kotlin.test.assertTrue",
                    )
                    includedSourceSets = listOf("main", "test")
                }
            """.trimIndent().toKts(highlighting)
        }
    },

    AddAssertEquals {
        override fun buildText(highlighting: Highlighting): AnnotatedString {
            return """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
                
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                powerAssert {
                    functions = listOf(
                        "kotlin.require",
                        "kotlin.test.assertTrue",
                        "kotlin.test.assertEquals",
                    )
                    includedSourceSets = listOf("main", "test")
                }
            """.trimIndent().toKts(highlighting)
        }
    },

    AddAssertNotNull {
        override fun buildText(highlighting: Highlighting): AnnotatedString {
            return """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
                
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                powerAssert {
                    functions = listOf(
                        "kotlin.require",
                        "kotlin.test.assertTrue",
                        "kotlin.test.assertEquals",
                        "kotlin.test.assertNotNull",
                    )
                    includedSourceSets = listOf("main", "test")
                }
            """.trimIndent().toKts(highlighting)
        }
    },

    AddAssertSoftly {
        override fun buildText(highlighting: Highlighting): AnnotatedString {
            return """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
                
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                powerAssert {
                    functions = listOf(
                        "kotlin.require",
                        "kotlin.test.assertTrue",
                        "kotlin.test.assertEquals",
                        "kotlin.test.assertNotNull",
                        "example.AssertScope.assert",
                    )
                    includedSourceSets = listOf("main", "test")
                }
            """.trimIndent().toKts(highlighting)
        }
    },

    ;

    val text: AnnotatedString
        @Composable get() = rememberHighlighted("GradleText::$name") { buildText(it) }

    abstract fun buildText(highlighting: Highlighting): AnnotatedString
}

@Composable
fun GradleText.animateTo(other: GradleText): ImmutableList<AnnotatedString> {
    return rememberHighlighted("GradleText.animateTo::${name}_${other.name}") { highlighting ->
        val sequence = when {
            this == other -> persistentListOf(this.buildText(highlighting))
            this < other -> animateTo(this, other, highlighting)
            this > other -> animateTo(other, this, highlighting).reversed()
            else -> error("!")
        }
        sequence.toImmutableList()
    }
}

fun GradleText.animateTo(other: GradleText, highlighting: Highlighting): ImmutableList<AnnotatedString> {
    val sequence = when {
        this == other -> persistentListOf(this.buildText(highlighting))
        this < other -> animateTo(start = this, end = other, highlighting)
        this > other -> animateTo(start = other, end = this, highlighting).reversed()
        else -> error("!")
    }
    return sequence.toImmutableList()
}

private fun animateTo(
    start: GradleText,
    end: GradleText,
    highlighting: Highlighting,
): List<AnnotatedString> {
    require(start.ordinal < end.ordinal)

    val entries = GradleText.entries.subList(start.ordinal, end.ordinal)
    val list = mutableListOf<AnnotatedString>()
    for (entry in entries) {
        val sequence = when (entry) {
            GradleText.Initial -> build_Initial_To_AddPlugin(highlighting)
            GradleText.AddPlugin -> build_AddPlugin_To_AddConfig(highlighting)
            GradleText.AddConfig -> build_AddConfig_To_AddAssertTrue(highlighting)
            GradleText.AddAssertTrue -> build_AddAssertTrue_To_AddRequire(highlighting)
            GradleText.AddRequire -> build_AddRequire_To_AddSourceSet(highlighting)
            GradleText.AddSourceSet -> build_AddSourceSet_To_AddAssertEquals(highlighting)
            GradleText.AddAssertEquals -> build_AddAssertEquals_To_AddAssertNotNull(highlighting)
            GradleText.AddAssertNotNull -> build_AddAssertNotNull_To_AddAssertSoftly(highlighting)
            GradleText.AddAssertSoftly -> error("!")
        }
        list.addAll(sequence.subList(0, sequence.lastIndex))
    }
    list.add(end.buildText(highlighting))

    val maxLines = list.maxOf { it.text.count { it == '\n' } }
    return list.map {
        val numLines = it.text.count { it == '\n' }
        if (numLines < maxLines) {
            it + AnnotatedString("\n".repeat(maxLines - numLines))
        } else {
            it
        }
    }
}

private fun String.toStyle(codeStyle: Highlighting): SpanStyle? {
    return when (this) {
        "class" -> codeStyle.keyword
        "ExperimentalKotlinGradlePluginApi" -> codeStyle.annotation
        "functions", "includedSourceSets" -> codeStyle.property
        "kotlin", "version", "powerAssert" -> codeStyle.extensionFunctionCall
        else -> null
    }
}

private fun String.toKts(highlighting: Highlighting): AnnotatedString {
    // TODO pulling from ShowTheme means this would need to be @Composable
    //  - text has to get rebuilt for every new composable...
    //  - ...or does it? can we build at the top and pass down somehow?
    return buildGradleKtsCodeString(
        text = this,
        codeStyle = highlighting,
        identifierType = { it.toStyle(highlighting) }
    )
}

private fun build_Initial_To_AddPlugin(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    return startAnimation(
        GradleText.Initial.buildText(highlighting)
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
            
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        GradleText.AddPlugin.buildText(highlighting)
    ).toList()
}

private fun build_AddPlugin_To_AddConfig(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    return startAnimation(
        GradleText.AddPlugin.buildText(highlighting) + AnnotatedString("\n\n\n\n")
    ).thenLineEndDiff(
        GradleText.AddConfig.buildText(highlighting)
    ).toList()
}

private fun build_AddConfig_To_AddAssertTrue(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    return startAnimation(
        GradleText.AddConfig.buildText(highlighting)
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
            
            }
        """.trimIndent().toKts(highlighting)
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
            
            
            }
        """.trimIndent().toKts(highlighting)
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
            
            
            
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        GradleText.AddAssertTrue.buildText(highlighting)
    ).toList()
}

private fun build_AddAssertTrue_To_AddRequire(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    return startAnimation(
        GradleText.AddAssertTrue.buildText(highlighting)
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
                functions = listOf(
                    
                    "kotlin.test.assertTrue",   
                )
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        GradleText.AddRequire.buildText(highlighting)
    ).toList()
}

private fun build_AddRequire_To_AddSourceSet(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    return startAnimation(
        GradleText.AddRequire.buildText(highlighting)
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
                functions = listOf(
                    "kotlin.require",
                    "kotlin.test.assertTrue",
                )
                
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        GradleText.AddSourceSet.buildText(highlighting)
    ).toList()
}

private fun build_AddSourceSet_To_AddAssertEquals(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    return startAnimation(
        GradleText.AddSourceSet.buildText(highlighting)
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
                functions = listOf(
                    "kotlin.require",
                    "kotlin.test.assertTrue",
                    
                )
                includedSourceSets = listOf("main", "test")
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        GradleText.AddAssertEquals.buildText(highlighting)
    ).toList()
}

private fun build_AddAssertEquals_To_AddAssertNotNull(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    return startAnimation(
        GradleText.AddAssertEquals.buildText(highlighting)
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
                functions = listOf(
                    "kotlin.require",
                    "kotlin.test.assertTrue",
                    "kotlin.test.assertEquals",
                    
                )
                includedSourceSets = listOf("main", "test")
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        GradleText.AddAssertNotNull.buildText(highlighting)
    ).toList()
}

private fun build_AddAssertNotNull_To_AddAssertSoftly(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    return startAnimation(
        GradleText.AddAssertNotNull.buildText(highlighting) + AnnotatedString("\n")
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
                functions = listOf(
                    "kotlin.require",
                    "kotlin.test.assertTrue",
                    "kotlin.test.assertEquals",
                    "kotlin.test.assertNotNull",
                    
                )
                includedSourceSets = listOf("main", "test")
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        GradleText.AddAssertSoftly.buildText(highlighting)
    ).toList()
}
