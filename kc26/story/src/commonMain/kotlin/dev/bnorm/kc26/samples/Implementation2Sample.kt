package dev.bnorm.kc26.sections

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.CODE_STYLE

object Implementation2Sample {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(CODE_STYLE) { identifier ->
            when (identifier) {
                "assert" -> CODE_STYLE.staticFunctionCall
                "trimIndent", "toDefaultMessage" -> CODE_STYLE.extensionFunctionCall
                "length" -> CODE_STYLE.property
                else -> null
            }
        }

        val e by tag("explanation")
        val m by tag("default message")

        val start = """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6) {$e
                        "${'"'}"
                            assert(str.length >= 1 && str[0] == 'x')
                                   |   |      |       |  |   |
                                   |   |      |       |  |   ${'$'}tmp6
                                   |   |      |       |  ${'$'}tmp5
                                   |   |      |       ${'$'}tmp4
                                   |   |      ${'$'}tmp3
                                   |   ${'$'}tmp2
                                   ${'$'}tmp1
                        "${'"'}".trimIndent()
                    $e}
                }
                else -> assert(false) {$e
                    "${'"'}"
                        assert(str.length >= 1 && str[0] == 'x')
                               |   |      |
                               |   |      ${'$'}tmp3
                               |   ${'$'}tmp2
                               ${'$'}tmp1
                    "${'"'}".trimIndent()
                $e}
            }
        """.toCodeSample()

        val end = """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6) {$e
                        CallExplanation(/* ... */)${m}.toDefaultMessage()${m}
                    $e}
                }
                else -> assert(false) {$e
                    CallExplanation(/* ... */)${m}.toDefaultMessage()${m}
                $e}
            }
        """.toCodeSample()

        start.collapse(e)
            .then { reveal(e) }
            .then { end }
    }
}