package dev.bnorm.kc26.samples

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.kc26.template.toKotlin

object FunctionSample {
    val originalFun = buildCodeSamples {
        fun String.toCodeSample(): CodeSample {
            return CodeSample(lazy {
                var count = 0
                extractTags(this).toKotlin {
                    when (it) {
                        "PowerAssert" -> CODE_STYLE.annotation
                        "explanation" if count == 0 -> CODE_STYLE.staticProperty.also { count++ } // TODO weird hack to avoid local property coloring
                        "expressions", "size", "rhs", "lhs", "value" -> CODE_STYLE.property
                        "filterIsInstance", "filter", "toDefaultMessage", "map" -> CODE_STYLE.extensionFunctionCall
                        else -> null
                    }
                }
            })
        }

        val exp by tag("explanation call")
        val c by tag("collapse")
        val h by tag("hide")
        val f by tag("failures")
        val t by tag("throw")

        val transformed = """
            @PowerAssert fun powerAssert(condition: Boolean) {
                if (!condition) {
                    val explanation: CallExplanation? = ${exp}null${exp}
                        ?: throw AssertionError("Assertion failed")
                    ${c}// ...${c}
                }
            }
        """.trimIndent().toCodeSample().collapse(c)

        val sample = """
            @PowerAssert fun powerAssert(condition: Boolean) {
                if (!condition) {
                    val explanation: CallExplanation? = ${exp}PowerAssert.explanation${exp}
                        ?: throw AssertionError("Assertion failed")
                    ${c}
                    ${f}val failures = explanation.expressions
                        .filterIsInstance<EqualityExpression>()
                        .filter { it.value == false }${f}
            
                    ${t}val message = explanation.toDefaultMessage()
                    throw when (failures.size) {
                        0 -> AssertionFailedError(message)
                        1 -> AssertionFailedError(message, failures[0].rhs, failures[0].lhs)
                        else -> MultipleFailuresError(
                            heading = message, failures = failures.map { 
                                AssertionFailedError(null, it.rhs, it.lhs)
                            }
                        )
                    }${t}${c}
                }
            }${h}
            
            
            
            ${h}
        """.trimIndent().toCodeSample()

        listOf(
            sample.collapse(c).hide(h),
            transformed,
            transformed.focus(c),
            sample.collapse(c).hide(h),
            sample.focus(f),
            sample.focus(t).scroll(f),
        )
    }

    val syntheticFun = buildCodeSamples {
        fun String.toCodeSample(): CodeSample {
            return CodeSample(lazy {
                extractTags(this).toKotlin(identifierStyle = {
                    when (it) {
                        "explanation" -> CODE_STYLE.staticProperty
                        "PowerAssert" -> CODE_STYLE.annotation
                        else -> null
                    }
                })
            })
        }

        val c by tag("collapse")
        val h by tag("hide")
        val exp by tag("explanation call")
        val invoke by tag("explanation call")
        val name by tag("explanation call")
        val ann by tag("explanation call")
        val param by tag("explanation call")

        val base = """
        ${ann}@PowerAssert ${ann}fun ${name}`${name}powerAssert${name}${'$'}powerassert`${name}($param
            ${param}condition: Boolean${param},
            `${'$'}explanation`: () -> CallExplanation,
        ${param}) {
            if (!condition) {
                val explanation: CallExplanation? = ${exp}PowerAssert.explanation${exp}${h} ?: ${h}${invoke}`${'$'}explanation`.invoke()${invoke}
                    ?: throw AssertionError("Assertion failed")
                ${c}// ...${c}
            }
        }
    """.trimIndent().toCodeSample()

        base.hide(h, name, param, invoke).collapse(c).focus(h)
            .then { unfocus() }
            .then { reveal(name) }
            .then { hide(ann) }
            .then { reveal(param) }
            .then { hide(exp).reveal(invoke) }
            .then { focus(h) }
    }

    val arraySample = """
        val hello = "Hello"
        powerAssert(hello.length == "World".substring(1, 4).length)
    """.trimIndent().toKotlin {
        when (it) {
            "substring" -> extensionFunctionCall
            "length" -> property
            "powerAssert" -> staticFunctionCall
            else -> null
        }
    }

    val arrayOutput = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        listOf(
            """
                com.willowtreeapps.opentest4k.AssertionFailedError:
                powerAssert(hello.length == "World".substring(1, 4).length)
                            |     |      |          |               |
                            |     5      false      "orl"           3
                            "Hello"
            """.toCodeSample(),
            """
                com.willowtreeapps.opentest4k.AssertionFailedError:
                powerAssert(hello.length == "World".substring(1, 4).length)
                            |     |      |          |               |
                            |     5      false      "orl"           3
                            "Hello"

                Expected :3
                Actual   :5
                <Click to see difference>
            """.toCodeSample(),
        )
    }
}