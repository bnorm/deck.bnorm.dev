package dev.bnorm.kc26.sections

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.storyboard.text.highlight.CodeScope

object FunctionParameterSample {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(
            codeStyle = CODE_STYLE,
            scope = CodeScope.Function,
        ) { identifier ->
            when (identifier) {
                "powerAssert" -> CODE_STYLE.staticFunctionCall
                "size" -> CODE_STYLE.property
                "theLordOfTheRingsMovies" -> CODE_STYLE.staticProperty
                else -> null
            }
        }

        val s by tag("splitter")

        listOf(
            """
                @Test fun test() {
                    powerAssert(${s}theLordOfTheRingsMovies.size${s} == ${s}3${s})
                }
            """.toCodeSample(),
            """
                @Test fun test() {
                    ${s}${s}val actual = ${s}theLordOfTheRingsMovies.size${s}
                    ${s}${s}val expected = ${s}3${s}
                    powerAssert(${s}actual${s} == ${s}expected${s})
                }
            """.toCodeSample(),
            """
                @Test fun test() {
                    ${s}@PowerAssert ${s}val actual = ${s}theLordOfTheRingsMovies.size${s}
                    ${s}@PowerAssert ${s}val expected = ${s}3${s}
                    powerAssert(${s}actual${s} == ${s}expected${s})
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
                powerAssert(actual == expected)
                            |     |  |
                            4     ${s}|${s}  3
                                  ${s}false${s}

                Expected :3
                Actual   :4
                <Click to see difference>
            """.toCodeSample(),
            """
                val actual ${s}= theLordOfTheRingsMovies.size${s}
                             ${s}|                       |${s}
                             ${s}|                       4${s}
                             ${s}[The Lord of the Rings (1978), The Fellowship of the Ring (2001), The Two Towers (2002), The Return of the King (2003)]${s}
                val expected ${s}= 3${s}
                powerAssert(actual == expected)
                            |      |  |
                            4      ${s}|${s}  3
                                   ${s}false${s}

                Expected :3
                Actual   :4
                <Click to see difference>
            """.toCodeSample(),
            """
                powerAssert(actual == expected)
                            |      |  |
                            |      ${s}|${s}  ${s}= 3${s}
                            |      ${s}false${s}
                            ${s}= theLordOfTheRingsMovies.size${s}
                              ${s}|                       |${s}
                              ${s}|                       4${s}
                              ${s}[The Lord of the Rings (1978), The Fellowship of the Ring (2001), The Two Towers (2002), The Return of the King (2003)]${s}

                Expected :3
                Actual   :4
                <Click to see difference>
            """.toCodeSample(),
        )
    }
}