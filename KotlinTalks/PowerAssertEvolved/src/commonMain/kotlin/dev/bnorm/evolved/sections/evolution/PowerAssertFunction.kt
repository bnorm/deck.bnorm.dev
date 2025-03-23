package dev.bnorm.evolved.sections.evolution

import androidx.compose.animation.core.createChildTransition
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
import dev.bnorm.evolved.template.Body
import dev.bnorm.evolved.template.CornerKodee
import dev.bnorm.evolved.template.Header
import dev.bnorm.evolved.template.code.KotlinFile
import dev.bnorm.evolved.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.scene
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

fun StoryboardBuilder.PowerAssertFunction() {
    scene(
        stateCount = 7,
        enterTransition = SceneEnter(Alignment.CenterEnd),
        exitTransition = SceneExit(Alignment.CenterEnd),
    ) {
        CornerKodee {
            Column(Modifier.Companion.fillMaxSize()) {
                Header()
                Box(Modifier.Companion.fillMaxSize()) {
                    Body(Modifier.Companion.padding(top = 32.dp, start = 32.dp, end = 32.dp).fillMaxSize()) {
                        ProvideTextStyle(MaterialTheme.typography.body2) {
                            Box(Modifier.Companion.fillMaxSize()) {
                                Column(Modifier.Companion.verticalScroll(rememberScrollState()).fillMaxSize()) {
                                    Text(EXAMPLE_POWER_ASSERT_FUN.toCode())
                                }
                            }
                        }
                    }

                    KotlinFile(
                        text = CallExplanationCode.toCode(),
                        title = "CallExplanation",
                        visible = frame.createChildTransition { it.toState() == 1 },
                        modifier = Modifier.Companion.align(Alignment.Companion.TopEnd),
                    )

                    KotlinFile(
                        text = ExpressionCode.toCode(),
                        title = "Expression",
                        visible = frame.createChildTransition { it.toState() == 3 },
                        modifier = Modifier.Companion.align(Alignment.Companion.TopEnd),
                    )

                    KotlinFile(
                        text = ToDefaultMessageCode.toCode(),
                        title = "toDefaultMessage",
                        visible = frame.createChildTransition { it.toState() == 5 },
                        modifier = Modifier.Companion.align(Alignment.Companion.TopEnd),
                    )
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

val CallExplanationCode = """
class CallExplanation(
    val offset: Int,
    val source: String,
    val dispatchReceiver: Receiver?,
    val contextArguments: Map<String, ValueArgument>,
    val extensionReceiver: Receiver?,
    val valueArguments: Map<String, ValueArgument>,
) {
    abstract class Argument internal constructor() {
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
abstract class Expression internal constructor() {
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

val ToDefaultMessageCode = """
public fun CallExplanation.toDefaultMessage(
    render: Expression.() -> String? = Expression::render,
): String = buildString {
    // ...
}

public fun Expression.render(): String {
    if (this is EqualityExpression && value == false) {
        return "Expected <${'$'}{lhs.render()}>, actual <${'$'}{rhs.render()}>."
    }

    return value.render()
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun Any?.render(): String {
    return when (val value = this) {
        is Array<*> -> value.contentDeepToString()
        is ByteArray -> value.contentToString()
        is ShortArray -> value.contentToString()
        is IntArray -> value.contentToString()
        is LongArray -> value.contentToString()
        is BooleanArray -> value.contentToString()
        is CharArray -> value.contentToString()
        is UByteArray -> value.contentToString()
        is UShortArray -> value.contentToString()
        is UIntArray -> value.contentToString()
        is ULongArray -> value.contentToString()
        else -> value.toString()
    }
}
""".trimIndent()
