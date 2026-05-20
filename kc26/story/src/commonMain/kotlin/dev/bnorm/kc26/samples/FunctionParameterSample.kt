package dev.bnorm.kc26.sections

import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.storyboard.text.highlight.CodeScope

object FunctionParameterSample {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(
            codeStyle = CODE_STYLE,
            scope = CodeScope.Function,
        ) { identifier ->
            when (identifier) {
                "powerAssert", "assertMember" -> CODE_STYLE.staticFunctionCall
                "size", "alive", "race", "age" -> CODE_STYLE.property
                "theFellowshipOfTheRing" -> CODE_STYLE.staticProperty
                "lastOrNull" -> CODE_STYLE.extensionFunctionCall
                else -> null
            }
        }

        val h by tag("hide")

        val base = """
                @Test fun test() {
                    assertMember(theFellowshipOfTheRing.lastOrNull()!!)
                }
                
                ${h}@PowerAssert ${h}fun assertMember(member: Character) {
                    powerAssert(member.alive)
                    powerAssert(when (member.race) {
                        is Race.Men -> member.age in 0..<100
                        Race.Dwarf -> member.age in 0..<250
                        Race.Elf -> member.age >= 0
                        Race.Maia -> member.age == -1
                    })
                }
            """.toCodeSample()

        listOf(
            base.hide(h),
            base,
        )
    }

    val outputs = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                powerAssert(member.alive)
                            |      |
                            |      false
                            Boromir
            """.toCodeSample(),
            """
                powerAssert(member.alive)
                            |      |
                            |      false
                            = theFellowshipOfTheRing.lastOrNull()!!
                              |                      |
                              |                      Boromir
                              [Frodo, Sam, Merry, Pippin, Gandalf, Aragorn, Legolas, Gimli, Boromir]
                            
            """.toCodeSample(),
        )
    }
}