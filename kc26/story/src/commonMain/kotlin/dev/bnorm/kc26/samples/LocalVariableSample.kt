package dev.bnorm.kc26.sections

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.storyboard.text.highlight.CodeScope

object LocalVariableSample {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(
            codeStyle = CODE_STYLE,
            scope = CodeScope.Function,
        ) { identifier ->
            when (identifier) {
                "powerAssert" -> CODE_STYLE.staticFunctionCall
                "trimIndent" -> CODE_STYLE.extensionFunctionCall
                "length" -> CODE_STYLE.property
                else -> null
            }
        }

        val s by tag("splitter")

        listOf(
            """
                @Test fun test() {
                    powerAssert(${s}"Hello".length${s} == ${s}"World".substring(1, 4).length${s})
                }
            """.toCodeSample(),
            """
                @Test fun test() {
                    ${s}${s}val expected = ${s}"Hello".length${s}
                    ${s}${s}val actual = ${s}"World".substring(1, 4).length${s}
                    powerAssert(${s}expected${s} == ${s}actual${s})
                }
            """.toCodeSample(),
            """
                @Test fun test() {
                    ${s}@PowerAssert ${s}val expected = ${s}"Hello".length${s}
                    ${s}@PowerAssert ${s}val actual = ${s}"World".substring(1, 4).length${s}
                    powerAssert(${s}expected${s} == ${s}actual${s})
                }
            """.toCodeSample(),
        )
    }

    val outputs = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                powerAssert(expected == actual)
                            |        |  |
                            5        ${s}|${s}  3
                                     ${s}false${s}

                Expected :3
                Actual   :5
                <Click to see difference>
            """.toCodeSample(),
            """
                val expected ${s}= "Hello".length${s}
                                       ${s}|${s}
                                       ${s}5${s}
                val actual ${s}= "World".substring(1, 4).length${s}
                                     ${s}|               |${s}
                                     ${s}"orl"           3${s}
                powerAssert(expected == actual)
                            |        |  |
                            5        ${s}|${s}  3
                                     ${s}false${s}

                Expected :3
                Actual   :5
                <Click to see difference>
            """.toCodeSample(),
            """
                powerAssert(expected == actual)
                            |        |  |
                            |        ${s}|${s}  ${s}= "World".substring(1, 4).length${s}
                            |        |            ${s}|               |${s}
                            |        |            ${s}"orl"           3${s}
                            |        ${s}false${s}
                            ${s}= "Hello".length${s}
                                      ${s}|${s}
                                      ${s}5${s}

                Expected :3
                Actual   :5
                <Click to see difference>
            """.toCodeSample(),
        )
    }
}