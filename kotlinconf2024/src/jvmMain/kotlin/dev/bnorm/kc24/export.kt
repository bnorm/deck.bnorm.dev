package dev.bnorm.kc24

import dev.bnorm.storyboard.easel.export.exportAsPdf
import java.io.File

fun main() {
    exportAsPdf(KotlinPlusPowerAssertEqualsLove, File("test.pdf"))
}
