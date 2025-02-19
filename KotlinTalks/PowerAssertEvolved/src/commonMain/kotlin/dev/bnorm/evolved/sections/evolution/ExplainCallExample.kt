package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.mac.MacTerminal
import dev.bnorm.evolved.template.Body
import dev.bnorm.evolved.template.CornerKodee
import dev.bnorm.evolved.template.Header
import dev.bnorm.evolved.template.code.KotlinFile
import dev.bnorm.evolved.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide

fun StoryboardBuilder.ExplainCallExample() {
    // TODO add side panels with full API of CallExplanation and Expression
    // TODO surrounding box for them as well?

    // TODO split into 2 separate slides with animation?
    slide(stateCount = 20) {
        CornerKodee {
            Column(Modifier.fillMaxSize()) {
                Header()
                Box(Modifier.fillMaxSize()) {
                    Body(Modifier.padding(top = 32.dp, start = 32.dp, end = 32.dp).fillMaxSize()) {
                        ProvideTextStyle(MaterialTheme.typography.h4) {
                            Text("How do we use it?")
                        }
                        ProvideTextStyle(MaterialTheme.typography.body2) {
                            when (currentState) {
                                in 1..11 -> {
                                    Box(Modifier.fillMaxSize()) {
                                        Column(Modifier.verticalScroll(rememberScrollState()).fillMaxSize()) {
                                            Text(EXAMPLE_POWER_ASSERT_FUN.toCode())
                                        }
                                    }
                                }

                                in 12..14 -> {
                                    Column(Modifier.fillMaxSize()) {
                                        Text(EXAMPLE_POWER_ASSERT_USE.toCode(identifierType = { highlighting, identifier ->
                                            when (identifier) {
                                                "test" -> highlighting.functionDeclaration
                                                "powerAssert" -> highlighting.staticFunctionCall
                                                else -> null
                                            }
                                        }))
                                        Box(Modifier.weight(1f))
                                        state.AnimatedVisibility(
                                            visible = { it.toState() == 13 },
                                            enter = slideInVertically(initialOffsetY = { it }),
                                            exit = slideOutVertically(targetOffsetY = { it }),
                                        ) {
                                            MacTerminal(modifier = Modifier.fillMaxWidth()) {
                                                Text(EXAMPLE_POWER_ASSERT_OUTPUT)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    KotlinFile(
                        text = CallExplanationCode.toCode(),
                        title = "CallExplanation",
                        visible = state.createChildTransition { it.toState() == 2 },
                        modifier = Modifier.align(Alignment.TopEnd),
                    )

                    KotlinFile(
                        text = ExpressionCode.toCode(),
                        title = "Expression",
                        visible = state.createChildTransition { it.toState() == 4 },
                        modifier = Modifier.align(Alignment.TopEnd),
                    )

                    // TODO have an example of toDefaultMessage?
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
        val explanation: CallExplanation? = ExplainCall.explanation
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

val CallExplanationCode = """
class CallExplanation(
    val offset: Int,
    val source: String,
    val dispatchReceiver: Receiver?,
    val contextArguments: Map<String, ValueArgument>,
    val extensionReceiver: Receiver?,
    val valueArguments: Map<String, ValueArgument>,
) {
    abstract class Argument() {
        abstract val startOffset: Int
        abstract val endOffset: Int
        abstract val expressions: List<Expression>
    }

    class ValueArgument(
        override val startOffset: Int,
        override val endOffset: Int,
        override val expressions: List<Expression>,
    ) : Argument()

    class Receiver(
        override val startOffset: Int,
        override val endOffset: Int,
        override val expressions: List<Expression>,
        val isImplicit: Boolean,
    ) : Argument()
    
    val expressions: List<Expression>
        get() = buildList {
            if (dispatchReceiver != null) {
                addAll(dispatchReceiver.expressions)
            }
            for (valueArgument in contextArguments.values) {
                addAll(valueArgument.expressions)
            }
            if (extensionReceiver != null) {
                addAll(extensionReceiver.expressions)
            }
            for (valueArgument in valueArguments.values) {
                addAll(valueArgument.expressions)
            }
        }
}
""".trimIndent()

val ExpressionCode = """
abstract class Expression {
    abstract val startOffset: Int
    abstract val endOffset: Int
    abstract val displayOffset: Int
    abstract val value: Any?
}

class ValueExpression(
    override val startOffset: Int,
    override val endOffset: Int,
    override val displayOffset: Int,
    override val value: Any?,
) : Expression()

class EqualityExpression(
    override val startOffset: Int,
    override val endOffset: Int,
    override val displayOffset: Int,
    override val value: Any?,
    val lhs: Any?,
    val rhs: Any?,
) : Expression()
""".trimIndent()
