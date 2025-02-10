package dev.bnorm.evolved.sections.evolution

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.Evolution() {
    /*
     * 1. new annotations
     * 2. new data structures
     * 3. improvements to diagram
     */

    SectionAndTitle("Evolving Power-Assert") {
        PowerAssertEvolves()
        NewApi()
        FunctionTransformation()
        CallTransformation()
        ExplainCallExample()
    }
}
