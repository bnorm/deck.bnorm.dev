package dev.bnorm.kc26.samples

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.CODE_STYLE

object BasicSample {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(
            codeStyle = CODE_STYLE,
        ) { identifier ->
            when (identifier) {
                "assert", "assertTrue", "assertEquals" -> CODE_STYLE.staticFunctionCall
                "size" -> CODE_STYLE.property
                "theLordOfTheRingsMovies" -> CODE_STYLE.staticProperty
                else -> null
            }
        }

        val s by tag("splitter")
        val base = """
            @Test fun test() {
                assert${s}${s}(theLordOfTheRingsMovies.size${s} == ${s}3)
            }
        """.trimIndent()

        listOf(
            base.toCodeSample(),
            base.replace("assert", "assert${s}True${s}").toCodeSample(),
            base.replace("assert", "assert${s}Equals${s}").replace(" == ", ", ").toCodeSample(),
        )
    }

    val outputs = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                java.lang.AssertionError:${s} Assertion failed${s}
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}${s}(theLordOfTheRingsMovies.size${s} == ${s}3)
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}${s}(theLordOfTheRingsMovies.size${s} == ${s}3)
                       ${s}|                       |    |${s}
                       |                       |${s}    false${s}
                       |                       4
                       [The Lord of the Rings (1978), The Fellowship of the Ring (2001), The Two Towers (2002), The Return of the King (2003)]
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}True${s}(theLordOfTheRingsMovies.size${s} == ${s}3)
                           ${s}|                       |    |${s}
                           |                       |${s}    false${s}
                           |                       4
                           [The Lord of the Rings (1978), The Fellowship of the Ring (2001), The Two Towers (2002), The Return of the King (2003)]
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}Equals${s}(theLordOfTheRingsMovies.size${s}, ${s}3)
                             |                       |${s}${s}
                             |                       4
                             [The Lord of the Rings (1978), The Fellowship of the Ring (2001), The Two Towers (2002), The Return of the King (2003)]
        
                Expected :4
                Actual   :3
                <Click to see difference>
            """.toCodeSample(),
        )
    }
}