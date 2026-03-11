package dev.bnorm.kc26.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.kc26.sections.next.PowerAssertLocalVariables
import dev.bnorm.kc26.template.*

fun SectionBuilder.WhatIsNext() {
    nextSection("What's Next?")

    carouselScene {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding)) {
                // TODO the elephant ("not that elephant") : kotlin-test support
                Text("Slide about kotlin-test support")
                WIP()
            }
        }
    }

    // TODO local variables (VariableExplanation and VariableAccessExpression)
    PowerAssertLocalVariables()

    carouselScene {
        SectionSceneScaffold { padding ->
            Box(Modifier.padding(padding)) {
                Column {
                    // TODO parameter forwarding (ArgumentExplanation and VariableAccessExpression)
                    Text("Slide about CallExplanation forwarding to other function calls")
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        Text(
                            """
                                @Test fun test() {
                                    doTest("Hello")
                                    doTest("World")
                                }
                                
                                @PowerAssert fun doTest(subject: String) {
                                    assertIsCapitalized(subject)
                                    assertEquals(subject.length, 5)
                                }
                                
                                @PowerAssert fun assertIsCapitalized(subject: String) { /* ... */ }
                            """.trimIndent().toKotlin { identifier ->
                                when (identifier) {
                                    "assertIsCapitalized", "assertEquals" -> staticFunctionCall
                                    else -> null
                                }
                            }
                        )
                    }
                }
                WIP(Modifier.align(Alignment.Center))
            }
        }
    }

    carouselScene {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding)) {
                // TODO diffs
                Text("Equality diffs for strings and collections")
                WIP()
            }
        }
    }

    carouselScene {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding)) {
                // TODO pprintln (StringTemplateExpression)
                Text("Pretty println with StringTemplateExpression")
                WIP()
            }
        }
    }

    carouselScene {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding)) {
                // TODO deep IntelliJ integration (ExplainedException for test failures and Explanations for debugging)
                Text("deep IntelliJ integration via Explanation in an exception")
                Text("deep IntelliJ debugger integration")
                WIP()
            }
        }
    }

    // TODO future ideas
    //  - AST
    //  - cite replacement? (additional source information)
    //  - oh god, lambda tracing...

    carouselScene {
        SectionSceneScaffold { padding ->
            Column(Modifier.padding(padding)) {
                // TODO generic Explain feature in Kotlin
                //  - balancing features, security, and performance
                //  - syntax idea? calls and variables
                Text("looking forward to power-assert as a language feature")
                Text("    $BULLET_1 features")
                Text("    $BULLET_1 performance")
                Text("    $BULLET_1 security")

                WIP()
            }
        }
    }

    // TODO a better ending...
}
