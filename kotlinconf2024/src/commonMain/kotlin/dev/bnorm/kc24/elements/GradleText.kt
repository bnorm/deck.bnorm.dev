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
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flowOf

enum class GradleText {
    Initial {
        override val text: AnnotatedString
            @Composable
            get() = rememberHighlighted("GradleText::Initial") {
                """
                    plugins {
                        kotlin("jvm") version "2.0.0"
                    }
                """.trimIndent().toKts(it)
            }
    },

    AddPlugin {
        override val text: AnnotatedString
            @Composable
            get() = rememberHighlighted("GradleText::AddPlugin") {
                """
                    plugins {
                        kotlin("jvm") version "2.0.0"
                        kotlin("plugin.power-assert") version "2.0.0"
                    }
                """.trimIndent().toKts(it)
            }
    },

    AddConfig {
        override val text: AnnotatedString
            @Composable
            get() = rememberHighlighted("GradleText::AddConfig") {
                """
                    plugins {
                        kotlin("jvm") version "2.0.0"
                        kotlin("plugin.power-assert") version "2.0.0"
                    }
                    
                    @OptIn(ExperimentalKotlinGradlePluginApi::class)
                    powerAssert {
                    }
                """.trimIndent().toKts(it)
            }
    },

    AddAssertTrue {
        override val text: AnnotatedString
            @Composable
            get() = rememberHighlighted("GradleText::AddAssertTrue") {
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
                """.trimIndent().toKts(it)
            }
    },

    AddRequire {
        override val text: AnnotatedString
            @Composable
            get() = rememberHighlighted("GradleText::AddRequire") {
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
                """.trimIndent().toKts(it)
            }
    },

    AddSourceSet {
        override val text: AnnotatedString
            @Composable
            get() = rememberHighlighted("GradleText::AddSourceSet") {
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
                """.trimIndent().toKts(it)
            }
    },

    AddAssertEquals {
        override val text: AnnotatedString
            @Composable
            get() = rememberHighlighted("GradleText::AddAssertEquals") {
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
                """.trimIndent().toKts(it)
            }
    },

    AddAssertNotNull {
        override val text: AnnotatedString
            @Composable
            get() = rememberHighlighted("GradleText::AddAssertNotNull") {
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
                """.trimIndent().toKts(it)
            }
    },

    AddAssertSoftly {
        override val text: AnnotatedString
            @Composable
            get() = rememberHighlighted("GradleText::AddAssertSoftly") {
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
                            "example.AssertScope.assert",
                        )
                        includedSourceSets.addAll("main", "test")
                    }
                """.trimIndent().toKts(it)
            }
    },

    ;

    @get:Composable
    abstract val text: AnnotatedString
}

@Composable
fun GradleText.animateTo(other: GradleText): ImmutableList<AnnotatedString> {
    require(this.ordinal < other.ordinal)

    flowOf("").buffer()

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
            GradleText.AddAssertNotNull -> list.addAll(Transitions.AddAssertNotNull_AddAssertSoftly.subList(0, Transitions.AddAssertNotNull_AddAssertSoftly.lastIndex))
            GradleText.AddAssertSoftly -> error("!")
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

private object Transitions {
    val Initial_AddPlugin: ImmutableList<AnnotatedString>
        @Composable
        get() {
            val start = GradleText.Initial.text
            val end = GradleText.AddPlugin.text
            return rememberHighlighted("GradleText::Initial_AddPlugin") {
                startAnimation(
                    start
                ).then(
                    """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                        
                        }
                    """.trimIndent().toKts(it)
                ).thenLineEndDiff(
                    end
                ).toList()
            }
        }

    val AddPlugin_AddConfig: ImmutableList<AnnotatedString>
        @Composable
        get() {
            val start = GradleText.AddPlugin.text
            val end = GradleText.AddConfig.text
            return rememberHighlighted("GradleText::AddPlugin_AddConfig") {
                startAnimation(
                    start + AnnotatedString("\n\n\n\n")
                ).thenLineEndDiff(
                    end
                ).toList()
            }
        }

    val AddConfig_AddAssertTrue: ImmutableList<AnnotatedString>
        @Composable
        get() {
            val start = GradleText.AddConfig.text
            val end = GradleText.AddAssertTrue.text
            return rememberHighlighted("GradleText::AddConfig_AddAssertTrue") {
                startAnimation(
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
                    """.trimIndent().toKts(it)
                ).then(
                    """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                            kotlin("plugin.power-assert") version "2.0.0"
                        }
                        
                        @OptIn(ExperimentalKotlinGradlePluginApi::class)
                        powerAssert {
                        
                        
                        }
                    """.trimIndent().toKts(it)
                ).then(
                    """
                        plugins {
                            kotlin("jvm") version "2.0.0"
                            kotlin("plugin.power-assert") version "2.0.0"
                        }
                        
                        @OptIn(ExperimentalKotlinGradlePluginApi::class)
                        powerAssert {
                        
                        
                        
                        }
                    """.trimIndent().toKts(it)
                ).thenLineEndDiff(
                    end
                ).toList()
            }
        }

    val AddAssertTrue_AddRequire: ImmutableList<AnnotatedString>
        @Composable
        get() {
            val start = GradleText.AddAssertTrue.text
            val end = GradleText.AddRequire.text
            return rememberHighlighted("GradleText::AddAssertTrue_AddRequire") {
                startAnimation(
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
                    """.trimIndent().toKts(it)
                ).thenLineEndDiff(
                    end
                ).toList()
            }
        }

    val AddRequire_AddSourceSet: ImmutableList<AnnotatedString>
        @Composable
        get() {
            val start = GradleText.AddRequire.text
            val end = GradleText.AddSourceSet.text
            return rememberHighlighted("GradleText::AddRequire_AddSourceSet") {
                startAnimation(
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
                    """.trimIndent().toKts(it)
                ).thenLineEndDiff(
                    end
                ).toList()
            }
        }

    val AddSourceSet_AddAssertEquals: ImmutableList<AnnotatedString>
        @Composable
        get() {
            val start = GradleText.AddSourceSet.text
            val end = GradleText.AddAssertEquals.text
            return rememberHighlighted("GradleText::AddSourceSet_AddAssertEquals") {
                startAnimation(
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
                    """.trimIndent().toKts(it)
                ).thenLineEndDiff(
                    end
                ).toList()
            }
        }

    val AddAssertEquals_AddAssertNotNull: ImmutableList<AnnotatedString>
        @Composable
        get() {
            val start = GradleText.AddAssertEquals.text
            val end = GradleText.AddAssertNotNull.text
            return rememberHighlighted("GradleText::AddAssertEquals_AddAssertNotNull") {
                startAnimation(
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
                    """.trimIndent().toKts(it)
                ).thenLineEndDiff(
                    end
                ).toList()
            }
        }

    val AddAssertNotNull_AddAssertSoftly: ImmutableList<AnnotatedString>
        @Composable
        get() {
            val start = GradleText.AddAssertNotNull.text
            val end = GradleText.AddAssertSoftly.text
            return rememberHighlighted("GradleText::AddAssertNotNull_AddAssertSoftly") { highlighting ->
                startAnimation(
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
        }
}
