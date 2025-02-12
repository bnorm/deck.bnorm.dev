package dev.bnorm.evolved.sections.evolution

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.Evolution() {
    SectionAndTitle("Evolving Power-Assert") {
        PowerAssertEvolves()
        NewApi()
        FunctionTransformation()
        CallTransformation()
        ExplainCallExample()
        // TODO something about toDefaultMessage to show diagram improvements for all diagrams
    }
}
