package dev.bnorm.kc26.samples

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.toKotlin

object ArraySample {
    val sample = """
        @Test fun test() {
            val members = theFellowshipOfTheRing
            assert(members.lastOrNull()!!.alive)
        }
    """.trimIndent().toKotlin {
        when (it) {
            "theFellowshipOfTheRing" -> staticProperty
            "alive" -> property
            "lastOrNull" -> extensionFunctionCall
            else -> null
        }
    }

    val v20Output = """
        assert(members.lastOrNull()!!.alive)
               |       |           |  |
               |       |           |  false
               |       |           Boromir
               |       Boromir
               [LCharacter;@7cb502c
    """.trimIndent()

    val v23Output = """
        assert(members.lastOrNull()!!.alive)
               |       |              |
               |       |              false
               |       Boromir
               [LCharacter;@6304101a
    """.trimIndent()

    val v24Output = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                assert(members.lastOrNull()!!.alive)
                       |       |              |
                       |       |              false
                       |       Boromir
                       [LCharacter;@6304101a
            """.toCodeSample(),
            """
                assert(members.lastOrNull()!!.alive)
                       |       |              |
                       |       |              ${s}false${s}
                       |       ${s}Boromir${s}
                       ${s}[Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli, Boromir]${s}
            """.toCodeSample(),
            """
                assert(members.lastOrNull()!!.alive)
                       |       |              |
                       |       ${s}Boromir${s}        ${s}false${s}
                       ${s}[Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli, Boromir]${s}
            """.toCodeSample(),
        )
    }
}