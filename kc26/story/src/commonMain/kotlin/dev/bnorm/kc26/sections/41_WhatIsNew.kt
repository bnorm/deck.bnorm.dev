package dev.bnorm.kc26.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc26.components.WIP
import dev.bnorm.kc26.sections.changing.NewMessageDiagramOutput
import dev.bnorm.kc26.sections.changing.PowerAssertDefaultMessage
import dev.bnorm.kc26.sections.changing.PowerAssertMessageExamples
import dev.bnorm.kc26.sections.new.*
import dev.bnorm.kc26.template.*

fun SectionBuilder.WhatIsNew() {
    // TODO Section: What's new for libraries wanting to use Power-Assert?
    //  - review how power-assert works
    //  - show change to `CallExplanation(...).toDefaultMessage()`
    //  - PowerAssert annotation
    //  - CallExplanation and related data structures
    //  - function transformation

    // TODO would it be better to show how CallExplanation with toDefaultMessage first?
    //  - they've already seen the transformation performed
    //  - would be easy to go back to that sample and show the use of CallExplanation instead

    nextSection("What's Changing?")

    NewMessageDiagramOutput()

    PowerAssertDefaultMessage()
    CallExplanationIntro()
    ExpressionIntro()

    PowerAssertMessageExamples()

    PowerAssertAnnotationIntro()
    PowerAssertFunctionTransformation()

    // TODO Expression subclasses
    carouselScene {
        SectionSceneScaffold { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Details about different Expression subclasses")
                    Text("     $BULLET_1 ValueExpression")
                    Text("     $BULLET_1 LiteralExpression")
                    Text("     $BULLET_1 EqualityExpression")
                }

                WIP(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    // TODO can we morph into Explanation instead?
    CallExplanationTransition()
    carouselScene {
        SectionSceneScaffold { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        Text(
                            """
                                abstract class Explanation {
                                    abstract val offset: Int
                                    abstract val source: String
                                    abstract val expressions: List<Expression>
                                }
                            """.trimIndent().toKotlin()
                        )
                    }
                }
            }
        }
    }
}
