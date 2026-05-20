package dev.bnorm.kc26.samples

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.toKotlin

object MultilineSample {
    val stringSample = """
        @Test fun test() {
            val protagonist = theFellowshipOfTheRing[0].shortName
            val originalAnimated = theLordOfTheRingsMovies[0]
            assert(protagonist in originalAnimated.synopsis)
        }
    """.trimIndent().toKotlin {
        when (it) {
            "assert" -> staticFunctionCall
            "theFellowshipOfTheRing", "theLordOfTheRingsMovies" -> staticProperty
            "shortName", "synopsis" -> property
            else -> null
        }
    }

    val v24Output = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                assert(protagonist in originalAnimated.synopsis)
                       |           |  |                |
                       |           |  |                The Fellowship of the Ring embark on a journey to destroy the One Ring and end
                Sauron's reign over Middle-earth.
                       |           |  The Lord of the Rings (1978)
                       |           false
                       Frodo
            """.toCodeSample(),
            """
                assert(protagonist in originalAnimated.synopsis)
                       |           |  |                |
                       ${s}Frodo${s}       ${s}|  |                The Fellowship of the Ring embark on a journey to destroy the One Ring and end${s}
                                   ${s}|  |                Sauron's reign over Middle-earth.${s}
                                   |  The Lord of the Rings (1978)
                                   false
            """.toCodeSample(),
            """
                assert(protagonist in originalAnimated.synopsis)
                       |           |  |                |
                       "${s}Frodo${s}"     |  |                ""${'"'}
                                   ${s}|  |                The Fellowship of the Ring embark on a journey to destroy the One Ring and end${s}
                                   ${s}|  |                Sauron's reign over Middle-earth.${s}
                                   |  |                ""${'"'}
                                   |  The Lord of the Rings (1978)
                                   false
            """.toCodeSample(),
        )
    }
}