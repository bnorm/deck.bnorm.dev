package dev.bnorm.kc26.samples

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.CODE_STYLE

object IntroSample {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(
            codeStyle = CODE_STYLE,
        ) { identifier ->
            when (identifier) {
                "assert", "assertTrue", "assertEquals" -> CODE_STYLE.staticFunctionCall
                "substring" -> CODE_STYLE.extensionFunctionCall
                "length" -> CODE_STYLE.property
                else -> null
            }
        }

        val s by tag("splitter")
        val base = """
            @Test fun test() {
                val hello = "Hello"
                assert${s}${s}(hello.length${s} == ${s}"World".substring(1, 4).length)
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
                assert${s}${s}(hello.length${s} == ${s}"World".substring(1, 4).length)
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}${s}(hello.length${s} == ${s}"World".substring(1, 4).length)
                       |     |      ${s}|    ${s}      |               |
                       |     |      ${s}|    ${s}      |               3
                       |     |      ${s}|    ${s}      orl
                       |     |      ${s}false${s}
                       |     5
                       Hello
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}True${s}(hello.length${s} == ${s}"World".substring(1, 4).length)
                           |     |      ${s}|    ${s}      |               |
                           |     |      ${s}|    ${s}      |               3
                           |     |      ${s}|    ${s}      orl
                           |     |      ${s}false${s}
                           |     5
                           Hello
            """.toCodeSample(),
            """
                java.lang.AssertionError:
                assert${s}Equals${s}(hello.length${s}, ${s}"World".substring(1, 4).length)
                             |     |      ${s}   ${s}      |               |
                             |     |      ${s}   ${s}      |               3
                             |     |      ${s}   ${s}      orl
                             |     5
                             Hello
        
                Expected :5
                Actual   :3
                <Click to see difference>
            """.toCodeSample(),
        )
    }
}