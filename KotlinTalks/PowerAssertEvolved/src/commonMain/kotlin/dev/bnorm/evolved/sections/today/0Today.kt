package dev.bnorm.evolved.sections.today

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.Today() {
    SectionAndTitle("All The Details!") {
        ExampleTransformation()
        // TODO function overloading here?
        ComplexTransformation()
        ExpressionTree()
        TransformTree()
        // TODO diagram building?
    }
}
