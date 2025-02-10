package dev.bnorm.evolved.sections.today

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.Today() {
    /*
     * 1. function overloading
     * 2. expression tree
     * 3. transformation
     * 4. diagram
     */

    SectionAndTitle("What We Have Today") {
        SimpleTransformation()
        ComplexTransformation()
        ExpressionTree()
        // TODO transformation

        // TODO function overloading
        // TODO diagram building?
    }
}
