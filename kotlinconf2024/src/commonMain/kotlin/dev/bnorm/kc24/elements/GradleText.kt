package dev.bnorm.kc24.elements

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.kc24.Theme
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.animation.then
import dev.bnorm.librettist.text.buildGradleKtsCodeString
import dev.bnorm.librettist.text.thenLineEndDiff
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

enum class GradleText {
    Initial {
        override val text: AnnotatedString = """
            plugins {
                kotlin("jvm") version "2.0.0"
            }
        """.trimIndent().toKts()
    },

    AddPlugin {
        override val text: AnnotatedString = """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
        """.trimIndent().toKts()
    },

    AddConfig {
        override val text: AnnotatedString = """
            plugins {
                kotlin("jvm") version "2.0.0"
                kotlin("plugin.power-assert") version "2.0.0"
            }
            
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            powerAssert {
            }
        """.trimIndent().toKts()
    },

    AddAssertTrue {
        override val text: AnnotatedString = """
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
        """.trimIndent().toKts()
    },

    AddRequire {
        override val text: AnnotatedString = """
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
        """.trimIndent().toKts()
    },

    AddSourceSet {
        override val text: AnnotatedString = """
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
        """.trimIndent().toKts()
    },

    AddAssertEquals {
        override val text: AnnotatedString = """
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
        """.trimIndent().toKts()
    },

    AddAssertNotNull {
        override val text: AnnotatedString = """
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
        """.trimIndent().toKts()
    },

    ;

    abstract val text: AnnotatedString
}

fun GradleText.animateTo(other: GradleText): ImmutableList<AnnotatedString> {
    require(this.ordinal < other.ordinal)

    val entries = GradleText.entries.subList(this.ordinal, other.ordinal)
    val list = mutableListOf<AnnotatedString>()
    for (entry in entries) {
        // @formatter:off
        when (entry) {
            GradleText.Initial -> list.addAll(Transitions.Initial_AddPlugin.subList(0, Transitions.Initial_AddPlugin.lastIndex))
            GradleText.AddPlugin -> list.addAll(Transitions.AddPlugin_AddConfig.subList(0, Transitions.AddPlugin_AddConfig.lastIndex))
            GradleText.AddConfig -> list.addAll(Transitions.AddConfig_AddAssertTrue.subList(0, Transitions.AddConfig_AddAssertTrue.lastIndex))
            GradleText.AddAssertTrue -> list.addAll(Transitions.AddAssertTrue_AddRequire.subList(0, Transitions.AddAssertTrue_AddRequire.lastIndex))
            GradleText.AddRequire -> list.addAll(Transitions.AddRequire_AddSourceSet.subList(0, Transitions.AddRequire_AddSourceSet.lastIndex))
            GradleText.AddSourceSet -> list.addAll(Transitions.AddSourceSet_AddAssertEquals.subList(0, Transitions.AddSourceSet_AddAssertEquals.lastIndex))
            GradleText.AddAssertEquals -> list.addAll(Transitions.AddAssertEquals_AddAssertNotNull.subList(0, Transitions.AddAssertEquals_AddAssertNotNull.lastIndex))
            GradleText.AddAssertNotNull -> error("!")
        }
        // @formatter:on
    }
    list.add(other.text)

    val maxLines = list.maxOf { it.text.count { it == '\n' } }
    return list.map {
        val numLines = it.text.count { it == '\n' }
        if (numLines < maxLines) {
            it + AnnotatedString("\n".repeat(maxLines - numLines))
        } else {
            it
        }
    }.toImmutableList()
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

private fun String.toKts(): AnnotatedString {
    // TODO pulling from ShowTheme means this would need to be @Composable
    //  - text has to get rebuilt for every new composable...
    //  - ...or does it? can we build at the top and pass down somehow?
    val codeStyle = Theme.codeStyle
    return buildGradleKtsCodeString(
        text = this,
        codeStyle = codeStyle,
        identifierType = { it.toStyle(codeStyle) }
    )
}

private object Transitions {
    val Initial_AddPlugin: ImmutableList<AnnotatedString> =
        startAnimation(
            GradleText.Initial.text
        ).then(
            """
                plugins {
                    kotlin("jvm") version "2.0.0"
                
                }
            """.trimIndent().toKts()
        ).thenLineEndDiff(
            GradleText.AddPlugin.text
        ).toList()

    val AddPlugin_AddConfig: ImmutableList<AnnotatedString> =
        startAnimation(
            GradleText.AddPlugin.text + AnnotatedString("\n\n\n\n")
        ).thenLineEndDiff(
            GradleText.AddConfig.text
        ).toList()

    val AddConfig_AddAssertTrue: ImmutableList<AnnotatedString> =
        startAnimation(
            GradleText.AddConfig.text
        ).then(
            """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
                
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                powerAssert {
                
                }
            """.trimIndent().toKts()
        ).then(
            """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
                
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                powerAssert {
                
                
                }
            """.trimIndent().toKts()
        ).then(
            """
                plugins {
                    kotlin("jvm") version "2.0.0"
                    kotlin("plugin.power-assert") version "2.0.0"
                }
                
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                powerAssert {
                
                
                
                }
            """.trimIndent().toKts()
        ).thenLineEndDiff(
            GradleText.AddAssertTrue.text
        ).toList()

    val AddAssertTrue_AddRequire: ImmutableList<AnnotatedString> =
        startAnimation(
            GradleText.AddAssertTrue.text
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
            """.trimIndent().toKts()
        ).thenLineEndDiff(
            GradleText.AddRequire.text
        ).toList()

    val AddRequire_AddSourceSet: ImmutableList<AnnotatedString> =
        startAnimation(
            GradleText.AddRequire.text
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
            """.trimIndent().toKts()
        ).thenLineEndDiff(
            GradleText.AddSourceSet.text
        ).toList()

    val AddSourceSet_AddAssertEquals: ImmutableList<AnnotatedString> =
        startAnimation(
            GradleText.AddSourceSet.text
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
            """.trimIndent().toKts()
        ).thenLineEndDiff(
            GradleText.AddAssertEquals.text
        ).toList()

    val AddAssertEquals_AddAssertNotNull: ImmutableList<AnnotatedString> =
        startAnimation(
            GradleText.AddAssertEquals.text
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
            """.trimIndent().toKts()
        ).thenLineEndDiff(
            GradleText.AddAssertNotNull.text
        ).toList()
}
