package dev.bnorm.evolved.sections.today

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.Today() {
    // TODO better title
    SectionAndTitle("How Does It Work?") {
        ExampleTransformation()
        // TODO function overloading here
        ComplexTransformation()
        WhenTransformation()
        ExpressionTree()
        TransformTree()
    }
}
