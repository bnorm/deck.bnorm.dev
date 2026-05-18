package dev.bnorm.kc26.samples

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.toKotlin

object Array2Sample {
    val arraySample = """
        @Test fun test() {
            assert(arrayOf(true, false)[1])
        }
    """.trimIndent().toKotlin {
        when (it) {
            "assert", "arrayOf" -> staticFunctionCall
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
                assert(arrayOf(true, false)[1])
                       |                   |
                       |                   false
                       [Ljava.lang.Boolean;@47caedad
            """.toCodeSample(),
            """
                assert(arrayOf(true, false)[1])
                       |                   |
                       |                   ${s}false${s}
                       ${s}[true, false]${s}
            """.toCodeSample(),
            """
                assert(arrayOf(true, false)[1])
                       |                   |
                       ${s}[true, false]${s}       ${s}false${s}
            """.toCodeSample(),
        )
    }
}