package dev.bnorm.kc25.template.code

fun String.padLines(count: Int = 10): String {
    val lines = lineSequence().count()
    if (count <= lines) return this

    return buildString {
        append(this@padLines)
        repeat(count - lines) {
            append('\n')
        }
    }
}
