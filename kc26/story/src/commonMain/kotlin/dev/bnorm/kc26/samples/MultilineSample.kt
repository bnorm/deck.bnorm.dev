package dev.bnorm.kc26.samples

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.toKotlin

object MultilineSample {
    val sample = """
        @Test fun test() {
            val protagonist = theFellowshipOfTheRing[0].shortName
            assert(protagonist in theLordOfTheRingsMovies[0].synopsis)
        }
    """.trimIndent().toKotlin {
        when (it) {
            "assert" -> staticFunctionCall
            "theFellowshipOfTheRing", "theLordOfTheRingsMovies" -> staticProperty
            "shortName", "synopsis" -> property
            else -> null
        }
    }

    val v20Output = """
            assert(protagonist in theLordOfTheRingsMovies[0].synopsis)
                   |           |  |                          |
                   |           |  |                          The Fellowship of the Ring embark on a journey to destroy the One Ring and end
            Sauron's reign over Middle-earth.
                   |           |  The Lord of the Rings (1978)
                   |           |  [The Lord of the Rings (1978), The Fellowship of the Ring (2001), The Two Towers (2002), The Return of the King (2003)]
                   |           false
                   Frodo
    """.trimIndent()

    val v23Output = """
            assert(protagonist in theLordOfTheRingsMovies[0].synopsis)
                   |           |  |                      |   |
                   |           |  |                      |   The Fellowship of the Ring embark on a journey to destroy the One Ring and end
            Sauron's reign over Middle-earth.
                   |           |  |                      The Lord of the Rings (1978)
                   |           |  [The Lord of the Rings (1978), The Fellowship of the Ring (2001), The Two Towers (2002), The Return of the King (2003)]
                   |           false
                   Frodo
    """.trimIndent()

    val v24Output = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                assert(protagonist in theLordOfTheRingsMovies[0].synopsis)
                       |           |  |                      |   |
                       |           ${s}|  |                      |   The Fellowship of the Ring embark on a journey to destroy the One Ring and end${s}
                       |           ${s}|  |                      |   Sauron's reign over Middle-earth.${s}
                       |           ${s}|  |                      The Lord of the Rings (1978)${s}
                       |           ${s}|  [The Lord of the Rings (1978), The Fellowship of the Ring (2001), The Two Towers (2002), The Return of the King (2003)]${s}
                       |           ${s}false${s}
                       ${s}Frodo${s}
            """.toCodeSample(),
            """
                assert(protagonist in theLordOfTheRingsMovies[0].synopsis)
                       |           |  |                      |   |
                       ${s}Frodo${s}       ${s}|  |                      |   The Fellowship of the Ring embark on a journey to destroy the One Ring and end${s}
                                   ${s}|  |                      |   Sauron's reign over Middle-earth.${s}
                                   ${s}|  |                      The Lord of the Rings (1978)${s}
                                   ${s}|  [The Lord of the Rings (1978), The Fellowship of the Ring (2001), The Two Towers (2002), The Return of the King (2003)]${s}
                                   ${s}false${s}
            """.toCodeSample(),
            """
                assert(protagonist in theLordOfTheRingsMovies[0].synopsis)
                       |           |  |                      |   |
                       "${s}Frodo${s}"     |  |                      |   ""${'"'}
                                   ${s}|  |                      |   The Fellowship of the Ring embark on a journey to destroy the One Ring and end${s}
                                   ${s}|  |                      |   Sauron's reign over Middle-earth.${s}
                                   |  |                      |   ""${'"'}
                                   ${s}|  |                      The Lord of the Rings (1978)${s}
                                   ${s}|  [The Lord of the Rings (1978), The Fellowship of the Ring (2001), The Two Towers (2002), The Return of the King (2003)]${s}
                                   ${s}false${s}
            """.toCodeSample(),
        )
    }
}