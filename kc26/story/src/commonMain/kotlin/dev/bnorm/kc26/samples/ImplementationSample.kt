package dev.bnorm.kc26.samples

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.storyboard.text.highlight.CodeScope

object ImplementationSample {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample {
            return trimIndent().toCodeSample(CODE_STYLE, scope = CodeScope.Function) { identifier ->
                when (identifier) {
                    "assert" -> CODE_STYLE.staticFunctionCall
                    "size" -> CODE_STYLE.property
                    "theLordOfTheRingsMovies" -> CODE_STYLE.staticProperty
                    "trimIndent", "toDefaultMessage" -> CODE_STYLE.extensionFunctionCall
                    else -> null
                }
            }
        }

        val m by tag("message")
        val samples = listOf(
            """
                assert(theLordOfTheRingsMovies.size == 3)
            """,
            """
                val tmp1 = theLordOfTheRingsMovies
                assert(tmp1.size == 3)
            """,
            """
                val tmp1 = theLordOfTheRingsMovies
                val tmp2 = tmp1.size
                assert(tmp2 == 3)
            """,
            """
                val tmp1 = theLordOfTheRingsMovies
                val tmp2 = tmp1.size
                val tmp3 = 3
                assert(tmp2 == tmp3)
            """,
            """
                val tmp1 = theLordOfTheRingsMovies
                val tmp2 = tmp1.size
                val tmp3 = 3
                val tmp4 = tmp2 == tmp3
                assert(tmp4)
            """,
            """
                val tmp1 = theLordOfTheRingsMovies
                val tmp2 = tmp1.size
                val tmp3 = 3
                val tmp4 = tmp2 == tmp3
                assert(tmp4) {${m}
                    "${'"'}"
                        assert(theLordOfTheRingsMovies.size == 3)
                               |                       |    |
                               |                       |    ${'$'}tmp4
                               |                       ${'$'}tmp2
                               ${'$'}tmp1
                    "${'"'}".trimIndent()
                ${m}}
            """,
        )

        val explanation =
            """
                val tmp1 = theLordOfTheRingsMovies
                val tmp2 = tmp1.size
                val tmp3 = 3
                val tmp4 = tmp2 == tmp3
                assert(tmp4) {${m}
                    CallExplanation(/* ... */).toDefaultMessage()
                ${m}}
            """

        samples.map { it.toCodeSample() }
            .then { explanation.toCodeSample() }
    }
}