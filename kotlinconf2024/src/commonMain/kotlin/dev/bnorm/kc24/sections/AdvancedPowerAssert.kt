package dev.bnorm.kc24.sections

import androidx.compose.material.Text
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.librettist.section.section
import dev.bnorm.librettist.show.ShowBuilder

fun ShowBuilder.AdvancedPowerAssert() {
    section(title = { Text("Advanced Power-Assert") }) {
        slide { SectionHeader() }

        // TODO extra use cases like
        //  1. require/check
        //  2. soft-assert
        //  3. logging?
    }
}
