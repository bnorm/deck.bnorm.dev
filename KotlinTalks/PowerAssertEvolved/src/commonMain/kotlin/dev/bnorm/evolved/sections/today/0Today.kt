package dev.bnorm.evolved.sections.today

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Today() {
    // TODO better title
    SectionAndTitle("How Does It Work?") {
        ExampleOutput()
        ExampleTransformation()
        FunctionOverloads()
        ComplexTransformation()
        WhenTransformation()
        ExpressionTree()
        TransformTree()
    }
}
