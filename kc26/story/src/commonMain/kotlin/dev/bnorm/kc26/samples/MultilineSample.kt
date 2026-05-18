package dev.bnorm.kc26.samples

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.toKotlin

object MultilineSample {
    val stringSample = """
        @Test fun test() {
            val a = "another string"
            val s = "this is a multiline\nstring. this is the second line"
            assert(a == s)
        }
    """.trimIndent().toKotlin {
        when (it) {
            "assert" -> staticFunctionCall
            else -> null
        }
    }

    val stringOutput = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                assert(a == s)
                       | |  |
                       | |  this is a multiline
                string. this is the second line
                       | false
                       another string
            """.toCodeSample(),
            """
                assert(a == s)
                       | |  |
                       | |  this is a multiline
                       | |  string. this is the second line
                       | false
                       ${s}another string${s}
            """.toCodeSample(),
            """
                assert(a == s)
                       | |  |
                       | |  ""${'"'}
                       | |  this is a multiline
                       | |  string. this is the second line
                       | |  ""${'"'}
                       | false
                       "${s}another string${s}"
            """.toCodeSample(),
        )
    }
}