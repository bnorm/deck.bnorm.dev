package dev.bnorm.kc26.samples

import dev.bnorm.kc26.template.toKotlin

object ArraySample {
    val sample = """
        val a: Boolean? = false
        assert(arrayOf(false, a)[0]!!)
    """.trimIndent().toKotlin {
        when (it) {
            "assert", "arrayOf" -> staticFunctionCall
            else -> null
        }
    }

    val v20Output = """
        powerAssert(arrayOf(false, a)[0]!!)
                    |              |    |
                    |              |    false
                    |              false
                    false
                    [Ljava.lang.Boolean;@47caedad
    """.trimIndent()

    val v23Output = """
        powerAssert(arrayOf(false, a)[0]!!)
                    |              | |
                    |              | false
                    |              false
                    [Ljava.lang.Boolean;@47caedad
    """.trimIndent()
}