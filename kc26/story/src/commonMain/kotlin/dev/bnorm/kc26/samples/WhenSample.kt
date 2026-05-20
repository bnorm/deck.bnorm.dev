package dev.bnorm.kc26.samples

import dev.bnorm.kc26.template.toKotlin

object WhenSample {
    val sample = """
        @Test fun test() {
            for (member in theFellowshipOfTheRing) {
                assert(when (member.race) {
                    is Race.Men -> member.age in 0..<80
                    Race.Dwarf -> member.age in 0..<250
                    Race.Elf -> member.age >= 0
                    Race.Maia -> member.age == -1
                })
            }
        }
    """.trimIndent().toKotlin {
        when (it) {
            "assert" -> staticFunctionCall
            "theFellowshipOfTheRing" -> staticProperty
            "race", "age" -> property
            else -> null
        }
    }

    val v20Output = """
        assert(when (member.race) {
               |
               false
            is Race.Men -> member.age in 0..<80
            Race.Dwarf -> member.age in 0..<250
            Race.Elf -> member.age >= 0
            Race.Maia -> member.age == -1
        })
    """.trimIndent()

    val v23Output = """
        assert(when (member.race) {
                     |      |
                     |      Dúnadan
                     Aragorn
        
            is Race.Men -> member.age in 0..<80
            |              |      |   |   |
            |              |      |   |   0..79
            |              |      |   false
            |              |      87
            |              Aragorn
            true
        
            Race.Dwarf -> member.age in 0..<250
            Race.Elf -> member.age >= 0
            Race.Maia -> member.age == -1
        })
    """.trimIndent()
}