package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import dev.bnorm.deck.shared.mac.MacTerminal
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide

fun StoryboardBuilder.ExplainCallExample() {
    slide(stateCount = 5) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("How do we use it?")
            }
            ProvideTextStyle(MaterialTheme.typography.body2) {
                when (currentState) {
                    1 -> Column(Modifier.verticalScroll(rememberScrollState()).fillMaxSize()) {
                        Text(EXAMPLE_POWER_ASSERT_FUN.toCode())
                    }

                    in 2..4 -> {
                        Column(Modifier.fillMaxSize()) {
                            Text(EXAMPLE_POWER_ASSERT_USE.toCode(identifierType = { highlighting, identifier ->
                                when (identifier) {
                                    "test" -> highlighting.functionDeclaration
                                    "powerAssert" -> highlighting.staticFunctionCall
                                    else -> null
                                }
                            }))
                            Box(Modifier.weight(1f))
                            // TODO terminal pop-up like in KotlinConf 2024
                            state.AnimatedVisibility(
                                visible = { it.toState() == 3 },
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { it }),
                            ) {
                                ProvideTextStyle(MaterialTheme.typography.body2) {
                                    MacTerminal(modifier = Modifier.fillMaxWidth()) {
                                        Text(EXAMPLE_POWER_ASSERT_OUTPUT)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val EXAMPLE_POWER_ASSERT_FUN = """
@ExplainCall
fun powerAssert(condition: Boolean) {
    contract { returns() implies condition }

    if (!condition) {
        val explanation = ExplainCall.explanation
            ?: throw AssertionError("Assertion failed")

        val equalityErrors = buildList {
            for (expression in explanation.expressions) {
                if (expression is EqualityExpression && expression.value == false) {
                    add(expression)
                }
            }
        }

        val message = explanation.toDefaultMessage { value.toString() }
        throw when (equalityErrors.size) {
            0 -> AssertionFailedError("Assertion failed:\n${'$'}message")

            1 -> {
                val error = equalityErrors[0]
                AssertionFailedError(
                    "Assertion failed:\n${'$'}message",
                    error.lhs.toString(),
                    error.rhs.toString(),
                )
            }

            else -> {
                MultipleFailuresError(
                    "Assertion failed:\n${'$'}message",
                    equalityErrors.map { EqualityError(it) },
                )
            }
        }
    }
}

class EqualityError(
    expression: EqualityExpression
) : AssertionFailedError(
    "Expected <${'$'}{expression.lhs}>, actual <${'$'}{expression.rhs}>",
    expression.lhs.toString(),
    expression.rhs.toString(),
) {
    override fun fillInStackTrace(): Throwable = this // Stack trace is unnecessary.
}





""".trimIndent()

val EXAMPLE_POWER_ASSERT_USE = """
@Test fun test() {
    powerAssert("Hello".length == "World".substring(1, 4).length)
}
""".trimIndent()

val EXAMPLE_POWER_ASSERT_OUTPUT = """
Assertion failed:
powerAssert("Hello".length == "World".substring(1, 4).length)
                    |      |          |               |
                    5      false      orl             3

Expected :5
Actual   :3
<Click to see difference>
""".trimIndent()
