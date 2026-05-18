package dev.bnorm.kc26.samples

import dev.bnorm.kc26.template.toKotlin

object WhenSample {
    val sample = """
        @Test fun test() {
            for (character in theFellowshipOfTheRing) {
                assert(when (character.race) {
                    is Race.Men -> character.age in 0..<80
                    Race.Dwarf -> character.age in 0..<250
                    Race.Elf -> character.age >= 0
                    Race.Maia -> character.age == -1
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
        assert(
            when (mascot.status) {
            |
            false
                Status.INACTIVE -> mascot.location == null
                Status.ACTIVE -> mascot.location == "Munich"
            }
        )
    """.trimIndent()

    val v23Output = """
        assert(
            when (mascot.status) {
                  |      |
                  |      ACTIVE
                  Mascot(name=Kodee, status=ACTIVE, location=München)
        
                Status.INACTIVE -> mascot.location == "Munich"
                Status.ACTIVE -> mascot.location == "Munich"
                                 |      |        |
                                 |      |        false
                                 |      München
                                 Mascot(name=Kodee, status=ACTIVE, location=München)
        
            }
        )
    """.trimIndent()
}