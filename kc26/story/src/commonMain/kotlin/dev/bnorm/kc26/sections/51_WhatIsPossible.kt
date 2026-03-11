package dev.bnorm.kc26.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc26.template.SectionBuilder
import dev.bnorm.kc26.template.SectionSceneScaffold
import dev.bnorm.kc26.template.WIP
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.code1
import dev.bnorm.kc26.template.code2
import dev.bnorm.kc26.template.toKotlin

fun SectionBuilder.WhatIsPossible() {
    nextSection("What's Possible?")

    carouselScene {
        SectionSceneScaffold { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // TODO example of basic JUnit integration
                    Text("JUnit integration example from KEEP")
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        Text(
                            """
                                @PowerAssert
                                fun powerAssert(condition: Boolean, @PowerAssert.Ignore message: String? = null) {
                                    contract { returns() implies condition } // Support smart-casts from the condition!

                                    if (!condition) {
                                        // If Power-Assert is not applied, fallback to using a simple message.
                                        val explanation = PowerAssert.explanation
                                            ?: throw AssertionFailedError(message)

                                        // Find all equality expressions that failed. 
                                        val equalityErrors = buildList {
                                            for (expression in explanation.expressions) {
                                                if (expression is EqualityExpression && expression.value == false) {
                                                    add(expression)
                                                }
                                            }
                                        }

                                        // Provide an OpenTest4J-compatible error message. 
                                        val failureMessage = buildString {
                                            // OpenTest4J likes to trim messages. Use zero-width space (U+200B) characters to preserve newlines.
                                            appendLine(message?.takeIf { it.isNotBlank() } ?: "\u200B")
                                            append(explanation.toDefaultMessage())
                                            append("\u200B")
                                        }

                                        // Based on the number of failed equality expressions, throw the appropriate error.
                                        throw when (equalityErrors.size) {
                                            0 -> AssertionFailedError(failureMessage)

                                            1 -> {
                                                val error = equalityErrors[0]
                                                AssertionFailedError(failureMessage, error.rhs, error.lhs)
                                            }

                                            else -> {
                                                MultipleFailuresError(
                                                    failureMessage,
                                                    equalityErrors.map { EqualityError(it) },
                                                )
                                            }
                                        }
                                    }
                                }
                            """.trimIndent().toKotlin(),
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                        )
                    }
                }
                WIP()
            }
        }
    }

    carouselScene {
        SectionSceneScaffold { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // TODO example more complex fluent assertions
                    Text("Fluent assertion example from KEEP")
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        Text(
                            """
                                @Test
                                fun simpleTest() {
                                    val subject = "Unknown"
                                    assertThat(subject) {
                                        hasLength("Kodee".length)
                                        startsWith("Kodee".substring(0, 1))
                                    }
                                }
                            """.trimIndent().toKotlin(),
                        )
                        ProvideTextStyle(MaterialTheme.typography.code2) {
                            Text(
                                """
                                Assertion failed:
                                 * String "Unknown" does not have length '5'.
                                 * String "Unknown" does not start with "K".
                                assertThat(subject) {
                                           |
                                           Unknown
                                
                                    hasLength("Kodee".length)
                                                      |
                                                      5
                                
                                    startsWith("Kodee".substring(0, 1))
                                                       |
                                                       K
                                
                                }
                            """.trimIndent(),
                            )
                        }
                    }
                }
                WIP()
            }
        }
    }

    carouselScene {
        SectionSceneScaffold { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // TODO non-assert use cases
                    Text("What about non-assert use cases?")
                    Text("Logging framework!")
                }
                WIP()
            }
        }
    }
}
