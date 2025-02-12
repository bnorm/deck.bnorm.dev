package dev.bnorm.evolved.sections.today

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.Today() {
    SectionAndTitle("What We Have Today") {
        SimpleTransformation()
        ComplexTransformation()
        ExpressionTree()
        TransformTree()
        // TODO transformation

        // TODO function overloading
        // TODO diagram building?
    }
}
