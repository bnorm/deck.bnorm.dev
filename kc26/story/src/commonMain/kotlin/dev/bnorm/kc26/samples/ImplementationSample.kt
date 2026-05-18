package dev.bnorm.kc26.samples

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.storyboard.text.highlight.CodeScope

object ImplementationSample {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample {
            return toCodeSample(CODE_STYLE, scope = CodeScope.Function) { identifier ->
                when (identifier) {
                    "assert" -> CODE_STYLE.staticFunctionCall
                    "trimIndent" -> CODE_STYLE.extensionFunctionCall
                    "length" -> CODE_STYLE.property
                    else -> null
                }
            }
        }

        val samples = listOf(
            """
                assert(str.length >= 1 && str[0] == 'x')
            """.trimIndent(),
            """
                val tmp1 = str
                assert(tmp1.length >= 1 && str[0] == 'x')
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                assert(tmp2 >= 1 && str[0] == 'x')
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                assert(tmp3 && str[0] == 'x')
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                assert(when {
                    tmp3 -> str[0] == 'x'
                    else -> false
                })
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> assert(str[0] == 'x')
                    else -> assert(false)
                }
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> {
                        val tmp4 = str
                        assert(tmp4[0] == 'x')
                    }
                    else -> assert(false)
                }
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> {
                        val tmp4 = str
                        val tmp5 = tmp4[0]
                        assert(tmp5 == 'x')
                    }
                    else -> assert(false)
                }
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> {
                        val tmp4 = str
                        val tmp5 = tmp4[0]
                        val tmp6 = tmp5 == 'x'
                        assert(tmp6)
                    }
                    else -> assert(false)
                }
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> {
                        val tmp4 = str
                        val tmp5 = tmp4[0]
                        val tmp6 = tmp5 == 'x'
                        assert(tmp6)
                    }
                    else -> assert(false) {
                        "${'"'}"
                            assert(str.length >= 1 && str[0] == 'x')
                                   |   |      |
                                   |   |      ${'$'}tmp3
                                   |   ${'$'}tmp2
                                   ${'$'}tmp1
                        "${'"'}".trimIndent()
                    }
                }
            """.trimIndent(),
        )

        val m by tag("message")
        val finalExample = """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6) {$m
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
                    $m}
                }
                else -> assert(false) {$m
                    "${'"'}"
                        assert(str.length >= 1 && str[0] == 'x')
                               |   |      |
                               |   |      ${'$'}tmp3
                               |   ${'$'}tmp2
                               ${'$'}tmp1
                    "${'"'}".trimIndent()
                $m}
            }
        """.trimIndent()

        samples.map { it.toCodeSample() }
            .then { finalExample.toCodeSample() }
            .then { collapse(m) }
    }
}