package dev.bnorm.dcnyc25.template

import dev.bnorm.dcnyc25.CodeString

val FirstSample = CodeString(
    """
        fun main() {
          println("Hello, KotlinConf!")
        }
    """.trimIndent()
)

val SecondSample = CodeString(
    """
        fun main() {
          println("Hello, droidcon!")
        }
    """.trimIndent()
)

val ThirdSample = CodeString(
    $$"""
        fun main() {
          val greeting = "Hello"
          println("$greeting, droidcon!")
        }
    """.trimIndent()
)

val ForthSample = $$"""
    fun main() {
      val greeting = "Hello"
      val name = "droidcon"
      println("$greeting, $name!")
    }
""".trimIndent()

val FifthSample = $$"""
    fun main(
      greeting: String,
      name: String,
    ) {
      println("$greeting, $name!")
    }
""".trimIndent()
