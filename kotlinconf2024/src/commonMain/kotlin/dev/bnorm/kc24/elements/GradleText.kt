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
                    functions.addAll(
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
                    functions.addAll(
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
                    functions.addAll(
                        "kotlin.require",
                        "kotlin.test.assertTrue",
                    )
                    includedSourceSets.addAll("main", "test")
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
                    functions.addAll(
                        "kotlin.require",
                        "kotlin.test.assertTrue",
                        "kotlin.test.assertEquals",
                    )
                    includedSourceSets.addAll("main", "test")
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
                    functions.addAll(
                        "kotlin.require",
                        "kotlin.test.assertTrue",
                        "kotlin.test.assertEquals",
                        "kotlin.test.assertNotNull",
                    )
                    includedSourceSets.addAll("main", "test")
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
                    functions.addAll(
                        "kotlin.require",
                        "kotlin.test.assertTrue",
                        "kotlin.test.assertEquals",
                        "kotlin.test.assertNotNull",
                        "example.AssertScope.assert",
                    )
                    includedSourceSets.addAll("main", "test")
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
    require(this.ordinal < other.ordinal)

    return rememberHighlighted("GradleText.animateTo::${name}_${other.name}") { highlighting ->
        val entries = GradleText.entries.subList(this.ordinal, other.ordinal)
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
        list.add(other.buildText(highlighting))

        val maxLines = list.maxOf { it.text.count { it == '\n' } }
        list.map {
            val numLines = it.text.count { it == '\n' }
            if (numLines < maxLines) {
                it + AnnotatedString("\n".repeat(maxLines - numLines))
            } else {
                it
            }
        }.toImmutableList()
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
    val start = GradleText.Initial.buildText(highlighting)
    val end = GradleText.AddPlugin.buildText(highlighting)
    return startAnimation(
        start
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
            
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        end
    ).toList()
}

private fun build_AddPlugin_To_AddConfig(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    val start = GradleText.AddPlugin.buildText(highlighting)
    val end = GradleText.AddConfig.buildText(highlighting)
    return startAnimation(
        start + AnnotatedString("\n\n\n\n")
    ).thenLineEndDiff(
        end
    ).toList()
}

private fun build_AddConfig_To_AddAssertTrue(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    val start = GradleText.AddConfig.buildText(highlighting)
    val end = GradleText.AddAssertTrue.buildText(highlighting)
    return startAnimation(
        start
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
        end
    ).toList()
}

private fun build_AddAssertTrue_To_AddRequire(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    val start = GradleText.AddAssertTrue.buildText(highlighting)
    val end = GradleText.AddRequire.buildText(highlighting)
    return startAnimation(
        start
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
                functions.addAll(
                    
                    "kotlin.test.assertTrue",   
                )
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        end
    ).toList()
}

private fun build_AddRequire_To_AddSourceSet(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    val start = GradleText.AddRequire.buildText(highlighting)
    val end = GradleText.AddSourceSet.buildText(highlighting)
    return startAnimation(
        start
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
                functions.addAll(
                    "kotlin.require",
                    "kotlin.test.assertTrue",
                )
                
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        end
    ).toList()
}

private fun build_AddSourceSet_To_AddAssertEquals(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    val start = GradleText.AddSourceSet.buildText(highlighting)
    val end = GradleText.AddAssertEquals.buildText(highlighting)
    return startAnimation(
        start
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
                functions.addAll(
                    "kotlin.require",
                    "kotlin.test.assertTrue",
                    
                )
                includedSourceSets.addAll("main", "test")
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        end
    ).toList()
}

private fun build_AddAssertEquals_To_AddAssertNotNull(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    val start = GradleText.AddAssertEquals.buildText(highlighting)
    val end = GradleText.AddAssertNotNull.buildText(highlighting)
    return startAnimation(
        start
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
                functions.addAll(
                    "kotlin.require",
                    "kotlin.test.assertTrue",
                    "kotlin.test.assertEquals",
                    
                )
                includedSourceSets.addAll("main", "test")
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        end
    ).toList()
}

private fun build_AddAssertNotNull_To_AddAssertSoftly(highlighting: Highlighting): ImmutableList<AnnotatedString> {
    val start = GradleText.AddAssertNotNull.buildText(highlighting)
    val end = GradleText.AddAssertSoftly.buildText(highlighting)
    return startAnimation(
        start + AnnotatedString("\n")
    ).then(
        """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
                functions.addAll(
                    "kotlin.require",
                    "kotlin.test.assertTrue",
                    "kotlin.test.assertEquals",
                    "kotlin.test.assertNotNull",
                    
                )
                includedSourceSets.addAll("main", "test")
            }
        """.trimIndent().toKts(highlighting)
    ).thenLineEndDiff(
        end
    ).toList()
}
